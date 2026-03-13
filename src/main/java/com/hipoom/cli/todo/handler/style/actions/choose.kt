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
 * 交互式选择配色方案
 * 通过序号输入方式选择配色方案
 * 
 * @param app CLI 应用实例
 * @param commandLine 命令行参数
 */
fun chooseStyle(app: CliApp, commandLine: CommandLine) {
    // 加载所有配色方案配置
    val configs = StyleStorage.loadAll(app)
    val styles: List<NameStylePair> = configs.name2Style ?: listOf()

    // 如果没有可用的配色方案，提示用户并返回
    if (styles.isEmpty()) {
        defaultTextBlockPrinter.error("没有可用的配色方案.")
        return
    }

    // 显示可选的配色方案列表
    printLine("可选的配色方案:")
    
    // 遍历所有配色方案并显示
    styles.forEachIndexed { index, style ->
        // 判断是否为当前使用的方案
        val isCurrent = style.name == configs.currentStyleName
        
        // 当前使用的方案显示 ✓ 标识
        val checkMark = if (isCurrent) "✓" else " "
        
        // 显示序号、标识和方案名称，并展示预览效果
        style.style?.showDemo("$checkMark $index. " + style.name)
    }

    // 提示用户输入序号
    val input = readLineWithPrompt("请输入要选择的方案序号:")
    val index = input?.toIntOrNull()

    // 检查序号是否为空
    if (index == null) {
        defaultTextBlockPrinter.error("您输入的序号无法识别.")
        return
    }

    // 检查序号是否超出范围
    if (index < 0 || index >= styles.size) {
        defaultTextBlockPrinter.error("您输入的序号超出范围.")
        return
    }

    // 设置选中的样式
    val selectedStyle: NameStylePair = styles[index]
    configs.currentStyleName = selectedStyle.name

    // 更新持久化数据
    val json = gson.toJson(configs)
    app.persistentData.updateStyles(json)
    
    // 显示成功提示
    printLine("✓ 已选择配色方案: ${selectedStyle.name}")
}
