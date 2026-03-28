package com.hipoom.cli.todo.config.storage

/**
 * 配置存储接口
 * 定义配置的存储和读取方法
 */
interface ConfigStorage {
    
    /**
     * 获取配置值
     * @param key 配置键名
     * @return 配置值字符串，不存在返回 null
     */
    fun get(key: String): String?
    
    /**
     * 设置配置值
     * @param key 配置键名
     * @param value 配置值字符串
     */
    fun set(key: String, value: String)
    
    /**
     * 删除配置值
     * @param key 配置键名
     */
    fun remove(key: String)
    
    /**
     * 检查配置是否存在
     * @param key 配置键名
     * @return 存在返回 true，否则返回 false
     */
    fun exists(key: String): Boolean
}
