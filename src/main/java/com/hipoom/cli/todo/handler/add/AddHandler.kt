package com.hipoom.cli.todo.handler.add

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.entity.item.Item
import com.hipoom.cli.todo.entity.item.last_modify_item_id
import com.hipoom.cli.todo.getFocusId
import com.hipoom.cli.todo.handler.DeadLineEditor
import com.hipoom.cli.todo.handler.label.LabelHandler
import com.hipoom.cli.todo.handler.show.ShowHandler
import com.hipoom.cli.todo.handler.view.ViewHandler
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.setQuickMode
import com.hipoom.cli.todo.utils.parseIds
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import java.util.Stack

/**
 * @author ZhengHaiPeng
 * @since 2025/2/2 13:56
 *
 */
class AddHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options = addOptions

    override val supportPrefixes: List<String> = listOf("add")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Add Item"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        // Help
        if (commandLine.hasOption("h")) {
            printHelp()
            return true
        }

        // 批量模式
        if (commandLine.hasOption("b")) {
            onBatchAdd(workspace, commandLine)
            return true
        }

        // 切换快速模式
        if (commandLine.hasOption("qm")) {
            val quickMode = commandLine.getOptionValue("qm").toBoolean()
            workspace.setQuickMode(quickMode)
            printLine("快速模式已${if (quickMode) "开启" else "关闭"}")
            return true
        }

        // 以模板模式创建新事项
        if (commandLine.hasOption("t")) {
            addWithTemplateMode(app, workspace, commandLine)
            return true
        }

        // 以高级模式创建新事项
        if (commandLine.hasOption("a")) {
            addWithAdvanceMode(app, workspace)
            return true
        }

        // 内容
        val content = commandLine.args.joinToString(separator = " ") { it }.trim()

        // 父节点们
        val parentIds = getParentIds(workspace, commandLine)

        // 负责人
        val owner = commandLine.getOptionValue("o")

        // 标签
        val labels = commandLine.getOptionValue("l")?.split(",")?.toMutableList() ?: mutableListOf()

        // 应用自动标签规则
        applyAutoLabelRules(content, labels, workspace)

        // 时间戳
        val deadline = commandLine.getOptionValue("d")?.let {
            DeadLineEditor.parseTimestamp(it.trim())
        }

        // 虚拟视图
        val viewName = commandLine.getOptionValue("v")

        val item = Item().apply {
            this.content = content
            this.owner = owner
            this.labels = labels
            this.status = Item.STATUS_NEW
            this.deadline = if (deadline == 0L) null else deadline
        }

        val newIds = workspace.itemDao().insert(
            item = item,
            parentIds = parentIds,
        )

        if (newIds.size == 1) {
            printLine("添加完成 (^_^), id = ${newIds.first()}")
        } else {
            printLine("添加完成 (^_^), ids = [${newIds.joinToString { it.toString() }}]")
        }

        // 如果指定了虚拟视图，将新添加的事项添加到该视图
        if (viewName != null) {
            ViewHandler().addItemToView(workspace, newIds, viewName)
        }

        // 更新最后一次改动的事项 id
        last_modify_item_id = newIds.last()

        printLine()
        ShowHandler().onHandle("show", app, workspace)
        return true
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun getParentIds(workspace: WorkspaceContext, commandLine: CommandLine): List<Int> {
        // 父节点们
        val (tempParentIds, _) = commandLine.getOptionValue("p").parseIds()
        val parentIds = if (tempParentIds.isNotEmpty()) {
            tempParentIds
        }
        // 如果没有指定父节点，但是有 focus 的节点，则添加到 focus 节点下
        else if (workspace.getFocusId() != null) {
            val temp = workspace.getFocusId()?.toIntOrNull()
            if (temp == null) {
                emptyList()
            } else {
                listOf(temp)
            }
        }
        // 既没有指定父节点，也没有 focus 节点，忽略
        else {
            emptyList()
        }
        return parentIds
    }

    /**
     * 应用自动标签规则
     */
    private fun applyAutoLabelRules(content: String, labels: MutableList<String>, workspace: WorkspaceContext) {
        val autoRules = LabelHandler().getAutoLabelRules(workspace)
        val matchedLabels = autoRules
            .filter { content.contains(it.contains, ignoreCase = true) }
            .map { it.label }
            .filter { !labels.contains(it) }
            
        if (matchedLabels.isNotEmpty()) {
            labels.addAll(matchedLabels)
            printLine("自动添加标签: ${matchedLabels.joinToString(", ")}")
        }
    }

    private fun onBatchAdd(workspace: WorkspaceContext, commandLine: CommandLine) {
        // 父节点们
        val parentIds = getParentIds(workspace, commandLine)

        // 负责人
        val owner = commandLine.getOptionValue("o")

        // 标签
        val baseLabels = commandLine.getOptionValue("l")?.split(",")?.toMutableList()

        // 时间戳
        val deadline = commandLine.getOptionValue("d")?.let {
            DeadLineEditor.parseTimestamp(it.trim())
        }

        printLine("批量新增中，每一行都将作为一个事项添加，输入 exit 推出批量模式。")
        printLine("在批量模式中，你可以输入 child 指令，后续输入将作为上一个输入事项的子事项。")
        printLine("同理，你也可以输入 parent 指令。")

        val stack = Stack<List<Int>>()
        stack.push(parentIds)

        // 上一个输入添加成功的 ids
        var preChildIds: List<Int>? = null

        var indent = ""
        while (true) {
            val parents = stack.peek()

            print("批量新增(${parents.joinToString { it.toString() }}) $indent> ")
            val content = readln()
            if (content.trim() == "exit" || content.trim() == "exit()" || content.trim() == "e") {
                break
            }

            if (content.isEmpty()) {
                continue
            }

            if (content == "child" || content == "c") {
                if (preChildIds == null) {
                    printLine("您还没有新增过任何指令，无法使用 child 指令.")
                    continue
                } else {
                    stack.push(preChildIds)
                    indent = "${indent}.   "
                    continue
                }
            }

            if (content == "parent" || content == "p") {
                if (stack.size == 1) {
                    printLine("您还没有执行过 child，或者当前就是最顶层了。")
                    continue
                }
                stack.pop()
                indent = indent.removeSuffix(".   ")
                continue
            }

            // 为每个事项创建独立的标签列表，并应用自动标签规则
            val itemLabels = baseLabels?.toMutableList() ?: mutableListOf()
            applyAutoLabelRules(content, itemLabels, workspace)
            
            // 添加
            val item = Item().apply {
                this.content = content
                this.owner = owner
                this.labels = itemLabels
                this.status = Item.STATUS_NEW
                this.deadline = if (deadline == 0L) null else deadline
            }
            preChildIds = workspace.itemDao().insert(item, parents)
        }
    }
}