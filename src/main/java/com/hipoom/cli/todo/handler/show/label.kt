package com.hipoom.cli.todo.handler.show

import com.hipoom.cli.todo.Configs
import com.hipoom.cli.workspace.WorkspaceContext


fun WorkspaceContext.enableShowLabel() {
    Configs.show.needShowLabel = true
}

fun WorkspaceContext.disableShowLabel() {
    Configs.show.needShowLabel = false
}