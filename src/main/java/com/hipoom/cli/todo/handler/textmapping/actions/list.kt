package com.hipoom.cli.todo.handler.textmapping.actions

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.todo.handler.textmapping.persistent.TextMappingStorage

fun handleListMappings(app: CliApp) {
    val configs = TextMappingStorage.loadAll(app)
    val mappings = configs.mappings
    
    if (mappings.isNullOrEmpty()) {
        println("No text mappings found.")
        println("Use 'text_mapping --add \"original\" \"replacement\"' to add a new mapping.")
        return
    }

    println("Text Mappings:")
    println("-".repeat(40))
    mappings.forEach { pair ->
        println("${pair.original} -> ${pair.replacement}")
    }
    println("-".repeat(40))
    println("Total: ${mappings.size} mapping(s)")
}
