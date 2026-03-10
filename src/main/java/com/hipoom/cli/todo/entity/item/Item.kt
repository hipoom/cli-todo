package com.hipoom.cli.todo.entity.item

import com.google.gson.annotations.SerializedName
import com.hipoom.cli.todo.utils.appendOrNull
import com.hipoom.cli.todo.utils.toAsterisks



var last_modify_item_id: Int? = null

/**
 * @author ZhengHaiPeng
 * @since 2025/2/2 14:00
 */
class Item {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("parentIds")
    var parentIds: MutableList<Int>? = null

    @SerializedName("content")
    var content: String? = null

    /**
     * @see STATUS_NEW
     * @see STATUS_DOING
     * @see STATUS_DONE
     * @see STATUS_DELETED
     */
    @SerializedName("status")
    var status: String? = null

    @SerializedName("collapseStatus")
    var collapseStatus: String? = null

    @SerializedName("owner")
    var owner: String? = null

    @SerializedName("deadline")
    var deadline: Long? = null

    @SerializedName("labels")
    var labels: MutableList<String>? = null

    @Transient
    var children: MutableList<Item>? = null

    @SerializedName("comments")
    var comments: MutableList<String>? = null

    @SerializedName("needHide")
    var needHide: Boolean? = null

    @SerializedName("isNotified")
    var isNotified: Boolean? = null


    companion object {
        const val STATUS_NEW     = "new"
        const val STATUS_DOING   = "doing"
        const val STATUS_DONE    = "done"
        const val STATUS_DELETED = "deleted"

        const val COLLAPSE_STATUS_COLLAPSE = "collapse"
        const val COLLAPSE_STATUS_EXPAND   = "expand"
    }

    @SerializedName("parentId")
    @Deprecated("除了 migrate 代码，其他地方都不应该再调用这个函数。")
    private val parentId: Int? = null



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    fun getFirstParentIdOrNull(): Int? {
        return parentIds?.firstOrNull()
    }

    fun getParentIdAndIKnownThisIsNotSafe(): Int? {
        return parentId
    }

    fun addParentId(pid: Int) = synchronized(this) {
        var tCatch = this.parentIds
        if (tCatch == null) {
            tCatch = ArrayList()
            this.parentIds = tCatch
        }
        tCatch.add(pid)
    }

    /**
     * 当前节点，可以有多个父节点。
     * 如果父节点中，有 [old]， 则将这个 [old] 移除，并替换为 [pid]。
     */
    fun replaceParentId(old: Int, pid: Int) = synchronized(this) {
        var tCatch = this.parentIds
        if (tCatch == null) {
            tCatch = ArrayList()
            this.parentIds = tCatch
        }
        tCatch.remove(old)
        tCatch.add(pid)
    }


}



fun Item.addChild(child: Item) {
    var temp = children
    if (temp == null) {
        temp = ArrayList()
        children = temp
    }
    temp.add(child)
}

fun Item.addChildren(input: List<Item>) {
    var temp = children
    if (temp == null) {
        temp = ArrayList()
        children = temp
    }
    temp.addAll(input)
}

fun Item.addComment(comment: String) {
    var temp = comments
    if (temp == null) {
        temp = ArrayList()
        comments = temp
    }
    temp.add(comment)
}

fun Item.addLabel(label: String) {
    var temp = labels
    if (temp == null) {
        temp = ArrayList()
        labels = temp
    }
    temp.add(label)
}

fun Item.copyFromAnotherWithoutChildren(item: Item) {
    this.id = item.id
    // this.parentId = item.parentId
    this.content = item.content
    this.status = item.status
    this.owner = item.owner
    this.deadline = item.deadline
    this.comments = item.comments
    this.labels = item.labels
    this.collapseStatus = item.collapseStatus
    this.needHide = item.needHide
}

fun Item.getOwners(): List<String> {
    val tCatch = owner ?: return emptyList()
    return tCatch.split(",").map { it.trim() }
}

fun Item.hasOwner(owner: String): Boolean {
    for (temp in getOwners()) {
        if (temp == owner) {
            return true
        }
    }
    return false
}

fun Item.hasAnyOwners(owners: List<String>): Boolean {
    for (owner in owners) {
        if (hasOwner(owner)) {
            return true
        }
    }

    return false
}

fun Item.getContentCompact(): String {
    if (needHide == true) {
        return content?.toAsterisks()?.appendOrNull(" [hide]") ?: ""
    }

    return content ?: ""
}

/**
 * 创建当前Item的副本
 */
fun Item.copy(
    id: Int? = this.id,
    parentIds: MutableList<Int>? = this.parentIds?.toMutableList(),
    content: String? = this.content,
    status: String? = this.status,
    collapseStatus: String? = this.collapseStatus,
    owner: String? = this.owner,
    deadline: Long? = this.deadline,
    labels: MutableList<String>? = this.labels?.toMutableList(),
    comments: MutableList<String>? = this.comments?.toMutableList(),
    needHide: Boolean? = this.needHide,
    children: MutableList<Item>? = this.children?.map { it.copy() }?.toMutableList()
): Item {
    val newItem = Item()
    newItem.id = id
    newItem.parentIds = parentIds
    newItem.content = content
    newItem.status = status
    newItem.collapseStatus = collapseStatus
    newItem.owner = owner
    newItem.deadline = deadline
    newItem.labels = labels
    newItem.comments = comments
    newItem.needHide = needHide
    newItem.children = children
    return newItem
}

fun Item.isChildOf(expectParentId: Int): Boolean {
    return parentIds?.contains(expectParentId) == true
}