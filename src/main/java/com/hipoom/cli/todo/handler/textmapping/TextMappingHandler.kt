package com.hipoom.cli.todo.handler.textmapping

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.handler.textmapping.actions.handleAddMapping
import com.hipoom.cli.todo.handler.textmapping.actions.handleDeleteMapping
import com.hipoom.cli.todo.handler.textmapping.actions.handleListMappings
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine

class TextMappingHandler : ApacheCliOptionHandler() {

    override val options = textMappingOptions

    override val supportPrefixes: List<String> = listOf("text_mapping", "tm")

    override fun description() = "Manage text mappings for automatic text replacement"

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

        if (commandLine.hasOption("l")) {
            handleListMappings(app)
            return true
        }

        if (commandLine.hasOption("add")) {
            val args = commandLine.getOptionValues("add")
            if (args == null || args.size < 2) {
                println("Usage: text_mapping --add \"original\" \"replacement\"")
                return true
            }
            handleAddMapping(app, args[0], args[1])
            return true
        }

        if (commandLine.hasOption("d")) {
            val original = commandLine.getOptionValue("d")
            if (original == null) {
                println("Usage: text_mapping --delete \"original\"")
                return true
            }
            handleDeleteMapping(app, original)
            return true
        }

        printHelp()
        return true
    }
}
