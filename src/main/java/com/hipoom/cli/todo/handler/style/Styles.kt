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
        override val commentTextColor: Color = Colors.Bits24.createForeground(50, 50, 50)
        override val commentBackgroundColor: Color = Colors.Bits24.createBackground(230, 230, 230)
    }

    val dark = object : Style {
        override val pinTextColor: Color? = null
        override val pinBackgroundColor: Color? = null
        override val commentTextColor: Color = Colors.Bits24.createForeground(230, 230, 230)
        override val commentBackgroundColor: Color = Colors.Bits24.createBackground(50, 50, 50)
    }

}