package com.hipoom.cli.todo.config

import com.hipoom.cli.todo.app
import com.hipoom.cli.todo.config.storage.AppConfigStorage
import com.hipoom.cli.todo.config.storage.ConfigStorage
import com.hipoom.cli.todo.config.storage.ProcessConfigStorage
import com.hipoom.cli.todo.config.storage.WorkspaceConfigStorage
import com.hipoom.cli.todo.gson

/**
 * 1. 配置管理器实现类
 * 
 * 1.1 整合三层存储（进程、App、Workspace），提供统一的配置访问接口。
 */
class ConfigManagerImpl : ConfigManager {
    
    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */
    
    /**
     * 1.1.1 配置变更监听器映射
     * Key: 配置键的完整路径
     * Value: 监听器列表
     */
    private val observers = HashMap<String, MutableList<(Any?) -> Unit>>()
    
    /**
     * 1.1.2 进程级别存储
     */
    private val processStorage = ProcessConfigStorage
    
    /**
     * 1.1.3 App 级别存储
     */
    private val appStorage by lazy { AppConfigStorage(app) }
    
    /**
     * 1.1.4 Workspace 级别存储
     */
    private val workspaceStorage by lazy { WorkspaceConfigStorage.getCurrent() }
    
    /* ======================================================= */
    /* Override Methods                                        */
    /* ======================================================= */
    
    /**
     * 1.2 获取配置值
     */
    override fun <T> get(key: ConfigKey<T>): T {
        // 1.2.1 根据作用域选择存储
        val storage = getStorage(key.scope)
        
        // 1.2.2 获取存储的字符串值
        val stringValue = storage.get(key.name)
        
        // 1.2.3 如果不存在，返回默认值
        if (stringValue == null) {
            return key.defaultValue
        }
        
        // 1.2.4 转换为目标类型
        return convertFromString(stringValue, key.defaultValue)
    }
    
    /**
     * 1.3 设置配置值
     */
    override fun <T> set(key: ConfigKey<T>, value: T) {
        // 1.3.1 验证配置值
        if (!key.isValid(value)) {
            throw IllegalArgumentException(key.getValidationError(value))
        }
        
        // 1.3.2 获取旧值用于通知
        val oldValue = get(key)
        
        // 1.3.3 根据作用域选择存储
        val storage = getStorage(key.scope)
        
        // 1.3.4 转换为字符串并存储
        val stringValue = convertToString(value)
        storage.set(key.name, stringValue)
        
        // 1.3.5 通知监听器
        notifyObservers(key, oldValue, value)
    }
    
    /**
     * 1.4 注册配置变更监听器
     */
    override fun <T> observe(key: ConfigKey<T>, observer: (T) -> Unit) {
        val fullPath = key.getFullPath()
        // 1.4.1 如果该键还没有监听器列表，创建一个
        if (!observers.containsKey(fullPath)) {
            observers[fullPath] = mutableListOf()
        }
        // 1.4.2 添加监听器，需要类型转换
        @Suppress("UNCHECKED_CAST")
        observers[fullPath]!!.add(observer as (Any?) -> Unit)
    }
    
    /**
     * 1.5 移除配置变更监听器
     */
    override fun <T> removeObserver(key: ConfigKey<T>, observer: (T) -> Unit) {
        val fullPath = key.getFullPath()
        // 1.5.1 获取监听器列表
        val observerList = observers[fullPath] ?: return
        // 1.5.2 移除监听器，需要类型转换
        @Suppress("UNCHECKED_CAST")
        observerList.remove(observer as (Any?) -> Unit)
        // 1.5.3 如果列表为空，移除整个条目
        if (observerList.isEmpty()) {
            observers.remove(fullPath)
        }
    }
    
    /**
     * 1.6 清除指定键的所有监听器
     */
    override fun <T> clearObservers(key: ConfigKey<T>) {
        val fullPath = key.getFullPath()
        observers.remove(fullPath)
    }
    
    /**
     * 1.7 检查配置是否存在
     */
    override fun <T> exists(key: ConfigKey<T>): Boolean {
        val storage = getStorage(key.scope)
        return storage.exists(key.name)
    }
    
    /**
     * 1.8 删除配置
     */
    override fun <T> remove(key: ConfigKey<T>) {
        val storage = getStorage(key.scope)
        storage.remove(key.name)
    }
    
    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */
    
    /**
     * 1.9 根据作用域获取存储实例
     */
    private fun getStorage(scope: ConfigScope): ConfigStorage {
        return when (scope) {
            ConfigScope.PROCESS -> processStorage
            ConfigScope.APP -> appStorage
            ConfigScope.WORKSPACE -> workspaceStorage
        }
    }
    
    /**
     * 1.10 将字符串值转换为目标类型
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> convertFromString(value: String, defaultValue: T): T {
        // 1.10.1 根据默认值的类型进行转换
        return when (defaultValue) {
            is Boolean -> value.toBoolean() as T
            is Int -> value.toIntOrNull() as? T ?: defaultValue
            is String -> value as T
            else -> {
                // 1.10.2 尝试使用 Gson 解析复杂类型
                try {
                    gson.fromJson(value, defaultValue!!::class.java) as T
                } catch (e: Exception) {
                    defaultValue
                }
            }
        }
    }
    
    /**
     * 1.11 将值转换为字符串
     */
    private fun <T> convertToString(value: T): String {
        return when (value) {
            is Boolean -> value.toString()
            is Int -> value.toString()
            is String -> value
            else -> gson.toJson(value)
        }
    }
    
    /**
     * 1.12 通知所有监听器
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> notifyObservers(key: ConfigKey<T>, oldValue: T?, newValue: T) {
        val fullPath = key.getFullPath()
        val observerList = observers[fullPath] ?: return
        
        // 1.12.1 遍历并通知所有监听器
        for (observer in observerList.toList()) {
            try {
                observer.invoke(newValue)
            } catch (e: Exception) {
                // 1.12.2 忽略监听器执行异常，避免影响其他监听器
                e.printStackTrace()
            }
        }
    }
    
    /* ======================================================= */
    /* Companion Object                                        */
    /* ======================================================= */
    
    companion object {
        /**
         * 1.13 获取默认的配置管理器实例
         */
        fun getDefault(): ConfigManagerImpl {
            return ConfigManagerImpl()
        }
    }
}
