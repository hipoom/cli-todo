# 子事项自定义排序功能 Spec

## Why

当前子事项展示时是按照 id 顺序展示的，用户无法自定义调整子事项的显示顺序。希望能够增加自定义展示顺序的能力，让用户可以按照自己的优先级或逻辑顺序排列子事项。

## What Changes

* 在 Item 实体中添加 `order` 字段，用于存储排序值

* 新增 `sort` 命令处理器，支持调整子事项顺序

* 修改展示逻辑，按 `order` 字段排序显示子事项

## Impact

* Affected specs: Item 实体结构、ItemDao 数据访问层

* Affected code:

  * `Item.kt` - 添加 order 字段

  * `ItemDao.kt` - 添加排序相关方法

  * `common.kt` (show handler) - 修改展示时的排序逻辑

  * 新增 `SortHandler.kt` 和 `options.kt`

## ADDED Requirements

### Requirement: 子事项排序字段

系统 SHALL 为每个 Item 提供一个 `order` 字段，用于存储该事项在其父节点下的排序位置。

#### Scenario: 默认排序值

* **WHEN** 创建新事项时

* **THEN** 系统自动分配一个合适的 order 值（默认为当前同级事项的最大 order + 1）

### Requirement: 排序命令

系统 SHALL 提供 `sort` 命令，允许用户调整子事项的显示顺序。

#### Scenario: 上移子事项

* **WHEN** 用户执行 `sort -i <id> --up` 命令

* **THEN** 指定事项在其同级事项中向上移动一位

#### Scenario: 下移子事项

* **WHEN** 用户执行 `sort -i <id> --down` 命令

* **THEN** 指定事项在其同级事项中向下移动一位

#### Scenario: 置顶子事项

* **WHEN** 用户执行 `sort -i <id> --top` 命令

* **THEN** 指定事项移动到同级事项的最前面

#### Scenario: 置底子事项

* **WHEN** 用户执行 `sort -i <id> --bottom` 命令

* **THEN** 指定事项移动到同级事项的最后面

### Requirement: 展示排序

系统 SHALL 在展示事项时，按照 order 字段升序排列同级子事项。

#### Scenario: 按 order 排序展示

* **WHEN** 用户查看事项列表

* **THEN** 同一父节点下的子事项按 order 值从小到大排列

#### Scenario: 兼容旧数据

* **WHEN** 展示没有 order 字段的旧数据时

* **THEN** 系统将 order 视为 0 或按 id 排序作为后备方案

## MODIFIED Requirements

无

## REMOVED Requirements

无

## 详细设计

### options.kt 设计

```kotlin
package com.hipoom.cli.todo.handler.sort

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

val sortOptions: Options = Options()
    .addOption(
        Option.builder("i")
            .longOpt("id")
            .hasArg(true)
            .valueSeparator(',')
            .desc("指定需要调整顺序的事项 ID")
            .build()
    )
    .addOption(
        Option.builder("u")
            .longOpt("up")
            .desc("上移一位：与上一个兄弟节点交换位置")
            .build()
    )
    .addOption(
        Option.builder("d")
            .longOpt("down")
            .desc("下移一位：与下一个兄弟节点交换位置")
            .build()
    )
    .addOption(
        Option.builder("t")
            .longOpt("top")
            .desc("置顶：移动到同级事项的最前面")
            .build()
    )
    .addOption(
        Option.builder("b")
            .longOpt("bottom")
            .desc("置底：移动到同级事项的最后面")
            .build()
    )
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .desc("显示帮助信息")
            .build()
    )
```

### SortHandler.kt 设计

```kotlin
package com.hipoom.cli.todo.handler.sort

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.handler.show.show
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.utils.parseIds
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options

class SortHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = sortOptions

    override val supportPrefixes: List<String> = listOf("sort")

    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "调整子事项的显示顺序"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            // 帮助
            commandLine.hasOption("h") -> printHelp()
            
            // 上移
            commandLine.hasOption("i") && commandLine.hasOption("u") -> 
                moveUp(workspace, app, commandLine)
            
            // 下移
            commandLine.hasOption("i") && commandLine.hasOption("d") -> 
                moveDown(workspace, app, commandLine)
            
            // 置顶
            commandLine.hasOption("i") && commandLine.hasOption("t") -> 
                moveToTop(workspace, app, commandLine)
            
            // 置底
            commandLine.hasOption("i") && commandLine.hasOption("b") -> 
                moveToBottom(workspace, app, commandLine)
            
            else -> {
                printLine("无法识别的指令")
                printHelp()
            }
        }
        return true
    }

    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun moveUp(workspace: WorkspaceContext, app: CliApp, commandLine: CommandLine) {
        val (ids, _) = commandLine.getOptionValue("i").parseIds()
        ids.forEach { id ->
            workspace.itemDao().sortMoveUp(id)
        }
        app.show()
    }

    private fun moveDown(workspace: WorkspaceContext, app: CliApp, commandLine: CommandLine) {
        val (ids, _) = commandLine.getOptionValue("i").parseIds()
        ids.forEach { id ->
            workspace.itemDao().sortMoveDown(id)
        }
        app.show()
    }

    private fun moveToTop(workspace: WorkspaceContext, app: CliApp, commandLine: CommandLine) {
        val (ids, _) = commandLine.getOptionValue("i").parseIds()
        ids.forEach { id ->
            workspace.itemDao().sortMoveToTop(id)
        }
        app.show()
    }

    private fun moveToBottom(workspace: WorkspaceContext, app: CliApp, commandLine: CommandLine) {
        val (ids, _) = commandLine.getOptionValue("i").parseIds()
        ids.forEach { id ->
            workspace.itemDao().sortMoveToBottom(id)
        }
        app.show()
    }
}
```

### ItemDao 新增方法设计

```kotlin
/**
 * 将指定事项向上移动一位（与上一个兄弟交换 order 值）
 * 如果已经是第一个，则不做任何操作
 */
fun sortMoveUp(id: Int) {
    val table = load(needBuildTree = true)
    val current = table.items?.find { it.id == id } ?: return
    val parentId = current.getFirstParentIdOrNull()
    
    // 获取同级兄弟节点
    val brothers = if (parentId == null) {
        table.items?.filter { it.parentIds.isNullOrEmpty() } ?: emptyList()
    } else {
        table.items?.filter { it.parentIds?.contains(parentId) == true } ?: emptyList()
    }
    
    // 按 order 排序
    val sortedBrothers = brothers.sortedBy { it.order ?: it.id ?: 0 }
    
    // 找到当前位置
    val currentIndex = sortedBrothers.indexOfFirst { it.id == id }
    if (currentIndex <= 0) return // 已经是第一个
    
    // 交换 order 值
    val prevBrother = sortedBrothers[currentIndex - 1]
    val tempOrder = current.order
    current.order = prevBrother.order
    prevBrother.order = tempOrder
    
    store(table)
    last_modify_item_id = id
}

/**
 * 将指定事项向下移动一位（与下一个兄弟交换 order 值）
 * 如果已经是最后一个，则不做任何操作
 */
fun sortMoveDown(id: Int) {
    // 实现逻辑与 sortMoveUp 类似
}

/**
 * 将指定事项移动到同级事项的最前面
 */
fun sortMoveToTop(id: Int) {
    val table = load(needBuildTree = true)
    val current = table.items?.find { it.id == id } ?: return
    val parentId = current.getFirstParentIdOrNull()
    
    // 获取同级兄弟节点
    val brothers = if (parentId == null) {
        table.items?.filter { it.parentIds.isNullOrEmpty() } ?: emptyList()
    } else {
        table.items?.filter { it.parentIds?.contains(parentId) == true } ?: emptyList()
    }
    
    // 找到最小的 order 值
    val minOrder = brothers.minOfOrNull { it.order ?: it.id ?: 0 } ?: 0
    
    // 将当前事项的 order 设置为最小值 - 1
    current.order = minOrder - 1
    
    store(table)
    last_modify_item_id = id
}

/**
 * 将指定事项移动到同级事项的最后面
 */
fun sortMoveToBottom(id: Int) {
    val table = load(needBuildTree = true)
    val current = table.items?.find { it.id == id } ?: return
    val parentId = current.getFirstParentIdOrNull()
    
    // 获取同级兄弟节点
    val brothers = if (parentId == null) {
        table.items?.filter { it.parentIds.isNullOrEmpty() } ?: emptyList()
    } else {
        table.items?.filter { it.parentIds?.contains(parentId) == true } ?: emptyList()
    }
    
    // 找到最大的 order 值
    val maxOrder = brothers.maxOfOrNull { it.order ?: it.id ?: 0 } ?: 0
    
    // 将当前事项的 order 设置为最大值 + 1
    current.order = maxOrder + 1
    
    store(table)
    last_modify_item_id = id
}
```

### 命令使用示例

```bash
# 上移事项 5
sort -i 5 --up
sort -i 5 -u

# 下移事项 5
sort -i 5 --down
sort -i 5 -d

# 将事项 5 置顶
sort -i 5 --top
sort -i 5 -t

# 将事项 5 置底
sort -i 5 --bottom
sort -i 5 -b

# 批量操作（上移多个事项）
sort -i 5,6,7 --up
```

