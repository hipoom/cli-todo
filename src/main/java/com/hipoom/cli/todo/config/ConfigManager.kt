package com.hipoom.cli.todo.config

/**
 * 1. 统一配置管理器接口
 * 
 * 1.1 提供类型安全的配置读写和变更监听功能。
 * 1.2 支持三种配置作用域：进程级别、App 级别、Workspace 级别。
 */
interface ConfigManager {
    
    /* ======================================================= */
    /* 2. 配置读取方法                                          */
    /* ======================================================= */
    
    /**
     * 2.1 获取配置值
     * @param key 配置键
     * @return 配置值，不存在则返回默认值
     */
    fun <T> get(key: ConfigKey<T>): T
    
    /**
     * 2.2 检查配置是否存在
     * @param key 配置键
     * @return 存在返回 true，否则返回 false
     */
    fun <T> exists(key: ConfigKey<T>): Boolean
    
    /* ======================================================= */
    /* 3. 配置写入方法                                          */
    /* ======================================================= */
    
    /**
     * 3.1 设置配置值
     * @param key 配置键
     * @param value 配置值
     * @throws IllegalArgumentException 当配置值验证失败时抛出
     */
    fun <T> set(key: ConfigKey<T>, value: T)
    
    /**
     * 3.2 删除配置
     * @param key 配置键
     */
    fun <T> remove(key: ConfigKey<T>)
    
    /* ======================================================= */
    /* 4. 配置变更监听方法                                      */
    /* ======================================================= */
    
    /**
     * 4.1 监听配置变更
     * @param key 配置键
     * @param observer 变更监听器
     */
    fun <T> observe(key: ConfigKey<T>, observer: (T) -> Unit)
    
    /**
     * 4.2 移除配置变更监听
     * @param key 配置键
     * @param observer 变更监听器
     */
    fun <T> removeObserver(key: ConfigKey<T>, observer: (T) -> Unit)
    
    /**
     * 4.3 清除指定配置键的所有监听器
     * @param key 配置键
     */
    fun <T> clearObservers(key: ConfigKey<T>)
}
