package com.hipoom.cli.todo.handler.view

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.scaffold.utils.readInt
import com.hipoom.cli.scaffold.utils.readString
import com.hipoom.cli.todo.handler.focus.FocusHandler
import com.hipoom.cli.todo.handler.show.ShowHandler
import com.hipoom.cli.todo.handler.view.*
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.utils.parseIds
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/2/2 17:22
 *
 */
class ViewHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = viewOptions

    override val supportPrefixes: List<String> = listOf("view")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Virtual View"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        // Help
        if (commandLine.hasOption("h")) {
            printHelp()
            return true
        }

        // Create view
        if (commandLine.hasOption("c")) {
            val viewName = commandLine.getOptionValue("c")
            createView(workspace, viewName)
            return true
        }

        // Add item to view
        if (commandLine.hasOption("a")) {
            val itemIdStr = commandLine.getOptionValue("a")
            val itemIds = itemIdStr.parseIds().operators
            if (itemIds.isEmpty()) {
                printLine("请输入有效的 item ID")
                return true
            }

            val viewName = commandLine.getOptionValue("v")
            if (viewName != null) {
                // Add to specified view
                addItemToView(workspace, itemIds, viewName)
            } else {
                // Add to current view
                addItemToView(workspace, itemIds)
            }
            return true
        }

        // Exit view mode
        if (commandLine.hasOption("exit")) {
            exitViewMode(workspace)
            return true
        }

        // List all views
        if (commandLine.hasOption("list")) {
            listAllViews(workspace)
            return true
        }

        // Delete view
        if (commandLine.hasOption("delete")) {
            val viewName = commandLine.getOptionValue("delete")
            deleteView(workspace, viewName)
            return true
        }

        // Switch to view (when no option is specified)
        val viewName = originParams.substringAfter("view ").trim()
        if (viewName.isNotEmpty()) {
            switchToView(workspace, app, viewName)
            return true
        }

        // If no valid command, show help
        printHelp()
        return true
    }



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    fun addItemToView(workspace: WorkspaceContext, itemIds: List<Int>, viewName: String) {
        // Check if view exists
        val view = workspace.findVirtualView(viewName)
        if (view == null) {
            printLine("虚拟视图 '$viewName' 不存在")
            return
        }

        var addedCount = 0
        var existsCount = 0

        itemIds.forEach { itemId ->
            val success = workspace.addItemToVirtualView(viewName, itemId)
            if (success) {
                addedCount++
            } else {
                existsCount++
            }
        }

        printLine("操作结果: 成功添加 $addedCount 个事项到视图 '$viewName'，$existsCount 个事项已存在")
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun createView(workspace: WorkspaceContext, viewName: String) {
        if (viewName.isBlank()) {
            printLine("视图名称不能为空")
            return
        }

        val success = workspace.createVirtualView(viewName)
        if (success) {
            printLine("成功创建虚拟视图: $viewName")
        } else {
            printLine("虚拟视图 '$viewName' 已存在")
        }
    }

    private fun addItemToView(workspace: WorkspaceContext, itemIds: List<Int>) {
        val currentView = workspace.getCurrentVirtualView()
        if (currentView == null) {
            printLine("请先切换到一个虚拟视图")
            return
        }

        addItemToView(workspace, itemIds, currentView.name)
    }

    private fun switchToView(workspace: WorkspaceContext, app: CliApp, viewName: String) {
        val success = workspace.switchToVirtualView(viewName)
        if (success) {
            printLine("已切换到虚拟视图: $viewName")
            FocusHandler().onHandle("focus -c", app, workspace)
            ShowHandler().onHandle("show", app, workspace)
        } else {
            printLine("虚拟视图 '$viewName' 不存在")
        }
    }

    private fun exitViewMode(workspace: WorkspaceContext) {
        workspace.exitVirtualView()
        printLine("已退出虚拟视图模式")
    }

    private fun listAllViews(workspace: WorkspaceContext) {
        val views = workspace.virtualViews()
        if (views.views.isEmpty()) {
            printLine("没有找到虚拟视图")
            return
        }

        printLine("虚拟视图列表:")
        views.views.forEachIndexed { index, view ->
            val isCurrent = view.name == views.currentView
            val currentMark = if (isCurrent) " (当前)" else ""
            printLine("${index + 1}. ${view.name}$currentMark (包含 ${view.itemIds.size} 个事项)")
        }
    }

    private fun deleteView(workspace: WorkspaceContext, viewName: String) {
        if (viewName.isBlank()) {
            printLine("视图名称不能为空")
            return
        }

        val success = workspace.deleteVirtualView(viewName)
        if (success) {
            printLine("成功删除虚拟视图: $viewName")
        } else {
            printLine("虚拟视图 '$viewName' 不存在")
        }
    }

}