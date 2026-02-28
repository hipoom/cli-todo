package com.hipoom.cli.todo

import com.google.gson.Gson
import com.google.gson.GsonBuilder

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