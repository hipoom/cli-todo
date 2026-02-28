package com.hipoom.cli.todo.handler.template


import com.google.gson.GsonBuilder
import com.hipoom.cli.core.ui.TextEditor
import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.scaffold.utils.readInt
import com.hipoom.cli.scaffold.utils.readLines
import com.hipoom.cli.scaffold.utils.readString
import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.findTemplateWithAlias
import com.hipoom.cli.todo.handler.template.entity.TemplateVO
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.templates
import com.hipoom.cli.todo.updateTemplates
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/2/2 17:22
 */
class TemplateHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options = templateOptions

    override val supportPrefixes: List<String> = listOf("temp")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Template"

    override fun onHandle(originParams: String, commandLine: CommandLine, app: CliApp, workspace: WorkspaceContext): Boolean {
        when {
            // list
            commandLine.hasOption("l") -> listTemplate(workspace)
            // create
            commandLine.hasOption("c") -> createTemplate(workspace, commandLine)
            // edit
            commandLine.hasOption("e") -> editTemplate(workspace, commandLine)
            // delete
            commandLine.hasOption("d") -> deleteTemplate(workspace)
            // help
            commandLine.hasOption("h") -> printHelp()
            // 其他未知的指令
            else -> printHelp()
        }

        return true
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun listTemplate(workspace: WorkspaceContext) {
        workspace.templates().templates.forEach { template ->
            printLine("序号: " + template.id + ", 模板名称: " + template.alias)
            template.items.forEachIndexed { index, item ->
                printLine("${index}. " + item)
            }
            printLine()
        }
    }

    private fun createTemplate(workspace: WorkspaceContext, commandLine: CommandLine) {
        var alias = commandLine.getOptionValue("c")

        if (alias.isNullOrEmpty()) {
            alias = readString("请输入模板名称")
        }
        if (alias == null) {
            return
        }

        val old = workspace.findTemplateWithAlias(alias)
        if (old != null) {
            printLine("已经存在过同名的模板了.")
            printLine()
            return
        }

        val useWindowMode = Configs.window.isEnable
        val lines =
            if (useWindowMode) {
                TextEditor.edit(
                    input = "",
                    prompt = "请输入该模板的所有子项，每行一个："
                ).split("\n")
            }
            else {
                printLine("请输入该模板的所有子项，每行一个（输入 exit 结束）：")
                readLines("exit")
            }.filter { it.isNotEmpty() }

        // 产生一个 id
        val id = workspace.increaseAndGetIndex("template")

        val template = TemplateVO(
            id = id,
            alias = alias,
            items = lines
        )

        // 写入到数据库中
        val templates = workspace.templates()
        templates.templates.add(template)
        workspace.updateTemplates(templates)

        printLine("模板添加成功，模板 id = " + template.id)
        printLine()
    }

    private fun editTemplate(workspace: WorkspaceContext, commandLine: CommandLine) {
        val useWindowMode = Configs.window.isEnable
        if (!useWindowMode) {
            printLine("目前只支持在 window 模式下编辑模板. 可以通过 window --enable 开启 window 模式.")
            return
        }

        printLine("当前存在的模板：")
        workspace.templates().templates.forEach { template ->
            printLine("序号: " + template.id + ", 模板名称: " + template.alias)
        }

        val specifiedId = commandLine.getOptionValue("e")
        val id = specifiedId.toIntOrNull() ?: readInt("请输入要编辑的模板的序号")
        if (id == null) {
            return
        }

        val temps = workspace.templates()
        val temp = temps.templates.find { it.id == id }
        if (temp == null) {
            printLine("没有找到对应序号的模板.")
            return
        }
        val gson = GsonBuilder().setPrettyPrinting().create()
        val json = gson.toJson(temp)

        // 在窗口模式下编辑
        val new = TextEditor.edit(input = json, prompt = "请按照 json 格式编辑内容")
        val newTemp = gson.fromJson(new, TemplateVO::class.java)
        if (newTemp == null) {
            printLine("无法根据内容生成模板内容.")
            return
        }

        // 移除旧的
        temps.templates.remove(temp)

        // 增加新的
        temps.templates.add(newTemp)

        // 更新到文件
        workspace.updateTemplates(templates = temps)
        printLine("模板已更新:")

        // 展示一次
        listTemplate(workspace)
    }

    private fun deleteTemplate(workspace: WorkspaceContext) {
        workspace.templates().templates.forEach { template ->
            printLine("序号: " + template.id + ", 模板名称: " + template.alias)
        }

        val id = readInt("请输入要删除的模板的序号") ?: return
        val temps = workspace.templates()
        temps.templates.removeIf { it.id == id }
        workspace.updateTemplates(templates = temps)

        printLine("删除成功，当前的模板列表：")
        workspace.templates().templates.forEach { template ->
            printLine("序号: " + template.id + ", 模板名称: " + template.alias)
        }

        printLine()
    }
}