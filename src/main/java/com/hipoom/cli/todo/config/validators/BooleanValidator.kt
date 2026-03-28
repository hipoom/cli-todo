package com.hipoom.cli.todo.config.validators

/**
 * 布尔验证器
 */
class BooleanValidator : ConfigValidator<Boolean> {
    override fun validate(value: Boolean): Boolean = true
    
    override fun getErrorMessage(value: Boolean): String = "布尔值验证失败"
}
