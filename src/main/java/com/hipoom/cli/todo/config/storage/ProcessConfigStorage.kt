package com.hipoom.cli.todo.config.storage

/**
 * 进程级别配置存储
 * 使用内存存储，进程结束后数据消失
 */
object ProcessConfigStorage : ConfigStorage {
    
    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */
    
    /**
     * 内存存储容器
     */
    private val storage = HashMap<String, String>()
    
    /* ======================================================= */
    /* Override Methods                                        */
    /* ======================================================= */
    
    override fun get(key: String): String? {
        return storage[key]
    }
    
    override fun set(key: String, value: String) {
        storage[key] = value
    }
    
    override fun remove(key: String) {
        storage.remove(key)
    }
    
    override fun exists(key: String): Boolean {
        return storage.containsKey(key)
    }
    
    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */
    
    /**
     * 清空所有配置
     */
    fun clear() {
        storage.clear()
    }
    
    /**
     * 获取所有配置键
     */
    fun getAllKeys(): Set<String> {
        return storage.keys.toSet()
    }
}
