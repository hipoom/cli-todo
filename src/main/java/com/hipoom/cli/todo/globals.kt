package com.hipoom.cli.todo

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hipoom.cli.core.ui.CharWidthCalculator
import com.hipoom.cli.core.ui.TextBlockPrinter
import com.hipoom.cli.core.ui.TextStyle
import com.hipoom.cli.core.ui.TextStyleBuilder
import com.hipoom.cli.core.ui.palette.Colors
import com.hipoom.cli.todo.utils.displayWidth
import org.jline.reader.LineReader

val gson: Gson = GsonBuilder()
    // .registerTypeAdapter(Color::class.java, ColorDeserializer())
    .setPrettyPrinting()
    .create()

lateinit var reader: LineReader


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

fun readLine(prompt: String? = null): String? {
    return reader.readLine(prompt)
}

fun TextBlockPrinter.printLine(indent: Int, text: String, maxWidth: Int = Int.MAX_VALUE, style: TextStyle? = null) {
    this.print(indent, maxWidth, text, style)
    printLine()
}

fun TextBlockPrinter.error(text: String, indent: Int = 0, maxWidth: Int = Int.MAX_VALUE) {
    val style = TextStyleBuilder()
        .color(Colors.Basic.Foreground.RED)
        .build()
    this.printLine(indent = indent, text = text, maxWidth = maxWidth, style = style)
}

fun TextBlockPrinter.sucess(text: String, indent: Int = 0, maxWidth: Int = Int.MAX_VALUE) {
    val style = TextStyleBuilder()
        .color(Colors.Basic.Foreground.GREEN)
        .build()
    this.printLine(indent = indent, text = text, maxWidth = maxWidth, style = style)
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