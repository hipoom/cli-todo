package com.hipoom.cli.todo.handler.textmapping.actions

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.todo.handler.textmapping.persistent.TextMappingStorage

fun handleDeleteMapping(app: CliApp, original: String) {
    val removed = TextMappingStorage.remove(app, original)
    if (removed) {
        println("Text mapping deleted: \"$original\"")
    } else {
        println("Text mapping not found: \"$original\"")
    }
}
