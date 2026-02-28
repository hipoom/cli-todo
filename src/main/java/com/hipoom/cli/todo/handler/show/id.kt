package com.hipoom.cli.todo.handler.show

import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.queryConfig
import com.hipoom.cli.todo.saveConfig
import com.hipoom.cli.workspace.WorkspaceContext

private const val KEY_NEED_SHOW_ID = "need_show_id"

fun WorkspaceContext.enableShowId() {
    saveConfig(KEY_NEED_SHOW_ID, "true")
    Configs.show.needShowId = true
}

fun WorkspaceContext.disableShowId() {
    saveConfig(KEY_NEED_SHOW_ID, "false")
    Configs.show.needShowId = false
}

fun queryNeedShowId(workspace: WorkspaceContext): Boolean {
    return workspace.queryConfig(KEY_NEED_SHOW_ID, "true")?.toBooleanStrictOrNull() ?: true
}