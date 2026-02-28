package com.hipoom.cli.todo.handler.show

import com.hipoom.cli.core.ui.AsciiTable
import com.hipoom.cli.core.ui.CharWidthCalculator
import com.hipoom.cli.core.ui.Printer
import com.hipoom.cli.core.ui.TextBlockPrinter
import com.hipoom.cli.core.ui.TextStyle
import com.hipoom.cli.core.ui.TextStyleBuilder
import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.entity.item.Item
import com.hipoom.cli.todo.entity.item.getContentCompact
import com.hipoom.cli.todo.entity.item.getOwners
import com.hipoom.cli.todo.handler.group.GroupHandler
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
    item?.children?.forEach {
        callback.invoke(it)
        dfsChildren(it, callback)
    }
}

/**
 * 遍历整个 workspace 的所有事项。
 */
fun dfsWorkspace(items: List<Item>?, callback: (Item)->Unit) {
    items?.forEach {
        callback(it)
        dfsChildren(it, callback)
    }
}


internal fun showAutoMode(
    workspace: WorkspaceContext,
    items: MutableList<Item>?
) {
    val isWhoMode = GroupHandler.isOwnerMode(workspace)
    if (isWhoMode) {
        showAsWhoMode(items = items, workspace = workspace)
    }
    else {
        workspace.showItemsAsTreeMode(items)
    }
}

/**
 * @param isLastOne 是否是最后一个。 如果是最后一个，打印时用 `-- 而不是 |--
 */
internal fun buildAsParentMode(
    workspace: WorkspaceContext,
    indent: String,
    item: Item,
    isLastOne: Boolean
): MutableList<TreeModeRow> {
    val rows = ArrayList<TreeModeRow>()

    val idDes = getIdDes(workspace, item)
    val temp = if (isLastOne) "`--" else "|--"
    rows.add(
        TreeModeRow(
            id = idDes,
            status = getStatusIcon(workspace, item),
            contentWithIndent = "$indent $temp " + item.getContentCompact(),
            collapseStatus = getCollapseFlag(item),
            owners = getOwnerDes(item),
            labels = getLabels(item),
            deadline = getDeadline(item),
            item = item
        )
    )
    // 是否需要折叠所有子节点
    val needCollapse = (item.collapseStatus == Item.COLLAPSE_STATUS_COLLAPSE)
    if (!needCollapse) {
        item.children?.forEachIndexed { index, child ->
            val isLastChild = (index == (item.children?.size ?: 0) - 1)
            val childRes = buildAsParentMode(workspace, "$indent .  ", child, isLastChild)
            rows.addAll(childRes)
        }
    }

    return rows
}

private fun Item.toSimpleLine(): TreeModeRow {
    return TreeModeRow(
        id = "[${this.id}]",
        status = "",
        contentWithIndent = getContentCompact(),
        collapseStatus = "",
        owners = getOwnerDes(this),
        labels = getLabels(this),
        deadline = getDeadline(this),
        item = this
    )
}

internal fun showAsWhoMode(
    workspace: WorkspaceContext,
    items: MutableList<Item>?
) {
    val personals = ListValueMap<String?, Item>()
    dfsWorkspace(items) { item ->
        val owners = item.getOwners()
        if (owners.isEmpty()) {
            personals.insert(null, item)
        } else {
            owners.forEach { owner ->
                personals.insert(owner, item)
            }
        }
    }

    printLine("")
    personals.forEach { (name, list) ->
        printLine("\uD83D\uDC64 ${name ?: "未指定"}:")

        list.forEachIndexed { index, item ->
            val state = getStatusIcon(workspace, item)
            val temp =
            if (index == list.size -1) {
                "`--"
            }
            else {
                "|--"
            }
            printLine("${getIdDes(workspace, item)}$state $temp ${item.getContentCompact()}" + getLabels(item))
        }
        printLine()
    }
}

internal fun cleanChildrenWithStatus(items: MutableList<Item>?, status: String) {
    if (items == null) {
        return
    }

    val it = items.iterator()
    while (it.hasNext()) {
        val item = it.next()
        if (item.status == status) {
            it.remove()
            continue
        }

        cleanChildrenWithStatus(item, status)
    }
}

private fun cleanChildrenWithStatus(item: Item, status: String) {
    val it = item.children?.iterator() ?: return
    while (it.hasNext()) {
        val child = it.next()
        if (child.status == status) {
            it.remove()
            continue
        }
        cleanChildrenWithStatus(child, status)
    }
}

internal fun getIdDes(workspace: WorkspaceContext, item: Item, forceShowId: Boolean = false): String {
    if (!forceShowId && !Configs.show.needShowId) {
        return ""
    }

    val maxIdLength = workspace.itemDao().maxIndex().toString().length
    val t = when(maxIdLength) {
        1 -> "[${item.id}] "
        2 -> when(item.id) {
            in 0..9    -> "[ ${item.id}]"
            else            -> "[${item.id}]"
        }
        3 -> when(item.id) {
            in 0..9    -> "[  ${item.id}]"
            in 10..99  -> "[ ${item.id}]"
            else            -> "[${item.id}]"
        }
        else  -> "[${item.id}]"
    }

    return t
}

internal fun getStatusIcon(workspace: WorkspaceContext, item: Item,): String {
    val needShowStatus = queryNeedShowStatus(workspace)
    if (!needShowStatus) {
        return ""
    }

    val state = Configs.show.status.get(item.status ?: Item.STATUS_NEW)

    return "[$state]"
}

fun getOwnerDes(item: Item): String {
    if (!Configs.show.needShowOwner) {
        return ""
    }

    if (item.owner.isNullOrEmpty()) {
        return ""
    }

    //🕴️   👤
    return Configs.show.icon.get("owner") + " " + item.owner
}

fun getLabels(item: Item): String {
    if (!Configs.show.needShowLabel) {
        return ""
    }

    if (item.labels.isNullOrEmpty()) {
        return ""
    }

    return Configs.show.icon.get("label") + " " + item.labels!!.joinToString { it }
}

fun getCollapseFlag(item: Item): String {
    val needCollapse = (item.collapseStatus == Item.COLLAPSE_STATUS_COLLAPSE)
    // 状态是已折叠，且没有子节点
    if (needCollapse && item.hasVisibleChild()) {
        return "[+]"
    }
    return ""
}

/**
 * 是否有可见的子节点。
 */
fun Item.hasVisibleChild(): Boolean {
    val tCatch = children
    if (tCatch.isNullOrEmpty()) {
        return false
    }

    tCatch.forEach { child ->
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

fun CliApp.show() {
    ShowHandler().onHandle("show", app = this, getCurrentWorkspace())
}

/**
 * Tree 模式下，打印时的每一行信息。
 */
class TreeModeRow(
    val id: String,
    val status: String,
    val contentWithIndent: String,
    val collapseStatus: String,
    val owners: String,
    val labels: String,
    val deadline: String,
    val item: Item
)

fun TreeModeRow.commentSubscript(): String {
    if (!Configs.show.needShowCommentSubscript) {
        return ""
    }

    val commentSize = this.item.comments?.size ?: 0
    if (commentSize <= 0) {
        return ""
    }

    val temp = number2Subscript(commentSize)
    return " ᶜ$temp"
}

fun List<TreeModeRow>.show(textStyle: TextStyle? = null) {
    val printer = TextBlockPrinter(
        printer = com.hipoom.cli.todo.Main.printer,
        charWidthCalculator = object : CharWidthCalculator {
            override fun calculate(text: String): Int {
                return text.displayWidth()
            }
        }
    )

    if (!Configs.show.useAlignMode) {
        forEach { row ->
            val line = (row.id + " " + row.status + row.contentWithIndent + row.commentSubscript() + " " + row.collapseStatus + " " + row.owners + " " + row.labels + " " + row.deadline)
            if (textStyle == null) {
                printLine(line)
            }
            else {
                printer.print(0, 100000, line, textStyle)
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
          + contentAndCommentSubscript.appendEmpty(maxContentLength)
          + " "
          + row.collapseStatus.appendEmpty(maxCollapseStatusLength)
          + "  "
          + row.owners.appendEmpty(maxOwnerLength)
          + "  "
          + row.labels.appendEmpty(maxLabelLength)
          + "  "
          + row.deadline
        )
        if (textStyle == null) {
            printLine(line)
        }
        else {
            printer.print(0, 100000, line, textStyle)
        }
    }

}

fun String.appendEmpty(targetLength: Int): String {
    val sb = StringBuilder(this)
    val size = targetLength - displayWidth()
    for (i in 0 until size) {
        sb.append(' ')
    }
    return sb.toString()
}

/**
 * @param items 非 tree 结构的事项列表。
 */
fun WorkspaceContext.showItemsAsTreeMode(items: List<Item>?) {
    showPins(items)

    // 逐个展示每个事项
    val rows = LinkedList<TreeModeRow>()
    items?.forEachIndexed { index, item ->
        val isLastOne = index == items.size - 1
        val temp = buildAsParentMode(this, "", item, isLastOne)
        rows.addAll(temp)
    }
    rows.show()
}

fun getDeadline(item: Item): String {
    if (!Configs.show.needShowDeadline) {
        return ""
    }
    if (item.deadline == null) {
        return ""
    }
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.deadline)
}

fun WorkspaceContext.showPins(items: List<Item>?) {
    val ids = database().query("pins").parseIds().operators
    if (ids.isEmpty()) {
        return
    }
    printLine("-----")
    val pins = items?.filter { ids.contains(it.id) }
    pins?.map {
        it.toSimpleLine()
    }?.show(
        textStyle = TextStyleBuilder()
            .backgroundColor(200, 200, 200)
            .build()
    )
    printLine("-----")
}