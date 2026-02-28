package com.hipoom.cli.todo.handler.show

import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.queryConfig
import com.hipoom.cli.todo.saveConfig
import com.hipoom.cli.workspace.WorkspaceContext


private const val KEY_NEED_SHOW_STATUS = "need_show_status"

fun WorkspaceContext.enableShowStatus() {
    saveConfig(KEY_NEED_SHOW_STATUS, "true")
    Configs.show.needShowStatus = true
}

fun WorkspaceContext.disableShowStatus() {
    saveConfig(KEY_NEED_SHOW_STATUS, "false")
    Configs.show.needShowStatus = false
}

fun queryNeedShowStatus(workspace: WorkspaceContext): Boolean {
    return workspace.queryConfig(KEY_NEED_SHOW_STATUS, "true")?.toBooleanStrictOrNull() ?: true
}