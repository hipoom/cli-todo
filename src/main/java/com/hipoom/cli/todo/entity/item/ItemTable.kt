package com.hipoom.cli.todo.entity.item

import com.google.gson.annotations.SerializedName

/**
 * @author ZhengHaiPeng
 * @since 2025/2/2 17:05
 *
 */
class ItemTable {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    @SerializedName("maxID")
    var maxID: Int = 0

    @SerializedName("items")
    var items: MutableList<Item>? = null

    @SerializedName("version")
    var version: Int? = null



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    fun ensureItems(): MutableList<Item> {
        var temp = items
        if (temp == null) {
            temp = ArrayList()
            items = temp
        }

        return temp
    }
}