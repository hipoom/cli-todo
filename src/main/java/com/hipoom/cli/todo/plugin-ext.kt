package com.hipoom.cli.todo

import com.hipoom.cli.scaffold.handler.AbsHandler
import com.hipoom.cli.workspace.WorkspaceContext

private fun TodoApp.notifyPlugins(event: String, params: Map<String, Any?>) {
    val temp = HashMap<String, Any?>()
    temp.putAll(params)
    temp["event"] = event
    pluginManager.notifyPlugins(temp)
}

/**
 * 在获取有哪些 handler 的时机触发。
 */
fun TodoApp.notifyOnGetSupportHandlers(handlers: MutableList<AbsHandler>) {
    notifyPlugins(
        event = "on_get_support_handlers",
        params = mapOf(
            "handlers" to handlers
        )
    )
}

/**
 * 工作目录切换时触发。
 */
fun TodoApp.notifyOnWorkspaceChanged(newContext: WorkspaceContext) {
    notifyPlugins(
        event = "on_workspace_changed",
        params = mapOf(
            "newContext" to newContext
        )
    )
}