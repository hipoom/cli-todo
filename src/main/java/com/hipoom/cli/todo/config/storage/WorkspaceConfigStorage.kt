package com.hipoom.cli.todo.config.storage

import com.hipoom.cli.todo.app
import com.hipoom.cli.todo.processData
import com.hipoom.cli.workspace.WorkspaceContext
import com.hipoom.cli.workspace.WorkspaceDataRepository

/**
 * Workspace 级别配置存储
 * 使用 Workspace 数据库存储，与工作空间绑定
 */
class WorkspaceConfigStorage(private val workspace: WorkspaceContext) : ConfigStorage {
    
    /* ======================================================= */
    /* Override Methods                                        */
    /* ======================================================= */
    
    override fun get(key: String): String? {
        return callWithRepository { it.get(key) }
    }
    
    override fun set(key: String, value: String) {
        runWithRepository { it.set(key, value) }
    }
    
    override fun remove(key: String) {
        runWithRepository { it.set(key, null) }
    }
    
    override fun exists(key: String): Boolean {
        return callWithRepository { it.get(key) } != null
    }
    
    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */
    
    /**
     * 使用数据仓库执行操作
     */
    private fun runWithRepository(callback: (WorkspaceDataRepository) -> Unit) {
        val temp = app.processData.currentWorkspaceDataRepository
        // 如果是当前工作空间，使用缓存的数据仓库
        if (workspace == temp?.workspace) {
            callback.invoke(temp)
        } else {
            // 否则创建新的数据仓库
            callback.invoke(WorkspaceDataRepository(workspace))
        }
    }
    
    /**
     * 使用数据仓库获取值
     */
    private fun <R> callWithRepository(callback: (WorkspaceDataRepository) -> R): R {
        val temp = app.processData.currentWorkspaceDataRepository
        // 如果是当前工作空间，使用缓存的数据仓库
        return if (workspace == temp?.workspace) {
            callback.invoke(temp)
        } else {
            // 否则创建新的数据仓库
            callback.invoke(WorkspaceDataRepository(workspace))
        }
    }
    
    /* ======================================================= */
    /* Companion Object                                        */
    /* ======================================================= */
    
    companion object {
        /**
         * 获取当前工作空间的配置存储实例
         */
        fun getCurrent(): WorkspaceConfigStorage {
            val workspace = app.processData.getCurrentWorkspaceContext()
            return WorkspaceConfigStorage(workspace)
        }
    }
}
