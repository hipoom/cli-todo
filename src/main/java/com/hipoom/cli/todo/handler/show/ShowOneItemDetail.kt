package com.hipoom.cli.todo.handler.show

import com.hipoom.cli.core.ui.CharWidthCalculator
import com.hipoom.cli.core.ui.TextBlockPrinter
import com.hipoom.cli.core.ui.TextStyle
import com.hipoom.cli.core.ui.TextStyleBuilder
import com.hipoom.cli.core.ui.palette.Colors
import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.entity.item.Item
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.utils.displayWidth
import sun.security.tools.keytool.Main
import java.text.SimpleDateFormat

/**
 * @author ZhengHaiPeng
 * @since 2025/2/3 22:52
 *
 */
object ShowOneItemDetail {

    fun show(item: Item?) {
        if (item == null) {
            return
        }

        printLine("id       : " + item.id)
        printLine("content  : " + item.content)
        printLine("owner    : " + item.owner)
        printLine("status   : " + item.status)
        if (!item.labels.isNullOrEmpty()) {
            printLine("labels   : " + item.labels?.joinToString { it })
        }
        if (item.deadline != null) {
            printLine("deadline : " + SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(item.deadline))
        } else {
            printLine("deadline : null")
        }
        if (item.comments?.isEmpty() == false) {
            printLine("comments : ", false)
        }

        val printer = TextBlockPrinter(
            printer = com.hipoom.cli.todo.Main.printer,
            charWidthCalculator = object : CharWidthCalculator {
                override fun calculate(text: String): Int {
                    return text.displayWidth()
                }
            }
        )

        val textColor = Configs.show.commentStyle.getTextColor()
        val style = TextStyleBuilder()
            .color(textColor)
            .build()

        item.comments?.forEachIndexed { index, comment ->
            if (index == 0) {
                printer.print(0, 60, comment + "\n", style)
            } else {
                printer.print(11, 60, comment + "\n", style)
            }
        }
        printLine("")
    }

}