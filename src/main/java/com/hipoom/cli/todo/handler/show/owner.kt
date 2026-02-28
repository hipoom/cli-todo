package com.hipoom.cli.todo.handler.show

import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.queryConfig
import com.hipoom.cli.todo.saveConfig
import com.hipoom.cli.workspace.WorkspaceContext



fun WorkspaceContext.enableShowOwner() {
    Configs.show.needShowOwner = true
}

fun WorkspaceContext.disableShowOwner() {
    Configs.show.needShowOwner = false
}