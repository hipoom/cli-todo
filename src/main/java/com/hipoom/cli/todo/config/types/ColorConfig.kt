package com.hipoom.cli.todo.config.types

import com.hipoom.cli.core.ui.palette.Color
import com.hipoom.cli.core.ui.palette.Colors

/**
 * 颜色配置数据类
 * @param r 红色分量 (0-255)
 * @param g 绿色分量 (0-255)
 * @param b 蓝色分量 (0-255)
 */
data class ColorConfig(
    val r: Int,
    val g: Int,
    val b: Int
) {
    /**
     * 转换为 Color 对象
     */
    fun toColor(): Color = Colors.Bits24.createForeground(r, g, b)
    
    /**
     * 转换为背景 Color 对象
     */
    fun toBackgroundColor(): Color = Colors.Bits24.createBackground(r, g, b)
    
    /**
     * 转换为字符串格式 "R,G,B"
     */
    override fun toString(): String = "$r,$g,$b"
    
    companion object {
        /**
         * 从字符串解析颜色配置
         * @param str 格式为 "R,G,B" 的字符串
         * @return ColorConfig 对象，解析失败返回 null
         */
        fun fromString(str: String): ColorConfig? {
            // 分割字符串并解析为整数
            val parts = str.split(",").mapNotNull { it.trim().toIntOrNull() }
            
            // 检查是否有三个分量
            if (parts.size != 3) return null
            
            // 检查每个分量是否在有效范围内
            if (parts.any { it !in 0..255 }) return null
            
            return ColorConfig(parts[0], parts[1], parts[2])
        }
        
        /**
         * 空颜色配置（表示使用默认值）
         */
        val NONE = ColorConfig(-1, -1, -1)
    }
}
