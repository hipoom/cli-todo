package com.hipoom.cli.todo.handler.delete

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.entity.item.Item
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
class DeleteHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = deleteOptions

    override val supportPrefixes: List<String> = listOf("delete")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Delete Item"

    override fun onHandle(originParams: String, commandLine: CommandLine, app: CliApp, workspace: WorkspaceContext): Boolean {
        when {
            commandLine.hasOption("i") -> delete(workspace, commandLine)
            commandLine.hasOption("h") -> printHelp()
            else -> {
                printLine("无法识别的指令")
            }
        }
        return true
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun delete(workspace: WorkspaceContext, commandLine: CommandLine) {
        val needDeletePhysical = commandLine.hasOption("p")
        val ids = commandLine.getOptionValue("i").parseIds().operators

        // 物理删除
        if (needDeletePhysical) {
            val isAllDeleted = workspace.itemDao().isAllDeleted(ids)
            if (!isAllDeleted) {
                printLine("存在部分事项没有被标记为删除，无法物理删除.")
                return
            }

            workspace.itemDao().deletePhysical(ids)
        }
        // 逻辑删除
        else {
            workspace.itemDao().updateStatus(Item.STATUS_DELETED, ids)
        }

    }

}