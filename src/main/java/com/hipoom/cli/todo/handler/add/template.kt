package com.hipoom.cli.todo.handler.add

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.utils.readInt
import com.hipoom.cli.todo.entity.item.Item
import com.hipoom.cli.todo.readLineWithPrompt
import com.hipoom.cli.todo.getFocusId
import com.hipoom.cli.todo.handler.show.ShowHandler
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.templates
import com.hipoom.cli.todo.utils.parseIds
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine


fun addWithTemplateMode(app: CliApp, workspace: WorkspaceContext, commandLine: CommandLine) {
    printLine("当前模板列表：")
    workspace.templates().templates.forEach { template ->
        printLine("序号: " + template.id + ", 模板名称: " + template.alias)
    }

    // 获取模板序号
    val id = readInt("请选择要基于哪个模板创建节点，输入对应模板的序号") ?: return
    printLine()

    val template = workspace.templates().templates.find { it.id == id }
    if (template == null) {
        printLine("没有找到 $id 对应的模板.")
        return
    }

    printLine("你选择的模板包含以下子项：")
    template.items.forEachIndexed { index, item ->
        printLine("${index}. " + item)
    }

    val content = readLineWithPrompt("请输入你要添加的事项内容")
    if (content.isNullOrEmpty()) {
        return
    }

    val item = Item()
    item.content = content
    item.status = Item.STATUS_NEW

    // 获取父节点 ids
    val parentIds = workspace.getParentIdOrFocusId(commandLine)
    val ids = workspace.itemDao().insert(item, parentIds)

    // 将模板中的每一项都插入到刚新增的事项中
    ids.forEach { parentId ->
        template.items.forEach { childContent ->
            val child = Item().apply {
                this.content = childContent
                this.parentIds = mutableListOf(parentId)
            }
            workspace.itemDao().insert(child)
        }
    }

    printLine("添加完成~")
    ShowHandler().onHandle("show", app, workspace)
}


/**
 * 根据当前的 focus 情况，以及用户指定的 -p 信息，返回要加入到哪个事项下。
 * 如果明确指定了 -p 参数，则返回用户指定的 -p，
 * 否则，如果当前有 focus，则返回 focus.
 * 否则返回 empty list.
 */
private fun WorkspaceContext.getParentIdOrFocusId(commandLine: CommandLine): List<Int> {
    val parentIds = commandLine.getOptionValue("p")
    if (!parentIds.isNullOrEmpty()) {
        return parentIds.parseIds().operators
    }

    val focus = getFocusId()?.toIntOrNull() ?: return emptyList()
    return listOf(focus)
}
