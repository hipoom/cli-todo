package com.hipoom.cli.todo.handler.developer

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.handler.need_show_expand_cmds
import com.hipoom.cli.todo.handler.need_show_last_modified_item
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.utils.displayWidth
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/8/4 20:29
 *
 */
class DevelopHandler: ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = developerOptions

    override val supportPrefixes: List<String> = listOf("develop")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description(): String {
        return "Develop Utils."
    }

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            commandLine.hasOption("h") -> printHelp()
            commandLine.hasOption("ccw") -> checkCharWidth(commandLine, workspace)
            commandLine.hasOption("slmi") -> showLastModifiedItem(commandLine, workspace)
            commandLine.hasOption("sec") -> showExpandCmds(commandLine, workspace)
            else -> {
                printLine("无法识别的指令")
            }
        }
        return true
    }

    private fun showExpandCmds(commandLine: CommandLine, workspace: WorkspaceContext) {
        need_show_expand_cmds = true
    }

    private fun showLastModifiedItem(commandLine: CommandLine, workspace: WorkspaceContext) {
        val isOpen = commandLine.getOptionValue("slmi").toBoolean()
        need_show_last_modified_item = isOpen
    }


    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun checkCharWidth(commandLine: CommandLine, workspace: WorkspaceContext) {
        val str = commandLine.getOptionValue("ccw")
        printLine("str: $str, width = ${str.displayWidth()}")
    }

}