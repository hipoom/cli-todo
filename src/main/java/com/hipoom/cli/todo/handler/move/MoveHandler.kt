package com.hipoom.cli.todo.handler.move

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.entity.item.Item.Companion.STATUS_DONE
import com.hipoom.cli.todo.handler.show.ShowHandler
import com.hipoom.cli.todo.handler.show.show
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.utils.parseIds
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/2/2 17:22
 *
 */
class MoveHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = moveOptions

    override val supportPrefixes: List<String> = listOf("move")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Move Item"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            // Help
            commandLine.hasOption("h") -> printHelp()
            commandLine.hasOption("ui") -> moveAllUnFinishedItems(workspace, app, commandLine)
            // Move to below the specified node
            commandLine.hasOption("i") && commandLine.hasOption("p") -> moveToTarget(workspace, app, commandLine)
            // Move up one level, be on the same level as the parent node.
            commandLine.hasOption("i") && commandLine.hasOption("u") -> moveUp(workspace, app, commandLine)
            // Move as root node
            commandLine.hasOption("i") && !commandLine.hasOption("p") -> moveToRoot(workspace, app, commandLine)
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

    private fun moveToTarget(workspace: WorkspaceContext, app: CliApp, commandLine: CommandLine) {
        val (ids, _) = commandLine.getOptionValue("i").parseIds()
        val pid = commandLine.getOptionValue("p").toIntOrNull()
        val pids = if (pid == null) emptyList() else listOf(pid)
        workspace.itemDao().move(pids, ids)
        printLine("")
        ShowHandler().onHandle("show", app, workspace)
    }

    private fun moveUp(workspace: WorkspaceContext, app: CliApp, commandLine: CommandLine) {
        val (ids, _) = commandLine.getOptionValue("i").parseIds()
        workspace.itemDao().moveUp(ids)
        app.show()
    }

    private fun moveToRoot(workspace: WorkspaceContext, app: CliApp, commandLine: CommandLine) {
        val (ids, _) = commandLine.getOptionValue("i").parseIds()
        workspace.itemDao().move(emptyList(), ids)
        printLine()
        app.show()
    }

    /**
     * 移动 --id 指定的父节点下的所有未完成的事项，到 --parent 指定的新父节点下方。
     */
    private fun moveAllUnFinishedItems(workspace: WorkspaceContext, app: CliApp, commandLine: CommandLine) {
        val (ids, _) = commandLine.getOptionValue("i").parseIds()

        // 父节点
        val newParentId = commandLine.getOptionValue("p").toIntOrNull()
        if (newParentId == null) {
            printLine("您需要指定新的父节点，示例： move --unfinished-items -i <要清理的节点> -p <新的父节点>")
            return
        }

        ids.forEach { id ->
            workspace.itemDao().useChildren(id = id) { children ->
                children.forEach { child ->
                    // 如果不是已完成，则修改它的父节点
                    if (child.status != STATUS_DONE) {
                        child.replaceParentId(id, newParentId)
                    }
                }
            }
        }

        app.show()
    }

}