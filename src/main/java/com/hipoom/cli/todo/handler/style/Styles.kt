package com.hipoom.cli.todo.handler.style

import com.hipoom.cli.core.ui.palette.Color
import com.hipoom.cli.core.ui.palette.Colors

/**
 * @author ZhengHaiPeng
 * @since 2026/3/1 0:24
 *
 */
object Styles {

    val light = object : Style {
        override val pinTextColor: Color? = null
        override val pinBackgroundColor: Color? = null
        override val secondaryTextColor: Color = Colors.Bits24.createForeground(100, 100, 100)
        override val commentBackgroundColor: Color = Colors.Bits24.createBackground(250, 250, 250)
        override val hintTextColor: Color = Colors.Bits24.createForeground(200, 200, 200)
    }

    val dark = object : Style {
        override val pinTextColor: Color? = null
        override val pinBackgroundColor: Color? = null
        override val secondaryTextColor: Color = Colors.Bits24.createForeground(230, 230, 230)
        override val commentBackgroundColor: Color = Colors.Bits24.createBackground(50, 50, 50)
        override val hintTextColor: Color = Colors.Bits24.createForeground(128, 128, 128)
    }

    fun getCurrentStyle(): Style {
        return light
    }

}