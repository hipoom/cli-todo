package com.hipoom.cli.todo.config.storage

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.todo.app

/**
 * App 级别配置存储
 * 使用数据库存储，跨工作空间共享
 */
class AppConfigStorage(private val cliApp: CliApp) : ConfigStorage {
    
    /* ======================================================= */
    /* Override Methods                                        */
    /* ======================================================= */
    
    override fun get(key: String): String? {
        return cliApp.database().query(key)
    }
    
    override fun set(key: String, value: String) {
        cliApp.database().save(key, value)
    }
    
    override fun remove(key: String) {
        cliApp.database().remove(key)
    }
    
    override fun exists(key: String): Boolean {
        return cliApp.database().query(key) != null
    }
    
    /* ======================================================= */
    /* Companion Object                                        */
    /* ======================================================= */
    
    companion object {
        /**
         * 获取默认的 App 配置存储实例
         */
        fun getDefault(): AppConfigStorage {
            return AppConfigStorage(app)
        }
    }
}
