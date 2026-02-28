package com.hipoom.cli.todo.handler.pin

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.entity.item.Item
import com.hipoom.cli.todo.handler.show.showPins
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.utils.parseIds
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options


class PinHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = pinOptions

    override val supportPrefixes: List<String> = listOf("pin")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Pin"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            commandLine.hasOption("i") -> {
                addPins(commandLine, workspace)
                return true
            }
            commandLine.hasOption("s") -> {
                showPins(commandLine, workspace)
                return true
            }
            commandLine.hasOption("u") -> {
                unpin(commandLine, workspace)
                return true
            }
            commandLine.hasOption("h") -> printHelp()
            else -> printLine("无法处理的命令.")
        }

        return true
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun addPins(commandLine: CommandLine, workspace: WorkspaceContext) {
        val ids = commandLine.getOptionValue("i").parseIds().operators
        val oldPins = workspace.database().query("pins").parseIds().operators
        val newPins = ids + oldPins
        val newPinString = newPins.joinToString { it.toString() }
        workspace.database().save("pins", newPinString)
    }

    private fun showPins(commandLine: CommandLine, workspace: WorkspaceContext) {
        val items = workspace
            .itemDao()
            .loadAllItems()
            .filter { it.status != Item.STATUS_DELETED}

        workspace.showPins(items)
    }

    private fun unpin(commandLine: CommandLine, workspace: WorkspaceContext) {
        val ids = commandLine.getOptionValue("u").parseIds().operators
        val oldPins = workspace.database().query("pins").parseIds().operators
        val newPins = oldPins - ids.toSet()
        val newPinString = newPins.joinToString { it.toString() }
        workspace.database().save("pins", newPinString)
    }

}
