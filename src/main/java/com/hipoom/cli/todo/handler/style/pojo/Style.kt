package com.hipoom.cli.todo.handler.style.pojo

import com.google.gson.annotations.SerializedName
import com.hipoom.cli.core.ui.TextStyle
import com.hipoom.cli.core.ui.TextStyleBuilder
import com.hipoom.cli.core.ui.palette.Color
import com.hipoom.cli.todo.defaultTextBlockPrinter
import com.hipoom.cli.todo.printLine

/**
 * @author ZhengHaiPeng
 * @since 2026/3/1 0:24
 *
 */
class Style(
    @SerializedName("pinTextColor")
    var pinTextColor: Color?,

    @SerializedName("pinBackgroundColor")
    var pinBackgroundColor: Color?,

    @SerializedName("secondaryTextColor")
    var secondaryTextColor: Color?,

    @SerializedName("commentBackgroundColor")
    var commentBackgroundColor: Color?,

    @SerializedName("hintTextColor")
    var hintTextColor: Color?
) {

    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    fun getCommentBlockStyle(): TextStyle {
        return TextStyleBuilder()
            .color(secondaryTextColor)
            .backgroundColor(commentBackgroundColor)
            .build()
    }

    fun getPinBlockStyle(): TextStyle {
        return TextStyleBuilder()
            .color(pinTextColor)
            .backgroundColor(pinBackgroundColor)
            .build()
    }

    fun showDemo(name: String) {
        val printer = defaultTextBlockPrinter
        printer.printLine(indent = 0, text = name)
        printer.printLine(indent = 4, text = "置顶事项的展示效果", style = getPinBlockStyle())
        printer.printLine(indent = 4, text = "备注的展示效果", style = getCommentBlockStyle())
        printLine()
    }

    fun clone(): Style {
        return Style(
            pinTextColor,
            pinBackgroundColor,
            secondaryTextColor,
            commentBackgroundColor,
            hintTextColor
        )
    }

}