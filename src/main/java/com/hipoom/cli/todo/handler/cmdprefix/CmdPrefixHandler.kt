package com.hipoom.cli.todo.handler.cmdprefix

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.scaffold.utils.removeEmptyStrings
import com.hipoom.cli.scaffold.utils.removePrefixes
import com.hipoom.cli.todo.currentCmdPrefix
import com.hipoom.cli.todo.printError
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.DefaultParser

class CmdPrefixHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options = cmdPrefixOptions

    override val supportPrefixes: List<String> = listOf("cmd-prefix")


    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "管理命令前缀"

    override fun onHandle(
        originParams: String,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        // 如果是 --help 指令
        if (originParams.contains("cmd-prefix -h") || originParams.contains("cmd-prefix --help")) {
            printHelp()
            return true
        }

        // 如果是 --clear 指令
        if (originParams.contains("cmd-prefix -c") || originParams.contains("cmd-prefix --clear")) {
            currentCmdPrefix = null
            return true
        }

        // 如果是 --set 指令
        if (originParams.contains("cmd-prefix -s") || originParams.contains("cmd-prefix --set")) {
            val prefix = originParams.removePrefixes(listOf("cmd-prefix -s", "cmd-prefix --set")).trim()
            if (prefix.isBlank()) {
                printLine("错误: 请指定要设置的前缀")
                return true
            }
            currentCmdPrefix = prefix
            return true
        }

        printError("无法解析的指令.")
        return true
    }

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        throw IllegalStateException("执行不到这里。")
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun extractPrefixFromOriginParams(originParams: String): String? {
        val setPatterns = listOf("--set ", "-s ")
        for (pattern in setPatterns) {
            val index = originParams.indexOf(pattern)
            if (index != -1) {
                val remaining = originParams.substring(index + pattern.length).trim()
                if (remaining.isNotEmpty()) {
                    return remaining
                }
            }
        }
        return null
    }

}
