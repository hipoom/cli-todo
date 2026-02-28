package com.hipoom.cli.todo.handler.owner

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.handler.show.showItemsAsTreeMode
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options


class OwnerHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = ownerOptions

    override val supportPrefixes: List<String> = listOf("owner")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description(): String {
        return "Owner"
    }

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            commandLine.hasOption("f") -> { findOwnerAndShow(commandLine, workspace) }
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

    private fun findOwnerAndShow(commandLine: CommandLine, workspace: WorkspaceContext) {
        val ownersString = commandLine.getOptionValue("f") ?: return
        val owners = ownersString.split(",").map { it.trim() }
        val items = workspace.itemDao().findItemsWithOwners(owners)
        workspace.showItemsAsTreeMode(items)
    }

}
