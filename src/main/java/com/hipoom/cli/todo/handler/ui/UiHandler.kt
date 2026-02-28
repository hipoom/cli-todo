package com.hipoom.cli.todo.handler.ui

import com.hipoom.cli.core.ui.CharWidthCalculator
import com.hipoom.cli.core.ui.TextBlockPrinter
import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.utils.displayWidth
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/2/2 17:22
 *
 */
class UiHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = uiOptions

    override val supportPrefixes: List<String> = listOf("ui")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "UI Settings"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        printLine("请选择要使用的颜色模式:")

        val printer = TextBlockPrinter(
            printer = com.hipoom.cli.todo.Main.printer,
            charWidthCalculator = object : CharWidthCalculator {
                override fun calculate(text: String): Int {
                    return text.displayWidth()
                }
            }
        )

        printLine(msg = "1. 亮色模式: ", newLine = false)

        return true
    }

}