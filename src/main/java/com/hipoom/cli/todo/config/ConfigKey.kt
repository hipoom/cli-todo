package com.hipoom.cli.todo.config

import com.hipoom.cli.todo.config.validators.ConfigValidator

/**
 * 类型安全的配置键
 * 
 * 用于定义配置项的元数据，包括配置名称、默认值、作用域和验证器。
 * 通过泛型参数 T 确保配置值的类型安全。
 *
 * @param T 配置值的类型
 * @property name 配置名称，用于存储和读取配置时的唯一标识
 * @property defaultValue 配置的默认值，当配置不存在时返回此值
 * @property scope 配置的作用域，决定配置的存储位置和生命周期
 * @property description 配置项的描述说明，用于文档和错误提示
 * @property validator 配置验证器（可选），用于验证配置值的合法性
 *
 * @example
 * ```kotlin
 * // 定义一个布尔类型的配置键
 * val NEED_SHOW_ID = ConfigKey(
 *     name = "show.needShowId",
 *     defaultValue = true,
 *     scope = ConfigScope.WORKSPACE,
 *     description = "是否展示事项的 ID"
 * )
 *
 * // 定义一个颜色类型的配置键，带验证器
 * val COMMENT_TEXT_COLOR = ConfigKey(
 *     name = "show.commentStyle.textColor",
 *     defaultValue = ColorConfig(128, 128, 128),
 *     scope = ConfigScope.WORKSPACE,
 *     description = "备注文字的颜色，格式是 RGB",
 *     validator = ColorValidator()
 * )
 * ```
 */
class ConfigKey<T>(
    val name: String,
    val defaultValue: T,
    val scope: ConfigScope,
    val description: String = "",
    val validator: ConfigValidator<T>? = null
) {
    
    /* ======================================================= */
    /* 公共方法                                                */
    /* ======================================================= */
    
    /**
     * 验证配置值是否合法
     * @param value 待验证的配置值
     * @return 验证结果，合法返回 true，否则返回 false
     */
    fun isValid(value: T): Boolean {
        // 如果没有验证器，默认通过
        if (validator == null) {
            return true
        }
        return validator.validate(value)
    }
    
    /**
     * 获取验证失败的错误信息
     * @param value 验证失败的配置值
     * @return 错误信息字符串
     */
    fun getValidationError(value: T): String {
        // 如果没有验证器，返回默认错误信息
        if (validator == null) {
            return "配置项 '$name' 没有配置验证器"
        }
        return "配置项 '$name' 验证失败: ${validator.getErrorMessage(value)}"
    }
    
    /**
     * 获取配置的完整路径名称
     * 格式: scope:name
     * @return 完整路径名称
     */
    fun getFullPath(): String {
        return "${scope.name.lowercase()}:$name"
    }
    
    /* ======================================================= */
    /* 重写方法                                                */
    /* ======================================================= */
    
    override fun toString(): String {
        val typeName = defaultValue?.let { it::class.simpleName } ?: "null"
        return "ConfigKey(name='$name', scope=$scope, type=$typeName)"
    }
    
    override fun equals(other: Any?): Boolean {
        // 如果是同一个对象，返回 true
        if (this === other) return true
        // 如果类型不匹配，返回 false
        if (other !is ConfigKey<*>) return false
        // 比较名称和作用域
        return name == other.name && scope == other.scope
    }
    
    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + scope.hashCode()
        return result
    }
}
