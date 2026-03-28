package com.hipoom.cli.todo.config

/**
 * 配置变更观察者接口
 */
interface ConfigObserver<T> {
    /**
     * 配置变更回调
     * @param key 配置键
     * @param oldValue 旧值
     * @param newValue 新值
     */
    fun onConfigChanged(key: ConfigKey<T>, oldValue: T?, newValue: T)
}
