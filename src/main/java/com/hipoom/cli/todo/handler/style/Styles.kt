package com.hipoom.cli.todo.handler.style

import com.hipoom.cli.core.ui.palette.Color
import com.hipoom.cli.core.ui.palette.Colors
import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.app
import com.hipoom.cli.todo.handler.style.persistent.StyleConfigs
import com.hipoom.cli.todo.handler.style.persistent.StyleStorage
import com.hipoom.cli.todo.handler.style.pojo.Style

/**
 * @author ZhengHaiPeng
 * @since 2026/3/1 0:24
 *
 */
object Styles {

    val light = Style(
        pinTextColor = Colors.Bits24.createForeground(0, 0, 0),
        pinBackgroundColor = Colors.Bits24.createBackground(255, 255, 255),
        secondaryTextColor = Colors.Bits24.createForeground(100, 100, 100),
        commentBackgroundColor = Colors.Bits24.createBackground(250, 250, 250),
        hintTextColor = Colors.Bits24.createForeground(200, 200, 200)
    )

    val dark = Style(
        pinTextColor = Colors.Bits24.createForeground(255, 255, 255),
        pinBackgroundColor = Colors.Bits24.createBackground(0, 0, 0),
        secondaryTextColor = Colors.Bits24.createForeground(230, 230, 230),
        commentBackgroundColor = Colors.Bits24.createBackground(50, 50, 50),
        hintTextColor = Colors.Bits24.createForeground(128, 128, 128)
    )

    var styles: StyleConfigs? = null

    fun getCurrentStyle(): Style {
        if (styles == null) {
            styles = StyleStorage.loadAll(app = app)
        }

        return when (styles!!.currentStyleName) {
            "light" -> light
            "dark" -> dark
            else -> {
                return styles!!.find(styles!!.currentStyleName!!) ?: light
            }
        }
    }

}