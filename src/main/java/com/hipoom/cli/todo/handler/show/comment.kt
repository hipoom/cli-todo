package com.hipoom.cli.todo.handler.show

import com.hipoom.cli.todo.Configs
import com.hipoom.cli.workspace.WorkspaceContext


fun WorkspaceContext.enableShowComment() {
    Configs.show.needShowComment = true
}

fun WorkspaceContext.disableShowComment() {
    Configs.show.needShowComment = false
}