package com.hipoom.cli.todo.handler.textmapping.actions

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.todo.handler.textmapping.persistent.TextMappingStorage

fun handleAddMapping(app: CliApp, original: String, replacement: String) {
    TextMappingStorage.addOrReplace(app, original, replacement)
    println("Text mapping added: \"$original\" -> \"$replacement\"")
}
