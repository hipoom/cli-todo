package com.hipoom.cli.todo.handler.show

import com.hipoom.cli.core.ui.AsciiTable
import com.hipoom.cli.core.ui.CharWidthCalculator
import com.hipoom.cli.core.ui.Printer
import com.hipoom.cli.core.ui.TextBlockPrinter
import com.hipoom.cli.core.ui.TextStyle
import com.hipoom.cli.core.ui.TextStyleBuilder
import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.defaultTextBlockPrinter
import com.hipoom.cli.todo.entity.item.Item
import com.hipoom.cli.todo.entity.item.getContentCompact
import com.hipoom.cli.todo.entity.item.getOwners
import com.hipoom.cli.todo.handler.group.GroupHandler
import com.hipoom.cli.todo.handler.style.Styles
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.utils.displayWidth
import com.hipoom.cli.todo.utils.number2Subscript
import com.hipoom.cli.todo.utils.parseIds
import com.hipoom.cli.workspace.WorkspaceContext
import com.hipoom.holder.ListValueMap
import java.text.SimpleDateFormat
import java.util.LinkedList
import kotlin.math.max
import kotlin.math.round

/**
 * 遍历当前节点的所有子节点（不含当前节点！）
 */
fun dfsChildren(item: Item?, callback: (Item)->Unit) {
    TraversalUtils.dfsTraverseChildren(item, callback)
}

/**
 * 遍历整个 workspace 的所有事项。
 */
fun dfsWorkspace(items: List<Item>?, callback: (Item)->Unit) {
    TraversalUtils.dfsTraverseWorkspace(items, callback)
}

/**
 * 显示自动模式
 */
internal fun showAutoMode(
    workspace: WorkspaceContext,
    items: MutableList<Item>?
) {
    ShowModeUtils.showAutoMode(workspace, items)
}

/**
 * 以所有者模式显示
 */
internal fun showAsWhoMode(
    workspace: WorkspaceContext,
    items: MutableList<Item>?
) {
    ShowModeUtils.showAsOwnerMode(workspace, items)
}

/**
 * 构建父模式的树状结构行
 */
internal fun buildAsParentMode(
    workspace: WorkspaceContext,
    indent: String,
    item: Item,
    isLastOne: Boolean
): MutableList<TreeModeRow> {
    return BuildUtils.buildParentModeTree(workspace, indent, item, isLastOne)
}

/**
 * 清理指定状态的子事项
 */
internal fun cleanChildrenWithStatus(items: MutableList<Item>?, status: String) {
    CleanUtils.cleanChildrenByStatus(items, status)
}

/**
 * 获取事项 ID 描述
 */
internal fun getIdDes(workspace: WorkspaceContext, item: Item, forceShowId: Boolean = false): String {
    return DescriptionUtils.getItemIdDescription(workspace, item, forceShowId)
}

/**
 * 获取事项状态图标
 */
internal fun getStatusIcon(workspace: WorkspaceContext, item: Item,): String {
    return DescriptionUtils.getItemStatusIcon(workspace, item)
}

/**
 * 获取所有者描述
 */
fun getOwnerDes(item: Item): String {
    return DescriptionUtils.getOwnerDescription(item)
}

/**
 * 获取事项标签
 */
fun getLabels(item: Item): String {
    return DescriptionUtils.getItemLabels(item)
}

/**
 * 获取折叠指示器
 */
fun getCollapseFlag(item: Item): String {
    return DescriptionUtils.getCollapseIndicator(item)
}

/**
 * 获取事项截止日期
 */
fun getDeadline(item: Item): String {
    return DescriptionUtils.getItemDeadline(item)
}

/**
 * 向字符串末尾添加空格以达到目标长度
 */
fun String.appendEmpty(targetLength: Int): String {
    return this.appendSpaces(targetLength)
}

/**
 * 显示固定的事项
 */
fun WorkspaceContext.showPins(items: List<Item>?) {
    this.showPinnedItems(items)
}

/**
 * 将事项转换为简单的行显示
 */
private fun Item.toSimpleLine(): TreeModeRow {
    return this.toSimpleRow()
}

/**
 * 遍历工具函数
 */
object TraversalUtils {
    /**
     * 深度优先遍历当前节点的所有子节点（不含当前节点）
     * @param item 当前节点
     * @param callback 遍历回调函数
     */
    fun dfsTraverseChildren(item: Item?, callback: (Item) -> Unit) {
        item?.children?.forEach {
            callback.invoke(it)
            dfsTraverseChildren(it, callback)
        }
    }

    /**
     * 深度优先遍历整个工作区的所有事项
     * @param items 事项列表
     * @param callback 遍历回调函数
     */
    fun dfsTraverseWorkspace(items: List<Item>?, callback: (Item) -> Unit) {
        items?.forEach {
            callback(it)
            dfsTraverseChildren(it, callback)
        }
    }
}

/**
 * 显示模式相关函数
 */
object ShowModeUtils {
    /**
     * 根据当前模式自动选择显示方式
     * @param workspace 工作区上下文
     * @param items 事项列表
     */
    internal fun showAutoMode(
        workspace: WorkspaceContext,
        items: MutableList<Item>?
    ) {
        val isOwnerMode = GroupHandler.isOwnerMode(workspace)
        if (isOwnerMode) {
            showAsOwnerMode(items = items, workspace = workspace)
        } else {
            workspace.showItemsAsTreeMode(items)
        }
    }

    /**
     * 以所有者模式显示事项
     * @param workspace 工作区上下文
     * @param items 事项列表
     */
    internal fun showAsOwnerMode(
        workspace: WorkspaceContext,
        items: MutableList<Item>?
    ) {
        val itemsByOwner = ListValueMap<String?, Item>()
        TraversalUtils.dfsTraverseWorkspace(items) { item ->
            val owners = item.getOwners()
            if (owners.isEmpty()) {
                itemsByOwner.insert(null, item)
            } else {
                owners.forEach { owner ->
                    itemsByOwner.insert(owner, item)
                }
            }
        }

        printLine("")
        itemsByOwner.forEach { (ownerName, itemList) ->
            printLine("\uD83D\uDC64 ${ownerName ?: "未指定"}:")

            itemList.forEachIndexed { index, item ->
                val statusIcon = DescriptionUtils.getItemStatusIcon(workspace, item)
                val connector = if (index == itemList.size - 1) "`--" else "|--"
                printLine("${DescriptionUtils.getItemIdDescription(workspace, item)}$statusIcon $connector ${item.getContentCompact()}" + DescriptionUtils.getItemLabels(item))
            }
            printLine()
        }
    }
}

/**
 * 构建相关函数
 */
object BuildUtils {
    /**
     * 构建父模式的树状结构行
     * @param workspace 工作区上下文
     * @param indent 缩进字符串
     * @param item 当前事项
     * @param isLastOne 是否是最后一个子节点，影响连接线样式
     * @return 构建的树状结构行列表
     */
    internal fun buildParentModeTree(
        workspace: WorkspaceContext,
        indent: String,
        item: Item,
        isLastOne: Boolean
    ): MutableList<TreeModeRow> {
        val rows = ArrayList<TreeModeRow>()

        val idDescription = DescriptionUtils.getItemIdDescription(workspace, item)
        val connector = if (isLastOne) "`--" else "|--"
        rows.add(
            TreeModeRow(
                id = idDescription,
                status = DescriptionUtils.getItemStatusIcon(workspace, item),
                indent_and_connector = "$indent $connector ",
                contentWithIndent = "$indent $connector " + item.getContentCompact(),
                collapseStatus = DescriptionUtils.getCollapseIndicator(item),
                owners = DescriptionUtils.getOwnerDescription(item),
                labels = DescriptionUtils.getItemLabels(item),
                deadline = DescriptionUtils.getItemDeadline(item),
                item = item
            )
        )
        
        // 检查是否需要折叠子节点
        val needCollapse = (item.collapseStatus == Item.COLLAPSE_STATUS_COLLAPSE)
        if (!needCollapse) {
            item.children?.forEachIndexed { index, child ->
                val isLastChild = (index == (item.children?.size ?: 0) - 1)
                val childRows = buildParentModeTree(workspace, "$indent .  ", child, isLastChild)
                rows.addAll(childRows)
            }
        }

        return rows
    }
}

/**
 * 清理相关函数
 */
object CleanUtils {
    /**
     * 清理指定状态的子事项
     * @param items 事项列表
     * @param status 要清理的状态
     */
    internal fun cleanChildrenByStatus(items: MutableList<Item>?, status: String) {
        if (items == null) {
            return
        }

        val iterator = items.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.status == status) {
                iterator.remove()
                continue
            }

            cleanChildrenByStatus(item, status)
        }
    }

    /**
     * 清理指定状态的子事项
     * @param item 父事项
     * @param status 要清理的状态
     */
    private fun cleanChildrenByStatus(item: Item, status: String) {
        val children = item.children ?: return
        val iterator = children.iterator()
        while (iterator.hasNext()) {
            val child = iterator.next()
            if (child.status == status) {
                iterator.remove()
                continue
            }
            cleanChildrenByStatus(child, status)
        }
    }
}

/**
 * 描述相关函数
 */
object DescriptionUtils {
    /**
     * 查询是否需要显示状态
     * @param workspace 工作区上下文
     * @return 是否需要显示状态
     */
    private fun queryNeedShowStatus(workspace: WorkspaceContext): Boolean {
        return Configs.show.needShowStatus
    }

    /**
     * 获取事项 ID 描述
     * @param workspace 工作区上下文
     * @param item 事项
     * @param forceShowId 是否强制显示 ID
     * @return ID 描述字符串
     */
    internal fun getItemIdDescription(workspace: WorkspaceContext, item: Item, forceShowId: Boolean = false): String {
        if (!forceShowId && !Configs.show.needShowId) {
            return ""
        }

        val maxIdLength = workspace.itemDao().maxIndex().toString().length
        val idDisplay = when (maxIdLength) {
            1 -> "[${item.id}] "
            2 -> when (item.id) {
                in 0..9 -> "[ ${item.id}]"
                else -> "[${item.id}]"
            }
            3 -> when (item.id) {
                in 0..9 -> "[  ${item.id}]"
                in 10..99 -> "[ ${item.id}]"
                else -> "[${item.id}]"
            }
            else -> "[${item.id}]"
        }

        return idDisplay
    }

    /**
     * 获取事项状态图标
     * @param workspace 工作区上下文
     * @param item 事项
     * @return 状态图标字符串
     */
    internal fun getItemStatusIcon(workspace: WorkspaceContext, item: Item): String {
        val needShowStatus = queryNeedShowStatus(workspace)
        if (!needShowStatus) {
            return ""
        }

        val status = Configs.show.status.get(item.status ?: Item.STATUS_NEW)
        return "[$status]"
    }

    /**
     * 获取所有者描述
     * @param item 事项
     * @return 所有者描述字符串
     */
    fun getOwnerDescription(item: Item): String {
        if (!Configs.show.needShowOwner) {
            return ""
        }

        if (item.owner.isNullOrEmpty()) {
            return ""
        }

        return Configs.show.icon.get("owner") + " " + item.owner
    }

    /**
     * 获取事项标签
     * @param item 事项
     * @return 标签描述字符串
     */
    fun getItemLabels(item: Item): String {
        if (!Configs.show.needShowLabel) {
            return ""
        }

        if (item.labels.isNullOrEmpty()) {
            return ""
        }

        return Configs.show.icon.get("label") + " " + item.labels!!.joinToString { it }
    }

    /**
     * 获取折叠指示器
     * @param item 事项
     * @return 折叠指示器字符串
     */
    fun getCollapseIndicator(item: Item): String {
        val isCollapsed = (item.collapseStatus == Item.COLLAPSE_STATUS_COLLAPSE)
        // 状态是已折叠，且有可见子节点
        if (isCollapsed && item.hasVisibleChild()) {
            return "[+]"
        }
        return ""
    }

    /**
     * 获取事项截止日期
     * @param item 事项
     * @return 截止日期字符串
     */
    fun getItemDeadline(item: Item): String {
        if (!Configs.show.needShowDeadline) {
            return ""
        }
        if (item.deadline == null) {
            return ""
        }
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.deadline)
    }
}

/**
 * 将事项转换为简单的行显示
 */
private fun Item.toSimpleRow(): TreeModeRow {
    return TreeModeRow(
        id = "[${this.id}]",
        status = "",
        indent_and_connector = "",
        contentWithIndent = getContentCompact(),
        collapseStatus = "",
        owners = DescriptionUtils.getOwnerDescription(this),
        labels = DescriptionUtils.getItemLabels(this),
        deadline = DescriptionUtils.getItemDeadline(this),
        item = this
    )
}

/**
 * 获取评论数量的下标表示
 * @return 评论数量的下标字符串
 */
fun TreeModeRow.commentSubscript(): String {
    if (!Configs.show.needShowCommentSubscript) {
        return ""
    }

    val commentCount = this.item.comments?.size ?: 0
    if (commentCount <= 0) {
        return ""
    }

    val subscript = number2Subscript(commentCount)
    return " ᶜ$subscript"
}

/**
 * 显示树状模式行列表
 * @param textStyle 文本样式
 */
fun List<TreeModeRow>.show(textStyle: TextStyle? = null) {
    val printer = defaultTextBlockPrinter

    fun printLine(line: String) {
        if (textStyle == null) {
            com.hipoom.cli.todo.printLine(line)
        } else {
            printer.printLine(indent = 0, text = line, maxWidth = 10000, textStyle)
        }
    }

    if (!Configs.show.useAlignMode) {
        forEach { row ->
            val line = (row.id + " " + row.status + row.contentWithIndent + row.commentSubscript() + " " + row.collapseStatus + " " + row.owners + " " + row.labels + " " + row.deadline)
            printLine(line)

            val commentIndent = line.indexOf("-- ") + 3
            
            // 打印评论
            if (Configs.show.needShowComment && !row.item.comments.isNullOrEmpty()) {
                row.item.comments?.forEachIndexed { index, comment ->
                    val indent = row.indent_and_connector.length
                    printer.printLine(indent = commentIndent, text = "[${index + 1}] $comment", style = Styles.getCurrentStyle().getCommentBlockStyle())
                }
            }

        }
        return
    }

    var maxContentLength = 0
    var maxCollapseStatusLength = 0
    var maxOwnerLength = 0
    var maxLabelLength = 0

    forEach { row ->
        maxContentLength = max(maxContentLength, row.contentWithIndent.displayWidth())
        maxCollapseStatusLength = max(maxCollapseStatusLength, row.collapseStatus.displayWidth())
        maxOwnerLength = max(maxOwnerLength, row.owners.displayWidth())
        maxLabelLength = max(maxLabelLength, row.labels.displayWidth())
    }

    forEach { row ->
        val contentAndCommentSubscript = row.contentWithIndent + row.commentSubscript()
        val line = (
            row.id
            + " "
            + row.status
            + contentAndCommentSubscript.appendSpaces(maxContentLength)
            + " "
            + row.collapseStatus.appendSpaces(maxCollapseStatusLength)
            + "  "
            + row.owners.appendSpaces(maxOwnerLength)
            + "  "
            + row.labels.appendSpaces(maxLabelLength)
            + "  "
            + row.deadline
        )

        val commentIndent = line.indexOf("-- ") + 3

        printLine(line)

        // 打印评论
        if (Configs.show.needShowComment && !row.item.comments.isNullOrEmpty()) {
            row.item.comments?.forEachIndexed { index, comment ->
                printer.printLine(indent = commentIndent, text = "[${index + 1}] $comment", style = Styles.getCurrentStyle().getCommentBlockStyle())
            }
        }
    }
}

/**
 * 向字符串末尾添加空格以达到目标长度
 * @param targetLength 目标长度
 * @return 添加空格后的字符串
 */
fun String.appendSpaces(targetLength: Int): String {
    val sb = StringBuilder(this)
    val spacesNeeded = targetLength - displayWidth()
    for (i in 0 until spacesNeeded) {
        sb.append(' ')
    }
    return sb.toString()
}

/**
 * 检查事项是否有可见的子节点
 * @return 是否有可见子节点
 */
fun Item.hasVisibleChild(): Boolean {
    val childrenList = children
    if (childrenList.isNullOrEmpty()) {
        return false
    }

    childrenList.forEach { child ->
        if (child.status == Item.STATUS_NEW || child.status == Item.STATUS_DOING) {
            return true
        }

        if (child.status == Item.STATUS_DONE && Configs.show.needShowDone) {
            return true
        }

        if (child.status == Item.STATUS_DELETED && Configs.show.needShowDeleted) {
            return true
        }
    }

    return false
}

/**
 * 以树状模式显示事项列表
 * @param items 事项列表
 */
fun WorkspaceContext.showItemsAsTreeMode(items: List<Item>?) {
    showPinnedItems(items)

    // 构建并显示树状结构
    val rows = LinkedList<TreeModeRow>()
    items?.forEachIndexed { index, item ->
        val isLastItem = index == items.size - 1
        val itemRows = BuildUtils.buildParentModeTree(this, "", item, isLastItem)
        rows.addAll(itemRows)
    }
    rows.show()
}

/**
 * 显示固定的事项
 * @param items 事项列表
 */
fun WorkspaceContext.showPinnedItems(items: List<Item>?) {
    val pinnedIds = database().query("pins").parseIds().operators
    if (pinnedIds.isEmpty()) {
        return
    }
    val pinnedItems = items?.filter { pinnedIds.contains(it.id) }
    if (pinnedItems.isNullOrEmpty()) {
        return
    }
    printLine("-----")
    pinnedItems.map {
        it.toSimpleRow()
    }.show(
        textStyle = TextStyleBuilder()
            .backgroundColor(Styles.getCurrentStyle().pinBackgroundColor)
            .build()
    )
    printLine("-----")
}

/**
 * 显示事项列表
 */
fun CliApp.show() {
    ShowHandler().onHandle("show", app = this, getCurrentWorkspace())
}

/**
 * 树状模式下，打印时的每一行信息
 */
class TreeModeRow(
    val id: String,
    val status: String,
    val indent_and_connector: String,
    val contentWithIndent: String,
    val collapseStatus: String,
    val owners: String,
    val labels: String,
    val deadline: String,
    val item: Item,
)