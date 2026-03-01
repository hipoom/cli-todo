package com.hipoom.cli.todo.handler.style

import com.google.gson.annotations.SerializedName
import com.hipoom.cli.core.ui.palette.Color

/**
 * 样式数据类，用于存储和加载样式配置。
 * @author ZhengHaiPeng
 * @since 2026/3/1
 */
data class StyleData(
    /**
     * 置顶文本颜色，格式为 RGB 字符串，例如 "255,0,0"，或 "None" 表示使用默认值。
     */
    @SerializedName("pinTextColor")
    val pinTextColor: String = "None",
    
    /**
     * 置顶背景颜色，格式为 RGB 字符串，例如 "255,0,0"，或 "None" 表示使用默认值。
     */
    @SerializedName("pinBackgroundColor")
    val pinBackgroundColor: String = "None",
    
    /**
     * 次要文本颜色，格式为 RGB 字符串，例如 "255,0,0"，或 "None" 表示使用默认值。
     */
    @SerializedName("secondaryTextColor")
    val secondaryTextColor: String = "None",
    
    /**
     * 备注背景颜色，格式为 RGB 字符串，例如 "255,0,0"，或 "None" 表示使用默认值。
     */
    @SerializedName("commentBackgroundColor")
    val commentBackgroundColor: String = "None",
    
    /**
     * 提示文本颜色，格式为 RGB 字符串，例如 "255,0,0"，或 "None" 表示使用默认值。
     */
    @SerializedName("hintTextColor")
    val hintTextColor: String = "None"
)

/**
 * 将 StyleData 转换为 Style 接口实现。
 */
fun StyleData.toStyle(): Style {
    val styleData = this
    return object : Style {
        override val pinTextColor: Color?
            get() = parseColor(styleData.pinTextColor, true)
        
        override val pinBackgroundColor: Color?
            get() = parseColor(styleData.pinBackgroundColor, false)
        
        override val secondaryTextColor: Color?
            get() = parseColor(styleData.secondaryTextColor, true)
        
        override val commentBackgroundColor: Color?
            get() = parseColor(styleData.commentBackgroundColor, false)
        
        override val hintTextColor: Color?
            get() = parseColor(styleData.hintTextColor, true)
    }
}

/**
 * 将 Style 接口实现转换为 StyleData。
 */
fun Style.toStyleData(): StyleData {
    return StyleData(
        pinTextColor = formatColor(pinTextColor),
        pinBackgroundColor = formatColor(pinBackgroundColor),
        secondaryTextColor = formatColor(secondaryTextColor),
        commentBackgroundColor = formatColor(commentBackgroundColor),
        hintTextColor = formatColor(hintTextColor)
    )
}

/**
 * 解析颜色字符串为 Color 对象。
 * @param isForeground 是否为前景色
 */
private fun parseColor(colorStr: String, isForeground: Boolean): Color? {
    if (colorStr == "None") {
        return null
    }
    
    val parts = colorStr.split(",").mapNotNull { it.trim().toIntOrNull() }
    if (parts.size != 3) {
        return null
    }
    
    return if (isForeground) {
        com.hipoom.cli.core.ui.palette.Colors.Bits24.createForeground(parts[0], parts[1], parts[2])
    } else {
        com.hipoom.cli.core.ui.palette.Colors.Bits24.createBackground(parts[0], parts[1], parts[2])
    }
}

/**
 * 格式化 Color 对象为字符串。
 */
private fun formatColor(color: Color?): String {
    if (color == null) {
        return "None"
    }
    
    // 这里简化处理，实际应该根据 Color 接口的实现获取 RGB 值
    // 由于 Color 接口没有提供获取 RGB 值的方法，这里暂时返回 "None"
    // 后续需要根据实际的 Color 实现进行修改
    return "None"
}
