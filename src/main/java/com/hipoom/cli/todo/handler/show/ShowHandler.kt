package com.hipoom.cli.todo.handler.show

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.entity.item.Item
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.handler.view.VirtualViews
import com.hipoom.cli.todo.handler.view.virtualViews
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/2/2 17:22
 *
 */
class ShowHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = showOptions

    override val supportPrefixes: List<String> = listOf("show")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Show Items"

    override fun onHandle(originParams: String, commandLine: CommandLine, app: CliApp, workspace: WorkspaceContext): Boolean {
        when {
            // enable-done | disable-done
            commandLine.hasOption("enable-done") -> workspace.enableShowDone()
            commandLine.hasOption("disable-done") -> workspace.disableShowDone()

            // enable-deleted | disable-deleted
            commandLine.hasOption("enable-deleted") -> workspace.enableShowDeleted()
            commandLine.hasOption("disable-deleted") -> workspace.disableShowDeleted()

            // enable-show-on-launch | disable-show-on-launch
            commandLine.hasOption("enable-show-on-launch") -> Configs.launch.needShowOnLaunch = true
            commandLine.hasOption("disable-show-on-launch") -> Configs.launch.needShowOnLaunch = false

            // enable-status | disable-status
            commandLine.hasOption("enable-status") -> workspace.enableShowStatus()
            commandLine.hasOption("disable-status") -> workspace.disableShowStatus()

            // enable-id | disable-id
            commandLine.hasOption("enable-id") -> workspace.enableShowId()
            commandLine.hasOption("disable-id") -> workspace.disableShowId()

            // enable-owner | disable-owner
            commandLine.hasOption("enable-owner") -> workspace.enableShowOwner()
            commandLine.hasOption("disable-owner") -> workspace.disableShowOwner()

            // enable-label | disable-label
            commandLine.hasOption("enable-label") -> workspace.enableShowLabel()
            commandLine.hasOption("disable-label") -> workspace.disableShowLabel()

            // enable-comment | disable-comment
            commandLine.hasOption("enable-comment") -> workspace.enableShowComment()
            commandLine.hasOption("disable-comment") -> workspace.disableShowComment()

            // help
            commandLine.hasOption("h") -> printHelp()
        }

        if (!commandLine.hasOption("h")) {
            onShow(originParams, workspace)
        }

        return true
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun onShow(originParams: String, workspace: WorkspaceContext) {
        // 尝试先交给 focus 逻辑处理
        val handledByFocus = tryShowByFocus(originParams, workspace)
        if (handledByFocus) {
            return
        }

        // 加载数据到内存
        val items = workspace.itemDao().loadAsTree()
        if (items.isEmpty()) {
            printLine("没有事项~")
            return
        }

        // 检查是否处于虚拟视图模式，如果是，则只显示该视图包含的事项
        val virtualViews = workspace.virtualViews()
        if (virtualViews.currentView != null) {
            val currentView = virtualViews.views.find { it.name == virtualViews.currentView }
            if (currentView != null) {
                val viewItemIds = currentView.itemIds.toSet()
                filterItemsByView(items, viewItemIds)
            }
        }

        // 清理已被删除的 items
        tryFilterDeletedItems(items)

        // 清理已完成的 items
        tryFilterDoneItems(items)

        // 如果过滤后没有事项，显示提示
        if (items.isEmpty()) {
            return
        }

        // 如果只展示根节点，清理所有非根节点
        tryFilterForOnlyRoot(originParams, items)

        // 展示
        showAutoMode(workspace, items)
        printLine("")
    }

    /**
     * 根据虚拟视图过滤事项
     */
    private fun filterItemsByView(items: MutableList<Item>, viewItemIds: Set<Int>) {
        // 先保留所有包含在视图中或有子项包含在视图中的项
        val filteredItems = items.filter { item ->
            hasItemInView(item, viewItemIds)
        }.toMutableList()

        // 清空原列表并添加过滤后的项
        items.clear()
        items.addAll(filteredItems)

        // 递归过滤每个项的子项
        items.forEach { item ->
            if (item.children?.isNotEmpty() == true) {
                filterItemsByView(item.children ?: mutableListOf(), viewItemIds)
            }
        }
    }

    /**
     * 检查一个项或其子项是否包含在视图中
     */
    private fun hasItemInView(item: Item, viewItemIds: Set<Int>): Boolean {
        // 如果当前项在视图中，返回 true
        if (viewItemIds.contains(item.id)) {
            return true
        }

        if (item.children == null) {
            return false
        }

        // 检查子项是否有在视图中的
        for (child in item.children!!) {
            if (hasItemInView(child, viewItemIds)) {
                return true
            }
        }

        return false
    }

}