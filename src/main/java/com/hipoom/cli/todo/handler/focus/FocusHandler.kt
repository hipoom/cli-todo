package com.hipoom.cli.todo.handler.focus

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.entity.item.last_modify_item_id
import com.hipoom.cli.todo.getFocusId
import com.hipoom.cli.todo.handler.show.show
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.setFocusId
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options


class FocusHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = focusOptions

    override val supportPrefixes: List<String> = listOf("focus")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description(): String {
        return "Focus"
    }

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        val old_focus_id = workspace.getFocusId()
        when {
            commandLine.hasOption("c") -> { clearFocus(workspace); app.show() }
            commandLine.hasOption("p") -> { focusParent(workspace); app.show() }
            commandLine.hasOption("i") -> { setFocusId(commandLine, workspace); app.show() }
            commandLine.hasOption("d") -> { focusNext(workspace); app.show() }
            commandLine.hasOption("u") -> { focusPrevious(workspace); app.show() }
            commandLine.hasOption("h") -> printHelp()
            else -> {
                printLine("无法识别的指令")
                printHelp()
            }
        }

        // 更新最后一次改动的事项 id
        val now_focus_id = workspace.getFocusId()
        if (old_focus_id != now_focus_id && now_focus_id != null) {
            last_modify_item_id = now_focus_id.toIntOrNull()
        }

        return true
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun clearFocus(workspace: WorkspaceContext): Boolean {
        workspace.setFocusId(null)
        return true
    }

    private fun setFocusId(commandLine: CommandLine, workspace: WorkspaceContext) {
        val id = commandLine.getOptionValue("i")?.toIntOrNull()
        if (id == null) {
            printLine("请输入正确的 id.")
            return
        }

        workspace.itemDao().useItem(
            id = id,
            ifNotFound = {
                printLine("无法找到 id = $id 对应的事项。\n")
            },
            onFound = {
                workspace.setFocusId(id.toString())
            }
        )
    }

    private fun focusParent(workspace: WorkspaceContext) {
        val fId = workspace.getFocusId()
        if (fId == null) {
            printLine("当前没有正在聚焦的事项.")
            return
        }

        workspace.itemDao().useItem(
            id = fId.toInt(),
            onFound = {
                val parentId = it.getFirstParentIdOrNull()
                if (parentId == null || parentId == 0) {
                    printLine("当前正在聚焦的事项，没有父级事项了.")
                    clearFocus(workspace)
                }
                else {
                    workspace.setFocusId(parentId.toString())
                }
            }
        )

        return
    }

    private fun focusNext(workspace: WorkspaceContext) {
        val currentFocusId = workspace.getFocusId()?.toIntOrNull() ?: return

        val next = workspace.itemDao().next(currentFocusId)
        if (next == null) {
            printLine("往上没有平级事项了~")
            return
        }

        workspace.setFocusId(next.id.toString())
    }

    private fun focusPrevious(workspace: WorkspaceContext) {
        val currentFocusId = workspace.getFocusId()?.toIntOrNull() ?: return

        val prev = workspace.itemDao().prev(currentFocusId)
        if (prev == null) {
            printLine("往下没有平级事项了~")
            return
        }

        workspace.setFocusId(prev.id.toString())
    }

}
