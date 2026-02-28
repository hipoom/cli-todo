package com.hipoom.cli.todo.handler.label

import com.google.gson.annotations.SerializedName

/**
 * 自动标签规则的数据类
 */
data class AutoLabelRule(
    @SerializedName("label")
    val label: String, // 要添加的标签名称
    
    @SerializedName("contains")
    val contains: String // 内容中需要包含的文本
)