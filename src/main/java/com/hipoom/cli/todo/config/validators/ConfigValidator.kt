package com.hipoom.cli.todo.config.validators

/**
 * 配置验证器接口
 */
interface ConfigValidator<T> {
    /**
     * 验证配置值是否合法
     * @param value 配置值
     * @return 是否合法
     */
    fun validate(value: T): Boolean
    
    /**
     * 获取验证失败的错误信息
     * @param value 配置值
     * @return 错误信息
     */
    fun getErrorMessage(value: T): String
}
