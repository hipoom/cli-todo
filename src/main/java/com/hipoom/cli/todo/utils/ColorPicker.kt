package com.hipoom.cli.todo.utils

import com.hipoom.cli.core.ui.TextStyle
import com.hipoom.cli.core.ui.palette.Color
import com.hipoom.cli.core.ui.palette.Colors
import com.hipoom.cli.scaffold.utils.readString
import com.hipoom.cli.todo.defaultTextBlockPrinter
import com.hipoom.cli.todo.printLine
import org.jline.terminal.Terminal

/**
 * @author ZhengHaiPeng
 * @since 2026/3/2 23:33
 *
 */
object ColorPicker {

    fun show256Colors() {
        printLine("+=========================================================================+")
        printLine("| Standard Code :                                                         |")
        printLine("| ", newLine = false)
        for (code in 0..7) {
            val color = Colors.Bits8.createBackground(code)
            val textColor = foreColor(code)
            defaultTextBlockPrinter.print(
                indent = 0,
                maxWidth = 0,
                text = formatCode(code),
                style = TextStyle(
                    color = textColor,
                    backgroundColor = color
                )
            )
            printLine(" ", newLine = false)
        }
        printLine("                                        |")
        printLine("+-------------------------------------------------------------------------+")
        printLine("| High Contrast Colors:                                                   |")
        printLine("| ", newLine = false)
        for (code in 8..15) {
            val color = Colors.Bits8.createBackground(code)
            defaultTextBlockPrinter.print(
                indent = 0,
                maxWidth = 0,
                text = formatCode(code),
                style = TextStyle(
                    color = foreColor(code),
                    backgroundColor = color
                )
            )
            printLine(" ", newLine = false)
        }
        printLine("                                        |")
        printLine("+-------------------------------------------------------------------------+")

        printLine("| 216 Colors:                                                             |")
        var newline = 0
        printLine("| ", newLine = false)
        for (code in 16..231) {
            val color = Colors.Bits8.createBackground(code)
            defaultTextBlockPrinter.print(
                indent = 0,
                maxWidth = 0,
                text = formatCode(code),
                style = TextStyle(
                    color = foreColor(code),
                    backgroundColor = color
                )
            )
            newline++
            if ((newline - 18) % 18 == 0) {
                printLine(" ", newLine = false)
                printLine("| ", newLine = true)
                if (code != 231) {
                    printLine("| ", newLine = false)
                }
            } else {
                printLine(" ", newLine = false)
            }

        }
        printLine("+-------------------------------------------------------------------------+")

        printLine("| Gray Colors:                                                            |")
        printLine("| ", newLine = false)
        for (code in 232..243) {
            val color = Colors.Bits8.createBackground(code)
            defaultTextBlockPrinter.print(
                indent = 0,
                maxWidth = 0,
                text = formatCode(code),
                style = TextStyle(
                    color = Colors.Basic.Foreground.WHITE,
                    backgroundColor = color
                )
            )
            printLine(" ", newLine = false)
        }
        printLine("                        |", newLine = true)
        printLine("| ", newLine = false)
        for (code in 244..255) {
            val color = Colors.Bits8.createBackground(code)
            defaultTextBlockPrinter.print(
                indent = 0,
                maxWidth = 0,
                text = formatCode(code),
                style = TextStyle(
                    color = Colors.Basic.Foreground.BLACK,
                    backgroundColor = color
                )
            )
            printLine(" ", newLine = false)
        }
        printLine("                        |", newLine = true)
        printLine("+=========================================================================+")

        var inputCode: Int? = null
        while (inputCode == null) {
            val input = readString("请输入您要选择的颜色代码")
            inputCode = input?.toIntOrNull()
        }

        printLine("您选择的颜色是: ", newLine = false)
        defaultTextBlockPrinter.print(
            indent = 0,
            maxWidth = 0,
            text = "        ",
            style = TextStyle(
                color = Colors.Basic.Foreground.WHITE,
                backgroundColor = Colors.Bits8.createBackground(inputCode)
            )
        )

    }

    private fun formatCode(code: Int): String {
        if (code <= 9) {
            return " $code "
        }

        if (code in 10..99) {
            return " $code"
        }

        return "$code"
    }

    private fun foreColor(code: Int): Color {
        val light = listOf(
            0, 1, 2, 3, 4, 5, 6,
            8, 9,
            16, 17, 18, 19, 20, 21,
            52, 53, 54, 55, 56, 57,
            58, 59,
            88, 89, 90, 91,
            124, 125, 126,
            160, 196
        )

        return if (light.contains(code)) Colors.Basic.Foreground.WHITE else Colors.Basic.Foreground.BLACK
    }


}