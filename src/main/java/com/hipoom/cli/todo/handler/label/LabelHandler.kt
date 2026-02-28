package com.hipoom.cli.todo.handler.label

import com.google.gson.Gson
import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.app
import com.hipoom.cli.todo.gson
import com.hipoom.cli.todo.handler.show.show
import com.hipoom.cli.todo.handler.show.showItemsAsTreeMode
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.utils.parseIds
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options


class LabelHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = labelOptions

    override val supportPrefixes: List<String> = listOf("label")

    companion object {

        private const val AUTO_LABEL_RULES_KEY = "auto_label_rules"

    }



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description(): String {
        return "Label"
    }

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            commandLine.hasOption("f") -> { findLabelsAndShow(commandLine, workspace) }
            commandLine.hasOption("a") -> { addLabel(commandLine, workspace) }
            commandLine.hasOption("hide") -> { hideWithLabels(commandLine, workspace) }
            commandLine.hasOption("unhide") -> { unhideWithLabels(commandLine, workspace) }
            commandLine.hasOption("auto-add") -> { addAutoLabelRule(commandLine, workspace) }
            commandLine.hasOption("h") -> printHelp()
            else -> {
                printLine("无法识别的指令")
                printHelp()
            }
        }
        return true
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun findLabelsAndShow(commandLine: CommandLine, workspace: WorkspaceContext) {
        val label = commandLine.getOptionValue("f") ?: return
        val items = workspace.itemDao().findItemsWithLabel(label)
        workspace.showItemsAsTreeMode(items)
    }

    private fun addLabel(commandLine: CommandLine, workspace: WorkspaceContext) {
        val label = commandLine.getOptionValue("a") ?: return
        if (label.isEmpty()) {
            return
        }

        val ids = commandLine.getOptionValue("i").parseIds()

        workspace.itemDao().addLabel(label, ids.operators)
    }

    private fun hideWithLabels(commandLine: CommandLine, workspace: WorkspaceContext) {
        val labels = commandLine.getOptionValue("hide").split(",").map { it.trim() }
        workspace.itemDao().markHideWithLabels(labels)
        app.show()
    }

    private fun unhideWithLabels(commandLine: CommandLine, workspace: WorkspaceContext) {
        val labels = commandLine.getOptionValue("unhide").split(",").map { it.trim() }
        workspace.itemDao().cancelHideWithLabels(labels)
        app.show()
    }

    /**
     * 添加自动标签规则
     */
    private fun addAutoLabelRule(commandLine: CommandLine, workspace: WorkspaceContext) {
        val label = commandLine.getOptionValue("auto-add") ?: return
        val contains = commandLine.getOptionValue("if-contains") ?: return
        
        if (label.isEmpty() || contains.isEmpty()) {
            printLine("标签名称和包含内容不能为空")
            return
        }
        
        // 获取现有规则
        val rules = getAutoLabelRules(workspace)
        
        // 添加新规则
        rules.add(AutoLabelRule(label, contains))
        
        // 保存规则
        saveAutoLabelRules(workspace, rules)

        printLine("自动标签规则添加成功：当内容包含 '$contains' 时，自动添加标签 '$label'")
    }

    /**
     * 获取所有自动标签规则
     */
    fun getAutoLabelRules(workspace: WorkspaceContext): MutableList<AutoLabelRule> {
        val json = workspace.database().query(AUTO_LABEL_RULES_KEY) ?: "[]"
        return gson.fromJson(json, Array<AutoLabelRule>::class.java).toMutableList()
    }

    /**
     * 保存自动标签规则
     */
    private fun saveAutoLabelRules(workspace: WorkspaceContext, rules: List<AutoLabelRule>) {
        val json = gson.toJson(rules)
        workspace.database().save(AUTO_LABEL_RULES_KEY, json)
    }

}
