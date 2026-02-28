package com.hipoom.cli.todo.handler.add

import com.hipoom.cli.core.ui.TextEditor
import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.entity.item.Item
import com.hipoom.cli.todo.gson
import com.hipoom.cli.todo.handler.show.ShowHandler
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.workspace.WorkspaceContext

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 12:08
 */


fun addWithAdvanceMode(app: CliApp, workspace: WorkspaceContext) {
    if (!Configs.window.isEnable) {
        printLine("目前只支持在 window 模式下使用高级模式. 可以通过 window --enable 开启 window 模式.")
        return
    }

    val origin = Item().apply {
        this.parentIds = mutableListOf(0)
        this.content = "请在这里输入内容"
        this.status = Item.STATUS_NEW
        this.owner = ""
        this.labels = ArrayList()
    }

    val json = gson.toJson(origin)

    val edited = TextEditor.edit(
        input = json,
        prompt = "编辑完成后关闭窗口"
    )

    val obj = gson.fromJson(edited, Item::class.java)
    workspace.itemDao().insert(obj)

    ShowHandler().onHandle("show", app, workspace)
}