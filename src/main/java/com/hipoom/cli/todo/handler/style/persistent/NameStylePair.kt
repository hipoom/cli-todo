package com.hipoom.cli.todo.handler.style.persistent

import com.google.gson.annotations.SerializedName
import com.hipoom.cli.todo.handler.style.pojo.Style

/**
 * @author ZhengHaiPeng
 * @since 2026/3/1 15:55
 *
 */
class NameStylePair {

    @SerializedName("name")
    var name: String? = null

    @SerializedName("style")
    var style: Style? = null

}