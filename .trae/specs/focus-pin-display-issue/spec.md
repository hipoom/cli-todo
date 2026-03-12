# Focus 模式下无法展示 Pin 事件问题分析

## Why
用户报告在 focus 模式下，pin（置顶）的事项无法显示。需要分析原因并提出解决方案。

## 问题分析

### 根本原因

通过代码分析，发现 **focus 模式下缺少显示 pinned items 的逻辑**。

#### 正常模式的显示流程 (ShowHandler.onShow)
```
ShowHandler.onShow()
  → showAutoMode(workspace, items)
    → showItemsAsTreeMode(items)
      → showPinnedItems(items)  ← ✅ 先显示 pin 事项
      → buildParentModeTree()   ← 然后显示树状结构
```

参见 [common.kt:615-626](file:///c:\Workspace\Github\cli-todo\src\main\java\com\hipoom\cli\todo\handler\show\common.kt#L615-L626):
```kotlin
fun WorkspaceContext.showItemsAsTreeMode(items: List<Item>?) {
    showPinnedItems(items)  // ← 正常模式会先显示 pin 事项
    // 构建并显示树状结构
    val rows = LinkedList<TreeModeRow>()
    ...
}
```

#### Focus 模式的显示流程 (tryShowByFocus)
```
tryShowByFocus()
  → loadAsTree(id) 获取 focus 事项
  → 过滤已删除、已完成事项
  → showAsWhoMode() 或 buildAsParentMode().show()
  → ❌ 缺少 showPinnedItems() 调用
```

参见 [focus.kt:10-40](file:///c:\Workspace\Github\cli-todo\src\main\java\com\hipoom\cli\todo\handler\show\focus.kt#L10-L40):
```kotlin
fun tryShowByFocus(originParams: String, workspace: WorkspaceContext): Boolean {
    val focusId = workspace.getFocusId() ?: return false
    val id = focusId.toIntOrNull() ?: return false

    workspace.itemDao().loadAsTree(
        id = id,
        onFound = { item ->
            tryFilterDeletedItems(items = item.children)
            tryFilterDoneItems(items = item.children)
            tryFilterForOnlyRoot(originParams, item.children)

            val isWhoMode = GroupHandler.isOwnerMode(workspace)
            if (isWhoMode) {
                showAsWhoMode(items = item.children, workspace = workspace)
            } else {
                buildAsParentMode(workspace, "", item, true).show()
                // ❌ 没有调用 showPinnedItems()
            }
            printLine()
        }
    )
    return true
}
```

### 次要问题

即使添加了 `showPinnedItems` 调用，还存在另一个问题：

`showPinnedItems` 函数需要从传入的 items 列表中过滤出 pinned items：

```kotlin
fun WorkspaceContext.showPinnedItems(items: List<Item>?) {
    val pinnedIds = database().query("pins").parseIds().operators
    val pinnedItems = items?.filter { pinnedIds.contains(it.id) }
    // ...
}
```

在 focus 模式下，如果 pin 的事项**不在当前 focus 的子树中**，即使调用了 `showPinnedItems`，也无法显示，因为传入的 items 只包含 focus 事项的子节点。

## What Changes

### 方案：在 focus 模式下显示 pin 事项

修改 `tryShowByFocus` 函数，在显示 focus 事项之前，先加载所有事项并显示 pinned items。

**修改点**：
- 在 `tryShowByFocus` 函数中，添加显示 pinned items 的逻辑
- 需要加载所有事项（而不只是 focus 的子树）来过滤 pinned items

## Impact
- Affected code: [focus.kt](file:///c:\Workspace\Github\cli-todo\src\main\java\com\hipoom\cli\todo\handler\show\focus.kt)
- Affected specs: 显示功能

## ADDED Requirements

### Requirement: Focus 模式下显示 Pin 事项

系统应在 focus 模式下也显示 pinned（置顶）的事项。

#### Scenario: Focus 模式下显示 pin 事项
- **GIVEN** 用户设置了 focus 模式
- **AND** 存在 pinned 的事项
- **WHEN** 用户执行 show 命令
- **THEN** 系统应先显示所有 pinned 事项
- **AND** 然后显示 focus 事项及其子事项
