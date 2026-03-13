@file:Suppress("MemberVisibilityCanBePrivate")

package com.hipoom.cli.todo.entity.item

import com.hipoom.cli.todo.gson
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.utils.containsAny
import java.io.File
import java.util.LinkedList

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 12:41
 */
class ItemDao(private val filePath: String) {

    /* ======================================================= */
    /* Public Methods - Insert                                 */
    /* ======================================================= */

    /**
     * 插入新元素。
     */
    fun insert(item: Item): List<Int> {
        return insert(item, item.parentIds ?: LinkedList<Int>())
    }

    /**
     * 将一个 [item] 加入到 [parentIds] 指定的多个父节点下方。
     * 注意：插入的 [item] 是复制多份，分别做为独立的 item，各自插入到多个 parent 下面。
     * 和 link 不一样。
     */
    fun insert(item: Item, parentIds: List<Int>): List<Int> {
        return callWithTable { table ->
            val ids = ArrayList<Int>()

            // 遍历每一个父节点 id，
            // 如果 parentIds 是空的，也需要添加，那么需要给 parentIds 补充一个 null 节点。
            val tempParentIds = parentIds.ifEmpty { listOf(null) }
            tempParentIds.forEach { parentId ->
                // 主键赋值
                val nextId = table.maxID + 1
                table.maxID = nextId

                // 负责人
                val owner: String? = item.owner ?: if (parentId != null) {
                    table.ensureItems().find { it.id == parentId }?.owner
                } else null

                // 复制一份
                val copy = if (parentId == null) {
                    item.copy(id = nextId, parentIds = mutableListOf(), owner = owner)
                } else {
                    item.copy(id = nextId, parentIds = mutableListOf(parentId), owner = owner)
                }

                // 加入到列表中
                table.ensureItems().add(copy)
                ids.add(nextId)
            }

            // 返回刚插入的元素的 id
            return@callWithTable ids
        }
    }



    /* ======================================================= */
    /* Public Methods - Update                                 */
    /* ======================================================= */

    /**
     * 更新指定 id 的项目的状态。
     */
    fun updateStatus(status: String, ids: List<Int>) {
        foreach(ids) { it.status = status }
        last_modify_item_id = ids.last()
    }

    /**
     * 更新指定 id 的项目的折叠状态。
     */
    fun updateCollapseStatus(status: String?, ids: List<Int>) {
        foreach(ids) {
            it.collapseStatus = status
        }
        last_modify_item_id = ids.last()
    }

    /**
     * 更新所有项目的折叠状态。
     */
    fun updateAllCollapseStatus(status: String?) {
        runWithTable { table ->
            table.ensureItems().forEach { item ->
                item.collapseStatus = status
            }
        }
    }

    /**
     * 将 [ids] 对应的多个事项，移动到 [newParentIds] 中。
     */
    fun move(newParentIds: List<Int>, ids: List<Int>) {
        foreach(ids) {
            it.parentIds = newParentIds.toMutableList()
        }
        last_modify_item_id = ids.last()
    }

    /**
     * 将所有节点都往上移动一个层级。
     *
     * @param ids 待移动的事项列表。
     */
    fun moveUp(ids: List<Int>) {
        // 读取现有数据
        val table = load(false)
        val items = table.ensureItems()

        // 遍历每一个 id
        ids.forEach { id ->
            val item = items.find { it.id == id }
            if (item == null) {
                return@forEach
            }

            // 找到每一个事项的父节点们；
            // 这里面，每个子节点都可能会有多个父节点
            val oldParents = items.filter {
                item.parentIds?.contains(it.id) == true
            }

            // 然后对每一个旧的父节点，统计其父节点们。
            // 也就是原来 [ids] 对应的爷爷们
            val grandfathers = LinkedList<Int>()
            oldParents.forEach { oldParent ->
                oldParent.parentIds?.also {
                    grandfathers.addAll(it)
                }
            }

            item.parentIds = grandfathers
        }

        // 保存到磁盘
        store(table)
        last_modify_item_id = ids.last()
    }

    /**
     * 使用某一个事项，用完自动保存。
     */
    fun useItem(id: Int, ifNotFound: (()->Unit)? = null, onFound: (Item) -> Unit) {
        find(
            predication = {
                it.id == id
            },
            block = {
                val item = it.firstOrNull()
                if (item == null) {
                    ifNotFound?.invoke()
                }
                else {
                    onFound.invoke(item)
                }
            }
        )
        last_modify_item_id = id
    }

    /**
     * 判断 [ids] 对应的所有事项是否都已经都标记为删除了。
     */
    fun isAllDeleted(ids: List<Int>): Boolean {
        val table = load(false)
        val found = table.ensureItems().filter { ids.contains(it.id) }.find { it.status != Item.STATUS_DELETED }
        return found == null
    }

    /**
     * 加载 [id] 对应的事项，并填充 children 及其所有递归子节点。
     * 但是和 [useChildren] 不同， [loadAsTree] 在执行 [onFound] 之后，并不会保存修改。
     */
    fun loadAsTree(id: Int, ifNotFound: (() -> Unit)? = null, onFound: (Item) -> Unit) {
        val table = load(true)
        val item = table.items?.find { it.id == id }
        if (item == null) {
            ifNotFound?.invoke()
        }
        else {
            onFound(item)
        }
    }

    /**
     * 加载 [id] 对应的事项，并填充 children 及其所有递归子节点。
     * 在执行 [onFound] 之后，会保存修改。
     * 注意： [onFound] 回调的 items 不包含 id 对应的事项本身，只含有其子事项。
     */
    fun useChildren(id: Int, onFound: (List<Item>) -> Unit) {
        val table = load(false)
        val items = table.ensureItems()
        val item = items.find { it.id == id }
        if (item == null) {
            return
        }

        buildParentChildRelationships(items)
        onFound(item.children ?: emptyList())
        store(table)
        last_modify_item_id = id
    }

    /**
     * 仅以 tree 的方式加载事项。
     */
    fun loadAsTree(): MutableList<Item> {
        val table = load(needBuildTree = true)
        val tree = table.ensureItems()

        // 移除所有有父节点的事项。
        tree.removeIf { item ->
            // 如果没有父节点，说明是根节点，不能移除
            if (item.parentIds.isNullOrEmpty()) {
                return@removeIf false
            }

            // 如果只有一个父节点，且父节点是 0，为了后续的兼容，也认为是没有父节点；
            // 后续可能会把 id = 0 的节点认作是虚拟的根节点。
            if (item.parentIds!!.size == 1 && item.parentIds!!.first() == 0) {
                return@removeIf false
            }

            return@removeIf true
        }
        
        // 对根节点按 order 排序（order 为 null 时按 id 排序）
        tree.sortBy { it.order ?: it.id ?: 0 }
        
        return tree
    }

    /**
     * 查询含有指定 label 的事项。
     */
    fun findItemsWithLabel(label: String): MutableList<Item> {
        val table = load(false)
        val items = table.ensureItems()
        val found = items.filter { it.labels?.contains(label) ?: false }.toMutableList()
        return found
    }

    /**
     * 寻找 [owners] 对应的所有事项。
     */
    fun findItemsWithOwners(owners: List<String>): List<Item> {
        val table = load(false)
        val items = table.ensureItems()
        val found = items.filter { it.hasAnyOwners(owners) }.toMutableList()
        return found
    }

    fun maxIndex(): Int {
        val table = load()
        return table.maxID
    }

    fun loadAllItems(): List<Item> {
        val table = load(false)
        return table.ensureItems()
    }

    /**
     * 将带有指定任意 label 的事项全部标记为隐藏。
     */
    fun markHideWithLabels(labels: List<String>) {
        find(
            predication = { item ->
                item.labels?.containsAny(labels) == true
            },
            block = { items ->
                items.forEach { item ->
                    item.needHide = true
                }
            }
        )
    }

    /**
     * 将带有指定任意 label 的事项全部标记为隐藏。
     */
    fun cancelHideWithLabels(labels: List<String>) {
        find(
            predication = { item ->
                item.labels?.containsAny(labels) == true
            },
            block = { items ->
                items.forEach { item ->
                    item.needHide = false
                }
            }
        )
    }

    /**
     * 物理删除 [ids] 对应的所有事项。
     */
    fun deletePhysical(ids: List<Int>) {
        val table = load(false)
        val items = table.ensureItems()
        items.removeIf {
            ids.contains(it.id)
        }
        store(table)
        last_modify_item_id = ids.last()
    }

    /**
     * 查询指定 id 的元素，及其所有的子元素。
     * 结果是平铺的，而不是 tree 模式。
     */
    fun findItemAndHisChildrenRecursively(ids: List<Int>): List<Item> {
        val table = load(false)
        val items = table.ensureItems()
        val roots = items.filter { ids.contains(it.id) }
        val res = ArrayList<Item>()
        res.addAll(roots)

        roots.forEach { item ->
            fullChildrenRecursively(
                all = items,
                parent = item
            )
        }

        return res
    }

    /**
     * 给多个事项添加相同的 label.
     */
    fun addLabel(label: String, ids: List<Int>) {
        useItems(
            ids = ids,
            block = { items ->
                items.forEach { item ->
                    item.addLabel(label)
                }
            }
        )
        last_modify_item_id = ids.last()
    }

    /**
     * 获取 [id] 节点平级的下一个兄弟节点，
     */
    fun next(id: Int): Item? {
        val brothers = findBrothers(id)
        var current: Item? = null
        for (brother in brothers) {
            if (brother.id == id) {
                current = brother
                continue
            }

            if (current != null) {
                return brother
            }
        }
        return null
    }

    /**
     * 获取 [id] 节点平级的上一个兄弟节点，
     */
    fun prev(id: Int): Item? {
        val brothers = findBrothers(id)
        var prev: Item? = null
        for (brother in brothers) {
            if (brother.id == id) {
                return prev
            }

            prev = brother
        }
        return null
    }

    /**
     * 如果 [id] 对应的事项的所有子节点，都已经折叠了，那么折叠 [id] 自己，否则，折叠所有可折叠的子节点。
     */
    fun smartCollapse(id: Int) {
        val table = load(needBuildTree = true)
        val items = table.ensureItems()

        val children = items.filter { it.parentIds?.contains(id) == true }
        var hasExpandChild = false
        for (child in children) {
            // 如果没有子节点，忽略
            if (child.children.isNullOrEmpty()) {
                continue
            }

            // 有子节点，且不是折叠状态
            if (child.collapseStatus != Item.COLLAPSE_STATUS_COLLAPSE) {
                // 改为折叠状态
                child.collapseStatus = Item.COLLAPSE_STATUS_COLLAPSE
                // 标记为有展开的子事项
                hasExpandChild = true
            }
        }

        // 如果有展开的子节点被折叠起来，那么就不折叠 id 对应的事项了
        if (hasExpandChild) {
            store(table)
            return
        }

        // 否则，折叠 id 对应的事项
        val current = items.find { it.id == id }
        current?.collapseStatus = Item.COLLAPSE_STATUS_COLLAPSE
        store(table)
        last_modify_item_id = id
    }

    /**
     * 如果 [id] 对应事项是折叠状态，则展开 [id] 自己；
     * 否则，展开 [id] 自己的子节点。
     */
    fun smartExpand(id: Int) {
        val table = load(needBuildTree = true)
        val items = table.ensureItems()

        val current = items.find { it.id == id }

        // 如果当前节点，就是折叠状态，需要先展开 id 对应的节点
        if (current?.collapseStatus == Item.COLLAPSE_STATUS_COLLAPSE) {
            current.collapseStatus = null
            store(table)
            return
        }

        // 否则，展开下一层的所有子节点
        val children = items.filter { it.parentIds?.contains(id) == true }
        for (child in children) {
            child.collapseStatus = null
        }

        store(table)
        last_modify_item_id = id
    }

    /**
     * 将指定事项向上移动一位（与上一个兄弟交换 order 值）
     * 如果已经是第一个，则不做任何操作
     */
    fun sortMoveUp(id: Int) {
        val table = load(needBuildTree = true)
        val current = table.items?.find { it.id == id } ?: return
        val parentId = current.getFirstParentIdOrNull()

        val brothers = if (parentId == null) {
            table.items?.filter { it.parentIds.isNullOrEmpty() } ?: emptyList()
        } else {
            table.items?.filter { it.parentIds?.contains(parentId) == true } ?: emptyList()
        }

        printLine("调试: id=$id, parentId=$parentId, brothers=${brothers.map { it.id }}")
        
        val sortedBrothers = brothers.sortedBy { it.order ?: it.id ?: 0 }
        printLine("调试: sortedBrothers=${sortedBrothers.map { "${it.id}(order=${it.order})" }}")
        
        // 如果兄弟节点的 order 都是 null，先按排序顺序分配 order 值
        if (sortedBrothers.all { it.order == null }) {
            printLine("调试: 所有 order 都是 null，分配初始值")
            sortedBrothers.forEachIndexed { index, item ->
                item.order = index
            }
        }
        
        val currentIndex = sortedBrothers.indexOfFirst { it.id == id }
        printLine("调试: currentIndex=$currentIndex")
        if (currentIndex <= 0) return

        val prevBrother = sortedBrothers[currentIndex - 1]
        val tempOrder = current.order
        current.order = prevBrother.order
        prevBrother.order = tempOrder
        
        printLine("调试: 交换后 current.order=${current.order}, prevBrother.order=${prevBrother.order}")

        store(table)
        last_modify_item_id = id
    }

    /**
     * 将指定事项向下移动一位（与下一个兄弟交换 order 值）
     * 如果已经是最后一个，则不做任何操作
     */
    fun sortMoveDown(id: Int) {
        val table = load(needBuildTree = true)
        val current = table.items?.find { it.id == id } ?: return
        val parentId = current.getFirstParentIdOrNull()

        val brothers = if (parentId == null) {
            table.items?.filter { it.parentIds.isNullOrEmpty() } ?: emptyList()
        } else {
            table.items?.filter { it.parentIds?.contains(parentId) == true } ?: emptyList()
        }

        val sortedBrothers = brothers.sortedBy { it.order ?: it.id ?: 0 }
        
        // 如果兄弟节点的 order 都是 null，先按排序顺序分配 order 值
        if (sortedBrothers.all { it.order == null }) {
            sortedBrothers.forEachIndexed { index, item ->
                item.order = index
            }
        }
        
        val currentIndex = sortedBrothers.indexOfFirst { it.id == id }
        if (currentIndex < 0 || currentIndex >= sortedBrothers.size - 1) return

        val nextBrother = sortedBrothers[currentIndex + 1]
        val tempOrder = current.order
        current.order = nextBrother.order
        nextBrother.order = tempOrder

        store(table)
        last_modify_item_id = id
    }

    /**
     * 将指定事项移动到同级事项的最前面
     */
    fun sortMoveToTop(id: Int) {
        val table = load(needBuildTree = true)
        val current = table.items?.find { it.id == id } ?: return
        val parentId = current.getFirstParentIdOrNull()

        val brothers = if (parentId == null) {
            table.items?.filter { it.parentIds.isNullOrEmpty() } ?: emptyList()
        } else {
            table.items?.filter { it.parentIds?.contains(parentId) == true } ?: emptyList()
        }

        val minOrder = brothers.minOfOrNull { it.order ?: it.id ?: 0 } ?: 0
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

        val brothers = if (parentId == null) {
            table.items?.filter { it.parentIds.isNullOrEmpty() } ?: emptyList()
        } else {
            table.items?.filter { it.parentIds?.contains(parentId) == true } ?: emptyList()
        }

        val maxOrder = brothers.maxOfOrNull { it.order ?: it.id ?: 0 } ?: 0
        current.order = maxOrder + 1

        store(table)
        last_modify_item_id = id
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    /**
     * 加载。
     *
     * @param needBuildTree 是否需要构建父子关系。 注意： 即使构建了父子关系，根节点也不会移除子节点。
     */
    private fun load(needBuildTree: Boolean = false): ItemTable {
        val file = File(filePath)
        val loaded = if (file.exists()) {
            gson.fromJson(file.readText(), ItemTable::class.java) ?: ItemTable()
        } else {
            ItemTable().apply {
                version = 2
            }
        }

        // 如果数据格式不是最新版本，升级到最新版本。
        if (loaded.version == null) {
            // 首次升级
            migrateNullTo2(loaded)
            store(loaded)
        }

        if (!needBuildTree) {
            return loaded
        }

        // 构建父子关系
        if (loaded.items != null) {
            buildParentChildRelationships(loaded.ensureItems())
        }

        return loaded
    }

    private fun store(table: ItemTable) {
        table.items?.forEach { item ->
            item.children = null
        }
        File(filePath).writeText(gson.toJson(table))
    }

    /**
     * 构建父子关系。
     * 只会基于 [items] 构建，如果 [items] 数据不全，则能构建多少就构建多少。
     */
    private fun buildParentChildRelationships(items: List<Item>): MutableList<Item> {
        // 将 items 转换为 id -> TodoItem 的映射
        val id2Items = items.associateBy { it.id }
        val result = mutableListOf<Item>()

        // 遍历每一个有父节点的元素
        for (item in items) {
            if (item.parentIds.isNullOrEmpty()) {
                continue
            }

            // 找到父节点们
            val parents = item.parentIds?.mapNotNull { parentId ->
                id2Items[parentId]
            }?.forEach { parent ->
                parent.addChild(item)
            }
        }

        // 对每个节点的 children 按 order 排序（order 为 null 时按 id 排序）
        items.forEach { item ->
            item.children?.sortBy { it.order ?: it.id ?: 0 }
        }

        return result
    }

    /**
     * 从 [all] 中，找到所有父节点是 [parentId] 的子事项。
     * 不会递归查询。
     */
    private fun findChildren(all: List<Item>, parentId: Int): List<Item> {
        return all.filter { it.parentIds?.contains(parentId) == true }
    }

    /**
     * 从 [all] 中，找到所有父节点是 [parent] 的子事项。
     * 会递归查询。
     */
    private fun fullChildrenRecursively(all: List<Item>, parent: Item) {
        val children = findChildren(all, parent.id!!)
        if (children.isEmpty()) {
            return
        }

        parent.addChildren(children)
        children.forEach {
            fullChildrenRecursively(all, it)
        }
    }

    private fun runWithTable(block: (ItemTable)->Unit) {
        val table = load()
        block.invoke(table)
        store(table)
    }

    private fun <R> callWithTable(block: (ItemTable)->R): R {
        val table = load()
        val r = block.invoke(table)
        store(table)
        return r
    }

    private fun useItems(ids: List<Int>, block: (List<Item>) -> Unit) {
        runWithTable { table ->
            val items = table.ensureItems().filter { ids.contains(it.id) }
            block.invoke(items)
        }
    }

    private fun foreach(ids: List<Int>, block: (Item) -> Unit) {
        useItems(ids) { items ->
            items.forEach { item ->
                block.invoke(item)
            }
        }
    }

    private fun find(predication: (Item)->Boolean, block: (List<Item>)->Unit) {
        runWithTable { table ->
            val items = table.ensureItems().filter { predication.invoke(it) }
            block.invoke(items)
        }
    }

    /**
     * @return 返回的这个列表中，每个 item 的 children 是否有值，是未定义的。
     */
    private fun findBrothers(id: Int): List<Item> {
        val table = load(needBuildTree = true)
        val items = table.ensureItems()
        val current = items.find { it.id == id }
        val parentId = current?.getFirstParentIdOrNull() ?: return items
        val parent = items.find { it.id == parentId }
        return parent?.children ?: emptyList()
    }

    /**
     * 从首个版本升级为 Version-2.
     * 这次升级主要是将 parentId 升级为 parentIds.
     */
    private fun migrateNullTo2(loaded: ItemTable) {
        printLine("============================================")
        printLine("升级 version = 2 中 ....")
        loaded.version = 2
        loaded.ensureItems().forEach { item ->
            printLine("\n---------------------")
            val parentId = item.getParentIdAndIKnownThisIsNotSafe()
            val parentIds = item.parentIds
            val allParentIds = HashSet<Int>()
            if (parentId != null) {
                allParentIds.add(parentId)
            }
            if (parentIds != null) {
                allParentIds.addAll(parentIds)
            }
            printLine("id = ${item.id}, parentId = ${parentId}, parentIds = ${parentIds?.joinToString { it.toString() }},  -> parentIds = ${allParentIds.joinToString { it.toString() }}")
            item.parentIds = allParentIds.toMutableList()
        }
        printLine("升级完毕!")
        printLine("============================================")
    }
}