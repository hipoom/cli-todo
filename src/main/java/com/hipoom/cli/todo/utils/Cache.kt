package com.hipoom.cli.todo.utils

import com.hipoom.cli.workspace.WorkspaceContext

private lateinit var currentWorkspace: WorkspaceContext

//fun updateWorkspace(workspace: WorkspaceContext) {
//    currentWorkspace = workspace
//    isQuickMode = workspace.database().query("isQuickMode")?.toBoolean() ?: false
//}
//
//fun currentWorkspace(): WorkspaceContext {
//    return currentWorkspace
//}
//
//var isQuickMode: Boolean = false