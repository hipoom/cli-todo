package com.hipoom.cli.todo.handler.style

import com.hipoom.cli.core.ui.TextStyle
import com.hipoom.cli.core.ui.TextStyleBuilder
import com.hipoom.cli.core.ui.palette.Color

/**
 * @author ZhengHaiPeng
 * @since 2026/3/1 0:24
 *
 */
interface Style {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    val pinTextColor: Color?

    val pinBackgroundColor: Color?

    val secondaryTextColor: Color?

    val commentBackgroundColor: Color?

    val hintTextColor: Color?



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    fun getCommentBlockStyle(): TextStyle {
        return TextStyleBuilder()
            .color(secondaryTextColor)
            .backgroundColor(commentBackgroundColor)
            .build()
    }

}