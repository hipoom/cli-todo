package com.hipoom.cli.todo.handler.style.persistent

import com.google.gson.annotations.SerializedName
import com.hipoom.cli.todo.handler.style.pojo.Style


class StyleConfigs {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    @SerializedName("currentStyleName")
    var currentStyleName: String? = null

    @SerializedName("name2Style")
    var name2Style: ArrayList<NameStylePair>? = null



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    fun find(name: String): Style? {
        return name2Style?.find { it.name == name }?.style
    }

    fun addOrReplace(name: String, style: Style) {
        // 先删除旧的
        name2Style?.removeIf { it.name == name }
        // 如果为空，就创建一个
        if (name2Style == null) {
            name2Style = ArrayList()
        }
        // 添加新的
        name2Style?.add(NameStylePair().apply {
            this.name = name
            this.style = style
        })  
    }

}