package com.hipoom.cli.todo.handler.find

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.entity.item.Item
import com.hipoom.cli.todo.entity.item.addChild
import com.hipoom.cli.todo.handler.show.showItemsAsTreeMode
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import com.hipoom.cli.todo.handler.find.findOptions
import com.hipoom.cli.todo.printLine

/**
 * FindHandler 用于查找 content 包含指定关键词的 items 并展示
 *
 * @author ZhengHaiPeng
 */
class FindHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = findOptions

    override val supportPrefixes: List<String> = listOf("find")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description(): String {
        return "Find items"
    }

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            commandLine.hasOption("c") -> { findContentAndShow(commandLine, workspace) }
            commandLine.hasOption("h") -> printHelp()
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

    private fun findContentAndShow(commandLine: CommandLine, workspace: WorkspaceContext) {
        val keyword = commandLine.getOptionValue("c") ?: return
        
        // 加载所有事项
        val allItems = workspace.itemDao().loadAllItems()
        
        // 过滤出content包含关键词的事项
        val foundItems = allItems.filter { item ->
            item.content?.contains(keyword, ignoreCase = true) == true
        }
        
        // 如果没有找到结果，显示提示信息
        if (foundItems.isEmpty()) {
            printLine("没有找到 content 包含 '$keyword' 的事项")
            return
        }
        
        // 构建树状结构并展示结果
        val treeItems = buildTreeFromFlatItems(foundItems)
        workspace.showItemsAsTreeMode(treeItems)
    }

    /**
     * 从扁平的事项列表构建树状结构
     */
    private fun buildTreeFromFlatItems(flatItems: List<Item>): MutableList<Item> {
        // 创建ID到Item的映射
        val idToItem = flatItems.associateBy { it.id!! }
        
        // 找出所有根节点（没有父节点或父节点不在列表中的节点）
        val roots = flatItems.filter { item ->
            item.parentIds.isNullOrEmpty() || 
            !item.parentIds!!.any { parentId -> idToItem.containsKey(parentId) }
        }.toMutableList()
        
        // 构建父子关系
        flatItems.forEach { child ->
            child.parentIds?.forEach { parentId ->
                idToItem[parentId]?.addChild(child)
            }
        }
        
        return roots
    }

}