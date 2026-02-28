package com.hipoom.cli.todo.handler.show

import com.hipoom.cli.todo.entity.item.Item


internal fun tryFilterForOnlyRoot(originParams: String, items: MutableList<Item>?) {
    if (items == null) {
        return
    }

    var onlyShowRoot = false
    if (originParams.startsWith("show --only-root")) {
        onlyShowRoot = true
    }
    if (onlyShowRoot) {
        items.forEach {
            it.children?.clear()
        }
    }
}