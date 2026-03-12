package com.hipoom.cli.todo.handler.show

import com.hipoom.cli.core.ui.TextStyleBuilder
import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.defaultTextBlockPrinter
import com.hipoom.cli.todo.entity.item.Item
import com.hipoom.cli.todo.printLine
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
        if (Configs.show.needShowComment && item.comments?.isEmpty() == false) {
            printLine("comments : ", false)

            val printer = defaultTextBlockPrinter

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
        }
        printLine("")
    }

}