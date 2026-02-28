package com.hipoom.cli.todo.handler.window

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.queryConfig
import com.hipoom.cli.todo.saveConfig
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options


class WindowHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = windowOptions

    override val supportPrefixes: List<String> = listOf("window")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Window"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            commandLine.hasOption("enable") -> workspace.enable()
            commandLine.hasOption("disable") -> workspace.disable()
            commandLine.hasOption("h") -> printHelp()
        }

        return true
    }

}

private const val KEY_IS_J_FRAME_WINDOW_ENABLE = "IS_J_FRAME_WINDOW_ENABLE"

private fun WorkspaceContext.disable() {
    saveConfig(KEY_IS_J_FRAME_WINDOW_ENABLE, "false")
    Configs.window.isEnable = false
}

private fun WorkspaceContext.enable() {
    saveConfig(KEY_IS_J_FRAME_WINDOW_ENABLE, "true")
    Configs.window.isEnable = true
}

fun queryWindowEnable(workspace: WorkspaceContext): Boolean {
    return workspace.queryConfig(KEY_IS_J_FRAME_WINDOW_ENABLE, "true").toBoolean()
}