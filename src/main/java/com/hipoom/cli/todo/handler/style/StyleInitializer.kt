package com.hipoom.cli.todo.handler.style

import com.hipoom.cli.core.ui.CharWidthCalculator
import com.hipoom.cli.core.ui.TextBlockPrinter
import com.hipoom.cli.core.ui.TextStyle
import com.hipoom.cli.todo.Main
import com.hipoom.cli.todo.defaultTextBlockPrinter
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.utils.displayWidth

/**
 * @author ZhengHaiPeng
 * @since 2026/3/1 0:16
 *
 */
object StyleInitializer {

    fun doInit() {
        // 检查是否已经有保存的样式配置
        val currentStyleName = com.hipoom.cli.todo.Configs.show.getCurrentStyle()
        
        if (currentStyleName != null) {
            // 已经有保存的样式，不需要重新选择
            return
        }
        
        // 没有保存的样式，提示用户选择
        printLine("请选择配色方案：")

        val printer = defaultTextBlockPrinter

        printLine("1. 浅色配色方案")
        printer.print(indent = 3, maxWidth = Int.MAX_VALUE, text = "浅色配色适用于浅色的终端/控制台。\n", style = null)
        printer.print(indent = 3, maxWidth = Int.MAX_VALUE, text = "这是一条备注。", style = TextStyle(
            backgroundColor = Styles.light.commentBackgroundColor,
            color = Styles.light.secondaryTextColor,
            bold = false,
            underline = false
        ))

        println()
        printLine("2. 深色配色方案")
        printer.printLine(indent = 3, text = "深色配色适用于深色的终端/控制台。")
        printer.print(indent = 3, maxWidth = Int.MAX_VALUE, text = "这是一条备注。", style = TextStyle(
            backgroundColor = Styles.dark.commentBackgroundColor,
            color = Styles.dark.secondaryTextColor,
            bold = false,
            underline = false
        ))
        println()
    }

}