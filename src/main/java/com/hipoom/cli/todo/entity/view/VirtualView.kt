package com.hipoom.cli.todo.entity.view

import com.google.gson.annotations.SerializedName

/**
 * @author ZhengHaiPeng
 * @since 2025/4/19 15:44
 *
 */
data class VirtualView(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)