package com.hipoom.cli.todo.handler.show

import com.hipoom.cli.todo.getFocusId
import com.hipoom.cli.todo.handler.group.GroupHandler
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.workspace.WorkspaceContext


fun tryShowByFocus(originParams: String, workspace: WorkspaceContext): Boolean {
    // 如果没有设置 focus-id, 交给外面处理
    val focusId = workspace.getFocusId() ?: return false
    // 获取 focus-id
    val id = focusId.toIntOrNull() ?: return false

    // 获取 focus 的事项
    workspace.itemDao().loadAsTree(
        id = id,
        onFound = { item ->
            // 清理已被删除的 items
            tryFilterDeletedItems(items = item.children)

            // 清理已完成的 items
            tryFilterDoneItems(items = item.children)

            // 如果只展示根节点，清理所有非根节点
            tryFilterForOnlyRoot(originParams, item.children)

            val isWhoMode = GroupHandler.isOwnerMode(workspace)
            if (isWhoMode) {
                showAsWhoMode(items = item.children, workspace = workspace)
            }
            else {
                buildAsParentMode(workspace, "", item, true).show()
            }
            printLine()
        }
    )
    return true
}