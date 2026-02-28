package com.hipoom.cli.todo.handler.view

import com.hipoom.cli.kvstorage.ext.asJsonObject
import com.hipoom.cli.scaffold.utils.readTextIfExist
import com.hipoom.cli.todo.gson
import com.hipoom.cli.workspace.WorkspaceContext
import com.hipoom.files.child
import com.hipoom.files.createNewFileIfNotExist

/**
 * 虚拟视图的数据结构
 * @property name 视图名称
 * @property itemIds 视图包含的item ID列表
 */
data class VirtualView(
    val name: String,
    val itemIds: MutableList<Int> = mutableListOf()
)

/**
 * 虚拟视图集合
 */
data class VirtualViews(
    val version: Int = 1,
    val views: MutableList<VirtualView> = mutableListOf(),
    var currentView: String? = null
)

/**
 * 获取所有虚拟视图
 */
fun WorkspaceContext.virtualViews(): VirtualViews {
    val file = workspaceDir.child("virtual_views.json")
    val json = file.readTextIfExist()
    return json?.asJsonObject(VirtualViews::class.java) ?: VirtualViews()
}

/**
 * 更新虚拟视图
 */
fun WorkspaceContext.updateVirtualViews(views: VirtualViews) {
    val file = workspaceDir.child("virtual_views.json")
    file.createNewFileIfNotExist()
    file.writeText(gson.toJson(views))
}

/**
 * 查找虚拟视图
 */
fun WorkspaceContext.findVirtualView(name: String): VirtualView? {
    return virtualViews().views.find { it.name == name }
}

/**
 * 创建虚拟视图
 */
fun WorkspaceContext.createVirtualView(name: String): Boolean {
    val views = virtualViews()
    if (views.views.any { it.name == name }) {
        return false
    }
    views.views.add(VirtualView(name))
    updateVirtualViews(views)
    return true
}

/**
 * 删除虚拟视图
 */
fun WorkspaceContext.deleteVirtualView(name: String): Boolean {
    val views = virtualViews()
    val removed = views.views.removeIf { it.name == name }
    if (removed) {
        // 如果删除的是当前视图，则退出视图模式
        if (views.currentView == name) {
            views.currentView = null
        }
        updateVirtualViews(views)
    }
    return removed
}

/**
 * 为虚拟视图添加item
 */
fun WorkspaceContext.addItemToVirtualView(viewName: String, itemId: Int): Boolean {
    val views = virtualViews()
    val view = views.views.find { it.name == viewName }
    if (view == null) {
        return false
    }
    if (!view.itemIds.contains(itemId)) {
        view.itemIds.add(itemId)
        updateVirtualViews(views)
    }
    return true
}

/**
 * 切换到虚拟视图
 */
fun WorkspaceContext.switchToVirtualView(name: String): Boolean {
    val views = virtualViews()
    if (views.views.any { it.name == name }) {
        views.currentView = name
        updateVirtualViews(views)
        return true
    }
    return false
}

/**
 * 退出虚拟视图模式
 */
fun WorkspaceContext.exitVirtualView() {
    val views = virtualViews()
    views.currentView = null
    updateVirtualViews(views)
}

/**
 * 获取当前虚拟视图
 */
fun WorkspaceContext.getCurrentVirtualView(): VirtualView? {
    val views = virtualViews()
    return views.currentView?.let { findVirtualView(it) }
}