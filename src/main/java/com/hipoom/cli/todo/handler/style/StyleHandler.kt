package com.hipoom.cli.todo.handler.style

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.handler.style.actions.handleCreateStyle
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.storeCurrentConfigs
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine

import com.hipoom.cli.todo.handler.style.actions.listStyles
import com.hipoom.cli.todo.handler.style.actions.chooseStyle

class StyleHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options = styleOptions

    override val supportPrefixes: List<String> = listOf("style")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Color style settings"

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

        if (commandLine.hasOption("list")) {
            listStyles(app)
            return true
        }
        
        if (commandLine.hasOption("choose")) {
            chooseStyle(app, commandLine)
            return true
        }

        if (commandLine.hasOption("create")) {
            handleCreateStyle(app)
            return true
        }
        
        return true
    }

}
