package com.hipoom.cli.todo.handler.time

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.getFocusId
import com.hipoom.cli.todo.handler.show.showItemsAsTreeMode
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/2/2 17:22
 *
 */
class TimeHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = timeOptions

    override val supportPrefixes: List<String> = listOf("time")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Show Order By Deadline"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        val fid = workspace.getFocusId()?.toIntOrNull()
        if (fid != null) {
            val root = workspace.itemDao().findItemAndHisChildrenRecursively(listOf(fid))
            root[0].children?.sortedBy {
                    it.deadline ?: 0
                }
                .also {
                    workspace.showItemsAsTreeMode(it)
                }
        } else {
            workspace.itemDao().loadAllItems().sortedBy { it.deadline ?: 0 }.also {
                workspace.showItemsAsTreeMode(it)
            }
        }
        return true
    }

}