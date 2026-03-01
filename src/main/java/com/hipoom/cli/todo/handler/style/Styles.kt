package com.hipoom.cli.todo.handler.style

import com.hipoom.cli.core.ui.palette.Color
import com.hipoom.cli.core.ui.palette.Colors
import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.handler.style.pojo.Style

/**
 * @author ZhengHaiPeng
 * @since 2026/3/1 0:24
 *
 */
object Styles {

    val light = Style(
        pinTextColor = null,
        pinBackgroundColor = null,
        secondaryTextColor = Colors.Bits24.createForeground(100, 100, 100),
        commentBackgroundColor = Colors.Bits24.createBackground(250, 250, 250),
        hintTextColor = Colors.Bits24.createForeground(200, 200, 200)
    )

    val dark = Style(
        pinTextColor = null,
        pinBackgroundColor = null,
        secondaryTextColor = Colors.Bits24.createForeground(230, 230, 230),
        commentBackgroundColor = Colors.Bits24.createBackground(50, 50, 50),
        hintTextColor = Colors.Bits24.createForeground(128, 128, 128)
    )

    fun getCurrentStyle(): Style {
        val styleName = Configs.show.getCurrentStyle()
        return when (styleName) {
            "light" -> light
            "dark" -> dark
            else -> {
                // 默认返回 light 样式
                light
            }
        }
    }

}