package com.hipoom.cli.todo

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hipoom.cli.core.ui.TextBlockPrinter
import com.hipoom.cli.core.ui.TextStyle

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