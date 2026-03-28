package com.hipoom.cli.todo.config

/**
 * 1. 配置管理器单例
 * 
 * 1.1 提供全局访问的配置管理器实例
 */
object Configs {
    
    /* ======================================================= */
    /* 属性                                                    */
    /* ======================================================= */
    
    /**
     * 1.2 获取默认的配置管理器实例
     */
    val manager: ConfigManager by lazy { ConfigManagerImpl.getDefault() }
    
    /* ======================================================= */
    /* 公共方法                                                */
    /* ======================================================= */
    
    /**
     * 1.3 获取配置值的便捷方法
     * @param key 配置键
     * @return 配置值
     */
    fun <T> get(key: ConfigKey<T>): T = manager.get(key)
    
    /**
     * 1.4 设置配置值的便捷方法
     * @param key 配置键
     * @param value 配置值
     */
    fun <T> set(key: ConfigKey<T>, value: T) = manager.set(key, value)
}
