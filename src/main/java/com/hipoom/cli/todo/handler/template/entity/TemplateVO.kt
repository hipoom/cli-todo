package com.hipoom.cli.todo.handler.template.entity

import com.google.gson.annotations.SerializedName

/**
 * @author ZhengHaiPeng
 * @since 2025/2/18 21:18
 *
 */
class TemplateVO(

    /**
     * 这个模板的 id.
     */
    @SerializedName("id")
    var id: Int,

    /**
     * 模板的别名。
     */
    @SerializedName("alias")
    var alias: String,

    /**
     * 这个模板下面的所有子项。
     */
    @SerializedName("items")
    var items: List<String>
)