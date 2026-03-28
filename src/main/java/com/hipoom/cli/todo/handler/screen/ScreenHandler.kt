package com.hipoom.cli.todo.handler.screen

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.cleanScreen
import com.hipoom.cli.todo.moveCursorToStart
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.queryConfig
import com.hipoom.cli.todo.saveConfig
import com.hipoom.cli.workspace.WorkspaceContext
import com.hipoom.files.child
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options


class ScreenHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = pathOptions

    override val supportPrefixes: List<String> = listOf("screen")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Screen"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            commandLine.hasOption("clean") -> {
                cleanScreen()
                return true
            }

            commandLine.hasOption("move-cursor-to-start") -> {
                moveCursorToStart()
                return true
            }
            commandLine.hasOption("h") -> printHelp()
        }

        return true
    }

}