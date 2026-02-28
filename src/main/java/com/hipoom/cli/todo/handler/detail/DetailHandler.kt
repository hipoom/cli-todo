package com.hipoom.cli.todo.handler.detail

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.handler.show.ShowOneItemDetail
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/2/3 22:50
 *
 */
class DetailHandler: ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = detailOptions

    override val supportPrefixes: List<String> = listOf("detail")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description(): String {
        return "Show Item Details"
    }

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            commandLine.hasOption("h") -> printHelp()
            commandLine.hasOption("i") -> showDetail(commandLine, workspace)
            else -> {
                printLine("无法识别的指令")
            }
        }
        return true
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun showDetail(commandLine: CommandLine, workspace: WorkspaceContext) {
        val id = commandLine.getOptionValue("i")?.toIntOrNull()
        if (id == null) {
            printLine("请输入正确的 id.")
            return
        }

        workspace.itemDao().useItem(
            id = id,
            ifNotFound = {
                printLine("没有找到 id = $id 的条目呢~")
            },
            onFound = {
                ShowOneItemDetail.show(it)
            }
        )
    }
}