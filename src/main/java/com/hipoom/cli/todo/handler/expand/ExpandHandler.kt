package com.hipoom.cli.todo.handler.expand

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.getFocusId
import com.hipoom.cli.todo.handler.show.show
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.utils.Ids
import com.hipoom.cli.todo.utils.parseIds
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2/20/25 AM10:29
 *
 */
class ExpandHandler: ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = expandOptions

    override val supportPrefixes: List<String> = listOf("expand")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description(): String {
        return "Expand"
    }

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            commandLine.hasOption("i") -> { onExpand(workspace, commandLine); app.show(); }
            commandLine.hasOption("a") -> { onExpandAll(workspace); app.show(); }
            commandLine.hasOption("h") -> printHelp()
            else -> {
                // 默认就是 expand -a
                onExpandAll(workspace)
                app.show()
                return true
            }
        }
        return true
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun onExpand(workspace: WorkspaceContext, commandLine: CommandLine) {
        val (ids, _) = commandLine.getOptionValue("i")?.parseIds() ?: Ids(emptyList(), null)
        workspace.itemDao().updateCollapseStatus(null, ids)
    }

    private fun onExpandAll(workspace: WorkspaceContext) {
        // 如果当前有 focusId, 那么展开 focusId 的所有子事项
        val fId = workspace.getFocusId()?.toIntOrNull()
        if (fId != null) {
            // 如果 [id] 对应事项是折叠状态，则展开 [id] 自己；
            // 否则，展开 [id] 自己的子节点。
            workspace.itemDao().smartExpand(fId)
        } else {
            workspace.itemDao().updateAllCollapseStatus(null)
        }
    }

}
