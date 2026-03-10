package com.hipoom.cli.todo.handler.style.actions

import com.hipoom.cli.core.ui.palette.Colors
import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.todo.handler.style.Styles
import com.hipoom.cli.todo.handler.style.persistent.StyleConfigs
import com.hipoom.cli.todo.handler.style.persistent.StyleStorage
import com.hipoom.cli.todo.handler.style.pojo.Style
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.utils.ColorPicker

/**
 * @author ZhengHaiPeng
 * @since 2026/3/1 16:00
 *
 */


 fun handleCreateStyle(app: CliApp) {
    // 从持久化数据中加载所有的样式配置
    val configs: StyleConfigs = StyleStorage.loadAll(app)

    printLine("现有的配色方案：")

    // 展示现有的配色方案
    configs.name2Style?.forEachIndexed { index, pair ->
        pair.style?.showDemo( "${index}." + pair.name)

    }
    

    val input = com.hipoom.cli.todo.readLineWithPrompt("请选择您要基于哪个现有方案创建新方案: ")
    val baseIndex = input?.toIntOrNull() ?: return
    val baseStyle: Style = configs.name2Style?.get(baseIndex)?.style!!
    val customColor = baseStyle.clone()

    var yn = com.hipoom.cli.todo.readLineWithPrompt("是否要修改置顶事项的文字颜色? (Y/N)")
    if (yn == "Y" || yn == "y") {
        val colorCode = ColorPicker.choose256Colors()
        if (colorCode != null) {
            customColor.pinTextColor = Colors.Bits8.createForeground(colorCode)
        }
    }

    yn = com.hipoom.cli.todo.readLineWithPrompt("是否要修改置顶事项的背景颜色? (Y/N)")
    if (yn == "Y" || yn == "y") {
        val colorCode = ColorPicker.choose256Colors()
        if (colorCode != null) {
            customColor.pinBackgroundColor = Colors.Bits8.createBackground(colorCode)
        }
    }

    yn = com.hipoom.cli.todo.readLineWithPrompt("是否要修改备注的文字颜色? (Y/N)")
    if (yn == "Y" || yn == "y") {
        val colorCode = ColorPicker.choose256Colors()
        if (colorCode != null) {
            customColor.secondaryTextColor = Colors.Bits8.createForeground(colorCode)
        }
    }

    yn = com.hipoom.cli.todo.readLineWithPrompt("是否要修改备注的背景颜色? (Y/N)")
    if (yn == "Y" || yn == "y") {
        val colorCode = ColorPicker.choose256Colors()
        if (colorCode != null) {
            customColor.commentBackgroundColor = Colors.Bits8.createBackground(colorCode)
        }
    }


    customColor.showDemo("当前颜色方案：")

    yn = com.hipoom.cli.todo.readLineWithPrompt("是否保存？ (Y/N)")
    var name: String? = null
    if (yn == "Y" || yn == "y") {
        name = com.hipoom.cli.todo.readLineWithPrompt("请输入配色方案的名称: ")
        if (name != null) {
            StyleStorage.addOrReplaceStyle(app = app, name = name, style = customColor)
        }

        yn = com.hipoom.cli.todo.readLineWithPrompt("是否立即使用？ (Y/N)")
        if (yn == "Y" || yn == "y") {
            StyleStorage.useStyle(app, name!!)
        }
    }

    Styles.styles = null
 }

