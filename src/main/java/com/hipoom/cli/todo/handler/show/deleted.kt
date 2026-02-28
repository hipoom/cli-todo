package com.hipoom.cli.todo.handler.show

import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.entity.item.Item
import com.hipoom.cli.todo.queryConfig
import com.hipoom.cli.workspace.WorkspaceContext



fun WorkspaceContext.enableShowDeleted() {
    Configs.show.needShowDeleted = true
}

fun WorkspaceContext.disableShowDeleted() {
    Configs.show.needShowDeleted = false
}

/**
 * 如果「强制展示已删除事项」的开关是关的，过滤掉已删除的事项。
 */
fun tryFilterDeletedItems(items: MutableList<Item>?) {
    val needShow = Configs.show.needShowDeleted

    // 需要显示 deleted 事项
    if (needShow) {
        return
    }

    // 不需要显示 deleted 事项，清理掉
    cleanDeletedItems(items)
}

private fun cleanDeletedItems(items: MutableList<Item>?) {
    cleanChildrenWithStatus(items, Item.STATUS_DELETED)
}