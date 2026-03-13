package com.hipoom.cli.todo.handler.style.actions

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.todo.handler.style.persistent.StyleStorage

fun listStyles(app: CliApp) {
    val configs = StyleStorage.loadAll(app)
    val styles = configs.name2Style ?: listOf()

    styles.forEach { stylePair ->
        val name = stylePair.name ?: return@forEach
        val displayName = if (configs.currentStyleName == name) {
            "✓ $name"
        } else {
            name
        }
        stylePair.style?.showDemo(displayName)
    }
}
