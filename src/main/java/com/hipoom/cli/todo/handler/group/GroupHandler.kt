package com.hipoom.cli.todo.handler.group

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.handler.show.show
import com.hipoom.cli.todo.queryConfig
import com.hipoom.cli.todo.saveConfig
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/2/5 19:28
 *
 */
class GroupHandler: ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    companion object {
        const val KEY_GROUP_MODE = "group-by-mode"
        const val GROUP_MODE_PARENT = "parent"
        const val GROUP_MODE_WHO = "who"

        fun isOwnerMode(workspace: WorkspaceContext): Boolean {
            return workspace.queryConfig(KEY_GROUP_MODE, GROUP_MODE_PARENT) == GROUP_MODE_WHO
        }
    }

    override val supportPrefixes: List<String> = listOf("group")

    override val options: Options = groupOptions



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */


    override fun description() = "Group Mode During Show"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            commandLine.hasOption("o") -> { workspace.saveConfig(KEY_GROUP_MODE, GROUP_MODE_WHO); app.show() }
            commandLine.hasOption("t") -> { workspace.saveConfig(KEY_GROUP_MODE, GROUP_MODE_PARENT); app.show() }
            else -> printHelp()
        }
        return true
    }

}