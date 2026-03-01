package com.hipoom.cli.todo

import com.hipoom.cli.kvstorage.ext.asJsonObject
import com.hipoom.cli.kvstorage.gson
import com.hipoom.cli.scaffold.utils.readTextIfExist
import com.hipoom.cli.todo.entity.item.ItemDao
import com.hipoom.cli.todo.handler.style.StyleData
import com.hipoom.cli.todo.handler.template.entity.TemplateVO
import com.hipoom.cli.todo.handler.template.entity.Templates
import com.hipoom.cli.workspace.WorkspaceContext
import com.hipoom.cli.workspace.WorkspaceDataRepository
import com.hipoom.files.child
import com.hipoom.files.createNewFileIfNotExist
import com.hipoom.holder.Holder
import com.sun.corba.se.spi.orbutil.threadpool.Work
import com.sun.org.apache.xpath.internal.operations.Bool


fun WorkspaceContext.itemDao(): ItemDao {
    return ItemDao(workspaceDir.child("items.json").absolutePath)
}

fun WorkspaceContext.saveConfig(key: String, value: String) {
    database().save(key, value)
}

fun WorkspaceContext.queryConfig(key: String, default: String?): String? {
    return database().query(key) ?: default
}

fun WorkspaceContext.templates(): Templates {
    val file = workspaceDir.child("templates.json")
    val json = file.readTextIfExist()
    return json?.asJsonObject(Templates::class.java) ?: Templates(version = 0, templates = ArrayList())
}

fun WorkspaceContext.updateTemplates(templates: Templates) {
    val file = workspaceDir.child("templates.json")
    file.createNewFileIfNotExist()
    file.writeText(gson.toJson(templates))
}

fun WorkspaceContext.findTemplateWithAlias(alias: String): TemplateVO? {
    return templates().templates.find { it.alias == alias }
}

fun WorkspaceContext.styles(): Map<String, StyleData> {
    val file = workspaceDir.child("styles.json")
    val json = file.readTextIfExist()
    return json?.asJsonObject(Map::class.java) as? Map<String, StyleData> ?: emptyMap()
}

fun WorkspaceContext.updateStyles(styles: Map<String, StyleData>) {
    val file = workspaceDir.child("styles.json")
    file.createNewFileIfNotExist()
    file.writeText(gson.toJson(styles))
}

fun WorkspaceContext.getCurrentStyleName(): String? {
    return getValueFromDataRepository("current_style")
}

fun WorkspaceContext.setCurrentStyleName(styleName: String) {
    setValueIntoDataRepository("current_style", styleName)
}



/* ======================================================= */
/* Public Methods                                          */
/* ======================================================= */

fun WorkspaceContext.getFocusId(): String? {
    return getValueFromDataRepository("focus_id")
}

fun WorkspaceContext.setFocusId(focusId: String?) {
    setValueIntoDataRepository("focus_id", focusId)
}

fun WorkspaceContext.isQuickMode(): Boolean {
    return getValueFromDataRepository("isQuickMode")?.toBoolean() ?: true
}

fun WorkspaceContext.setQuickMode(isQuickMode: Boolean) {
    setValueIntoDataRepository("isQuickMode", isQuickMode.toString())
}



/* ======================================================= */
/* Private Methods                                         */
/* ======================================================= */

private fun WorkspaceContext.runWithRepository(callback: (WorkspaceDataRepository)->Unit) {
    val temp = app.processData.currentWorkspaceDataRepository
    if (this == temp?.workspace) {
        callback.invoke(temp)
    } else {
        callback.invoke(WorkspaceDataRepository(this))
    }
}

private fun <R> WorkspaceContext.callWithRepository(callback: (WorkspaceDataRepository)->R): R {
    val temp = app.processData.currentWorkspaceDataRepository
    return if (this == temp?.workspace) {
        callback.invoke(temp)
    } else {
        callback.invoke(WorkspaceDataRepository(this))
    }
}

private fun WorkspaceContext.getValueFromDataRepository(key: String): String? {
    return callWithRepository {
        it.get(key)
    }
}

private fun WorkspaceContext.setValueIntoDataRepository(key: String, value: String?) {
    return runWithRepository {
        it.set(key, value)
    }
}