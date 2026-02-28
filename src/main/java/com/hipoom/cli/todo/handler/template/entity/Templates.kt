package com.hipoom.cli.todo.handler.template.entity

import com.google.gson.annotations.SerializedName

/**
 * @author ZhengHaiPeng
 * @since 2025/2/18 21:51
 *
 */
class Templates(

    @SerializedName("version")
    var version: Int,

    @SerializedName("templates")
    val templates: ArrayList<TemplateVO>
)