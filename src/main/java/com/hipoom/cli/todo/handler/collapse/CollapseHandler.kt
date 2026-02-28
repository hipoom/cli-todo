package com.hipoom.cli.todo.handler.collapse

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.entity.item.Item
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
class CollapseHandler: ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = collapseOptions

    override val supportPrefixes: List<String> = listOf("collapse")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description(): String {
        return "Collapse"
    }

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            commandLine.hasOption("i") -> { onCollapse(workspace, commandLine); app.show(); }
            commandLine.hasOption("a") -> { onCollapseAll(workspace); app.show(); }
            commandLine.hasOption("h") -> printHelp()
            else -> {
                // 默认就是 collapse -a
                onCollapseAll(workspace)
                app.show()
            }
        }
        return true
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun onCollapse(workspace: WorkspaceContext, commandLine: CommandLine) {
        val (ids, _) = commandLine.getOptionValue("i")?.parseIds() ?: Ids(emptyList(), null)
        workspace.itemDao().updateCollapseStatus(Item.COLLAPSE_STATUS_COLLAPSE, ids)
    }

    private fun onCollapseAll(workspace: WorkspaceContext) {
        // 如果当前有 focusId, 那么只折叠 focusId 的所有子事项
        val fId = workspace.getFocusId()?.toIntOrNull()
        if (fId != null) {
            // 判断是不是所有子节点都折叠起来了，如果不是，先折叠所有的子节点
            workspace.itemDao().smartCollapse(fId)
        }
        // 如果没有 focusId, 那么折叠所有根节点
        else {
            workspace.itemDao().updateAllCollapseStatus(Item.COLLAPSE_STATUS_COLLAPSE)
        }
    }

}
