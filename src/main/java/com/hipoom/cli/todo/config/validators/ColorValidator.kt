package com.hipoom.cli.todo.config.validators

import com.hipoom.cli.todo.config.types.ColorConfig

/**
 * 颜色验证器
 */
class ColorValidator : ConfigValidator<ColorConfig> {
    override fun validate(value: ColorConfig): Boolean {
        // NONE 表示使用默认值，跳过验证
        if (value == ColorConfig.NONE) return true
        return value.r in 0..255 && value.g in 0..255 && value.b in 0..255
    }
    
    override fun getErrorMessage(value: ColorConfig): String {
        return "颜色值必须在 0-255 范围内，当前值: ${value.r},${value.g},${value.b}"
    }
}
