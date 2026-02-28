package com.hipoom.cli.todo.handler.show

import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.entity.item.Item
import com.hipoom.cli.workspace.WorkspaceContext


fun WorkspaceContext.enableShowDone() {
    Configs.show.needShowDone = true
}

fun WorkspaceContext.disableShowDone() {
    Configs.show.needShowDone = false
}

internal fun tryFilterDoneItems(items: MutableList<Item>?) {
    val needShow = Configs.show.needShowDone

    // 需要显示 done 事项
    if (needShow) {
        return
    }

    // 不需要显示 done 事项，清理掉
    cleanChildrenWithStatus(items, Item.STATUS_DONE)
}