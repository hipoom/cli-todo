@file:Suppress("FoldInitializerAndIfToElvis")

package com.hipoom.cli.todo

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.workspace.Workspace
import com.hipoom.cli.workspace.WorkspaceContext
import com.hipoom.cli.workspace.WorkspaceDataRepository


/**
 * 进程数据。
 */
object ProcessData {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    /**
     * 保存当前进程的数据。
     */
    private val processDataRepository = HashMap<String, Any?>()

    /**
     * 当前工作目录的数据仓库。
     */
    var currentWorkspaceDataRepository: WorkspaceDataRepository? = null
        private set;



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 获取当前工作空间。
     */
    fun getCurrentWorkspaceContext(): WorkspaceContext {
        val temp = processDataRepository["current_workspace"] as? WorkspaceContext
        if (temp == null) {
            throw IllegalStateException("无法获取当前的工作空间是什么.")
        }
        return temp
    }

    /**
     * 更新当前工作空间。
     * 在 App#onStart、 App#onWorkspaceChanged 这两个方法中更新。
     */
    fun updateCurrentWorkspaceContext(workspace: WorkspaceContext) {
        processDataRepository["current_workspace"] = workspace
        currentWorkspaceDataRepository = WorkspaceDataRepository(workspace)
    }

}


/**
 * 持久化数据，是整个 app 的数据，不属于任何 workspace。
 */
object PersistentData {

    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    fun queryLastestWorkspaceContext(): WorkspaceContext? {
        val alias = app.database().query("currentWorkspace") ?: "default"
        return Workspace.queryWorkspaceContext(app.getAppName(), alias)
    }

    fun updateCurrentWorkspaceContext(workspace: WorkspaceContext) {
        app.database().save("currentWorkspace", workspace.workspaceAlias)
    }

    fun createAndStoreCurrentWorkspaceContext(alias: String) {
        updateCurrentWorkspaceContext(Workspace.relative.createAndSaveWorkspace(app.getAppName(), alias))
    }

    fun loadStyles(): String? {
        return app.database().query("styles")
    }

    fun updateStyles(json: String) {
        return app.database().save("styles", json)
    }

    fun loadTextMappings(): String? {
        return app.database().query("text_mappings")
    }

    fun updateTextMappings(json: String) {
        return app.database().save("text_mappings", json)
    }

}

val CliApp.processData: ProcessData
    get() = ProcessData

val CliApp.persistentData: PersistentData
    get() = PersistentData
