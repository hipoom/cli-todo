package com.hipoom.cli.todo.handler.sort

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.handler.show.show
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.utils.parseIds
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options

class SortHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = sortOptions

    override val supportPrefixes: List<String> = listOf("sort")

    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "调整子事项的显示顺序"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            commandLine.hasOption("h") -> printHelp()
            commandLine.hasOption("i") && commandLine.hasOption("u") -> 
                moveUp(workspace, app, commandLine)
            commandLine.hasOption("i") && commandLine.hasOption("d") -> 
                moveDown(workspace, app, commandLine)
            commandLine.hasOption("i") && commandLine.hasOption("t") -> 
                moveToTop(workspace, app, commandLine)
            commandLine.hasOption("i") && commandLine.hasOption("b") -> 
                moveToBottom(workspace, app, commandLine)
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

    private fun moveUp(workspace: WorkspaceContext, app: CliApp, commandLine: CommandLine) {
        val (ids, _) = commandLine.getOptionValue("i").parseIds()
        ids.forEach { id ->
            workspace.itemDao().sortMoveUp(id)
        }
        app.show()
    }

    private fun moveDown(workspace: WorkspaceContext, app: CliApp, commandLine: CommandLine) {
        val (ids, _) = commandLine.getOptionValue("i").parseIds()
        ids.forEach { id ->
            workspace.itemDao().sortMoveDown(id)
        }
        app.show()
    }

    private fun moveToTop(workspace: WorkspaceContext, app: CliApp, commandLine: CommandLine) {
        val (ids, _) = commandLine.getOptionValue("i").parseIds()
        ids.forEach { id ->
            workspace.itemDao().sortMoveToTop(id)
        }
        app.show()
    }

    private fun moveToBottom(workspace: WorkspaceContext, app: CliApp, commandLine: CommandLine) {
        val (ids, _) = commandLine.getOptionValue("i").parseIds()
        ids.forEach { id ->
            workspace.itemDao().sortMoveToBottom(id)
        }
        app.show()
    }
}
