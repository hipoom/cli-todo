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

    private var colorStyles = mutableListOf(
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
        } else if (commandLine.hasOption("l")) {
            listStyles()
        } else if (commandLine.hasOption("a")) {
            val styleInfo = commandLine.getOptionValue("a")
            addStyle(styleInfo)
        } else if (commandLine.hasOption("d")) {
            val styleName = commandLine.getOptionValue("d")
            deleteStyle(styleName)
        } else if (commandLine.hasOption("show")) {
            val styleName = commandLine.getOptionValue("show")
            showStyleDetails(styleName)
        } else if (commandLine.hasOption("e")) {
            val styleInfo = commandLine.getOptionValue("e")
            editStyle(styleInfo)
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
        
        // 保存当前选择的样式名称
        Configs.show.setCurrentStyle(styleName)
        
        workspace.storeCurrentConfigs()
        printLine("已切换到配色方案: ${style.name}")
    }

    private fun listStyles() {
        printLine("所有可用的配色方案:")
        printLine()
        colorStyles.forEach { style ->
            printLine("  ${style.name} - ${style.desc}")
            printLine("    文本颜色: ${style.commentTextColor}")
            printLine("    背景颜色: ${style.commentBackgroundColor}")
            printLine("    置顶背景: ${style.pinBackgroundColor}")
            printLine()
        }
    }

    private fun addStyle(styleInfo: String) {
        val parts = styleInfo.split(",")
        if (parts.size != 5) {
            printLine("格式错误，请使用: name,desc,textColor,bgColor,pinColor")
            return
        }

        val (name, desc, textColor, bgColor, pinColor) = parts
        if (colorStyles.any { it.name == name }) {
            printLine("已存在同名配色方案: $name")
            return
        }

        val newStyle = ColorStyle(
            name = name,
            desc = desc,
            commentTextColor = textColor,
            commentBackgroundColor = bgColor,
            pinBackgroundColor = pinColor
        )

        colorStyles.add(newStyle)
        printLine("已添加新配色方案: $name")
    }

    private fun deleteStyle(styleName: String) {
        if (styleName == "default") {
            printLine("默认配色方案不可删除")
            return
        }

        val removed = colorStyles.removeIf { it.name == styleName }
        if (removed) {
            printLine("已删除配色方案: $styleName")
        } else {
            printLine("未找到配色方案: $styleName")
        }
    }

    private fun showStyleDetails(styleName: String) {
        val style = colorStyles.find { it.name == styleName }
        if (style == null) {
            printLine("未找到配色方案: $styleName")
            return
        }

        printLine("配色方案详情:")
        printLine()
        printLine("  名称: ${style.name}")
        printLine("  描述: ${style.desc}")
        printLine("  文本颜色: ${style.commentTextColor}")
        printLine("  背景颜色: ${style.commentBackgroundColor}")
        printLine("  置顶背景: ${style.pinBackgroundColor}")
    }

    private fun editStyle(styleInfo: String) {
        val parts = styleInfo.split(",")
        if (parts.size != 5) {
            printLine("格式错误，请使用: name,desc,textColor,bgColor,pinColor")
            return
        }

        val (name, desc, textColor, bgColor, pinColor) = parts
        val styleIndex = colorStyles.indexOfFirst { it.name == name }
        if (styleIndex == -1) {
            printLine("未找到配色方案: $name")
            return
        }

        val updatedStyle = ColorStyle(
            name = name,
            desc = desc,
            commentTextColor = textColor,
            commentBackgroundColor = bgColor,
            pinBackgroundColor = pinColor
        )

        colorStyles[styleIndex] = updatedStyle
        printLine("已更新配色方案: $name")
    }
}
