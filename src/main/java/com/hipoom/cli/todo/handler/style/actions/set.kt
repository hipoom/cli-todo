package com.hipoom.cli.todo.handler.style.actions

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.todo.defaultTextBlockPrinter
import com.hipoom.cli.todo.error
import com.hipoom.cli.todo.gson
import com.hipoom.cli.todo.handler.style.persistent.NameStylePair
import com.hipoom.cli.todo.handler.style.persistent.StyleStorage
import com.hipoom.cli.todo.persistentData
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.readLineWithPrompt
import org.apache.commons.cli.CommandLine

/**
 * @author ZhengHaiPeng
 * @since 2026/3/1 15:59
 *
 */
fun setStyle(app: CliApp, commandLine: CommandLine) {
    val configs = StyleStorage.loadAll(app)
    val styles: List<NameStylePair> = configs.name2Style ?: listOf()

    if (styles.isNotEmpty()) {
        printLine("可选的配色方案:")
        styles.forEachIndexed { index, style ->
            style.style?.showDemo("$index. " + style.name)
        }
    }

    val input = readLineWithPrompt("请输入要设置的方案序号:")
    val index = input?.toIntOrNull()

    // 检查序号是否为空
    if (index == null) {
        defaultTextBlockPrinter.error("您输入的序号无法识别.")
        return
    }

    // 检查序号是否超出范围
    if (index >= styles.size) {
        defaultTextBlockPrinter.error("您输入的序号超出范围.")
        return
    }

    // 设置选中的样式
    val selectedStyle: NameStylePair = styles[index]
    configs.currentStyleName = selectedStyle.name

    // 更新持久化数据
    val json = gson.toJson(configs)
    app.persistentData.updateStyles(json)
}
