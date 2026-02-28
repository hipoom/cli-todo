package com.hipoom.cli.todo.handler.mark

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.entity.item.Item
import com.hipoom.cli.todo.handler.show.ShowHandler
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
class MarkHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = markOptions

    override val supportPrefixes: List<String> = listOf("mark")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Mark Item Status"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        if (commandLine.hasOption("h")) {
            printHelp()
            return true
        }

        val ids = if (commandLine.hasOption("i") ) commandLine.getOptionValue("i") else null
        val parsedIds = ids?.parseIds()
        val idInts = parsedIds?.operators
        if (idInts.isNullOrEmpty()) {
            printLine("需要通过 -i 指定有效的 id.")
            return true
        }

        val status = when {
            commandLine.hasOption("n") -> Item.STATUS_NEW
            commandLine.hasOption("doing") -> Item.STATUS_DOING
            commandLine.hasOption("d") -> Item.STATUS_DONE
            commandLine.hasOption("del") -> Item.STATUS_DELETED
            else -> null
        }

        if (status == null) {
            printLine("需要指定新的状态")
            return true
        }

        workspace.itemDao().updateStatus(status, idInts)
        ShowHandler().onHandle("show", app, workspace)

        return true
    }

}