package com.hipoom.cli.todo.handler

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.entity.item.addComment
import com.hipoom.cli.todo.readLineWithPrompt
import com.hipoom.cli.todo.handler.show.ShowOneItemDetail
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/2/3 22:50
 *
 */
class CommentHandler: ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = Options()
        .addOption(
            Option.builder("i")
                .longOpt("id")
                .hasArg(true)
                .desc("Add comment to the specified item")
                .build()
        )

    override val supportPrefixes: List<String> = listOf("comment")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Comment"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            commandLine.hasOption("i") -> addComment(commandLine, workspace)
        }
        return true
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun addComment(commandLine: CommandLine, workspace: WorkspaceContext) {
        val id = commandLine.getOptionValue("i")?.toIntOrNull()
        if (id == null) {
            printLine("解析不到你要给哪个事项添加备注呢 (｡ŏ_ŏ)")
            return
        }

        workspace.itemDao().useItem(
            id = id,
            ifNotFound = {
                printLine("没有找到 id = $id 的条目呢~")
            },
            onFound = {
                val comment = readLineWithPrompt("请输入要添加的备注") ?: return@useItem
                it.addComment(comment)
                printLine("备注添加完成啦 (ﾉ>ω<)ﾉ")
                printLine("")
                ShowOneItemDetail.show(it)
            }
        )
    }
}
