package com.hipoom.cli.todo.handler.config

import com.hipoom.cli.core.ui.TextEditor
import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.config.ConfigGroup
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.currentConfigs
import com.hipoom.cli.todo.gson
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.storeCurrentConfigs
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 20:57
 *
 */
class ConfigHandler: ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = configOptions

    override val supportPrefixes: List<String> = listOf("config")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description(): String = "Config"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {

        when {
            commandLine.hasOption("e") -> editConfigs(workspace)
            commandLine.hasOption("s") -> showConfigs()
            commandLine.hasOption("h") -> printHelp()
        }

        return true
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun editConfigs(workspace: WorkspaceContext) {
        val input = gson.toJson(currentConfigs).replace("    ", "        ")
        val output = TextEditor.edit(
            input = input,
            prompt = "请谨慎编辑，建议编辑前先备份. 请不要修改任何 name 属性，你应该只需要修改 value 对应的字段。"
        )

        currentConfigs = gson.fromJson(output, ConfigGroup::class.java)
        workspace.storeCurrentConfigs()
    }

    private fun showConfigs() {
        val input = gson.toJson(currentConfigs).replace("    ", "        ")
        printLine(input)
        printLine()
    }

}