package com.hipoom.cli.todo.handler.style

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.storeCurrentConfigs
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine

class StyleHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options = styleOptions

    override val supportPrefixes: List<String> = listOf("style")

    data class ColorStyle(
        val name: String,
        val desc: String,
        val commentTextColor: String,
        val commentBackgroundColor: String,
        val pinBackgroundColor: String
    )

    private val colorStyles = listOf(
        ColorStyle(
            name = "default",
            desc = "默认方案，适合普通终端使用",
            commentTextColor = "128,128,128",
            commentBackgroundColor = "None",
            pinBackgroundColor = "200,200,200"
        ),
        ColorStyle(
            name = "dark",
            desc = "暗色模式，适合深色背景终端",
            commentTextColor = "180,180,180",
            commentBackgroundColor = "None",
            pinBackgroundColor = "80,80,80"
        ),
        ColorStyle(
            name = "light",
            desc = "亮色模式，适合浅色背景终端",
            commentTextColor = "80,80,80",
            commentBackgroundColor = "None",
            pinBackgroundColor = "220,220,220"
        ),
        ColorStyle(
            name = "colorful",
            desc = "彩色模式，适合支持丰富颜色的终端",
            commentTextColor = "0,180,180",
            commentBackgroundColor = "30,30,60",
            pinBackgroundColor = "255,200,100"
        )
    )

    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Color style settings"

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

        if (commandLine.hasOption("s")) {
            val styleName = commandLine.getOptionValue("s")
            setStyle(styleName, workspace)
        } else {
            showStyles()
        }

        return true
    }

    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun showStyles() {
        printLine("可用的配色方案:")
        printLine()
        colorStyles.forEach { style ->
            printLine("  ${style.name} - ${style.desc}")
        }
        printLine()
        printLine("使用 'todo style -s <方案名>' 切换配色方案")
    }

    private fun setStyle(styleName: String, workspace: WorkspaceContext) {
        val style = colorStyles.find { it.name == styleName }
        if (style == null) {
            printLine("未知的配色方案: $styleName")
            printLine()
            showStyles()
            return
        }

        Configs.show.commentStyle.setTextColor(style.commentTextColor)
        Configs.show.commentStyle.setBackgroundColor(style.commentBackgroundColor)
        Configs.show.setPinBackgroundColor(style.pinBackgroundColor)
        
        workspace.storeCurrentConfigs()
        printLine("已切换到配色方案: ${style.name}")
    }
}
