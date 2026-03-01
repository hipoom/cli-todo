package com.hipoom.cli.todo

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hipoom.cli.core.ui.CharWidthCalculator
import com.hipoom.cli.core.ui.TextBlockPrinter
import com.hipoom.cli.core.ui.TextStyle
import com.hipoom.cli.todo.utils.displayWidth

val gson: Gson = GsonBuilder().setPrettyPrinting().create()


fun printLine(msg: Any? = null, newLine: Boolean = true) {
    if (msg == null) {
        println()
        return
    }

    if (newLine) {
        println(msg)
    } else {
        print(msg)
    }
}

fun TextBlockPrinter.printLine(indent: Int, text: String, maxWidth: Int = Int.MAX_VALUE, style: TextStyle? = null) {
    this.print(indent, maxWidth, text, style)
    printLine()
}

/**
 * 默认的 TextBlock 打印
 */
val defaultTextBlockPrinter = TextBlockPrinter(
    printer = Main.printer,
    charWidthCalculator = object : CharWidthCalculator {
        override fun calculate(text: String): Int {
            return text.displayWidth()
        }
    }
)