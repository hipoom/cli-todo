package com.hipoom.cli.todo.config

import com.hipoom.cli.todo.config.keys.LaunchConfigKeys
import com.hipoom.cli.todo.config.keys.ShowConfigKeys
import com.hipoom.cli.todo.config.keys.WindowConfigKeys

/**
 * 配置适配器
 * 提供与旧配置系统兼容的访问接口，内部使用新的 ConfigManager
 * 
 * 这个类用于在迁移期间保持向后兼容，所有新的代码应该直接使用 ConfigManager
 */
@Deprecated(
    message = "请使用 ConfigManager 或 Configs 单例访问配置",
    replaceWith = ReplaceWith("Configs.get(key)", "com.hipoom.cli.todo.config.Configs")
)
object ConfigAdapter {
    
    /* ======================================================= */
    /* Window 配置                                             */
    /* ======================================================= */
    
    /**
     * 窗口模式是否开启
     */
    val windowIsEnable: Boolean
        get() = Configs.get(WindowConfigKeys.IS_ENABLE)
    
    /* ======================================================= */
    /* Show 配置                                               */
    /* ======================================================= */
    
    /**
     * 是否展示事项 ID
     */
    var showNeedShowId: Boolean
        get() = Configs.get(ShowConfigKeys.NEED_SHOW_ID)
        set(value) { Configs.set(ShowConfigKeys.NEED_SHOW_ID, value) }
    
    /**
     * 是否展示事项状态
     */
    var showNeedShowStatus: Boolean
        get() = Configs.get(ShowConfigKeys.NEED_SHOW_STATUS)
        set(value) { Configs.set(ShowConfigKeys.NEED_SHOW_STATUS, value) }
    
    /**
     * 是否展示已完成事项
     */
    var showNeedShowDone: Boolean
        get() = Configs.get(ShowConfigKeys.NEED_SHOW_DONE)
        set(value) { Configs.set(ShowConfigKeys.NEED_SHOW_DONE, value) }
    
    /**
     * 是否展示已删除事项
     */
    var showNeedShowDeleted: Boolean
        get() = Configs.get(ShowConfigKeys.NEED_SHOW_DELETED)
        set(value) { Configs.set(ShowConfigKeys.NEED_SHOW_DELETED, value) }
    
    /**
     * 是否展示负责人
     */
    var showNeedShowOwner: Boolean
        get() = Configs.get(ShowConfigKeys.NEED_SHOW_OWNER)
        set(value) { Configs.set(ShowConfigKeys.NEED_SHOW_OWNER, value) }
    
    /**
     * 是否展示标签
     */
    var showNeedShowLabel: Boolean
        get() = Configs.get(ShowConfigKeys.NEED_SHOW_LABEL)
        set(value) { Configs.set(ShowConfigKeys.NEED_SHOW_LABEL, value) }
    
    /**
     * 是否使用高级对齐模式
     */
    var showUseAlignMode: Boolean
        get() = Configs.get(ShowConfigKeys.USE_ALIGN_MODE)
        set(value) { Configs.set(ShowConfigKeys.USE_ALIGN_MODE, value) }
    
    /**
     * 是否展示截止时间
     */
    var showNeedShowDeadline: Boolean
        get() = Configs.get(ShowConfigKeys.NEED_SHOW_DEADLINE)
        set(value) { Configs.set(ShowConfigKeys.NEED_SHOW_DEADLINE, value) }
    
    /**
     * 是否展示备注
     */
    var showNeedShowComment: Boolean
        get() = Configs.get(ShowConfigKeys.NEED_SHOW_COMMENT)
        set(value) { Configs.set(ShowConfigKeys.NEED_SHOW_COMMENT, value) }
    
    /**
     * 是否展示备注下标
     */
    var showNeedShowCommentSubscript: Boolean
        get() = Configs.get(ShowConfigKeys.NEED_SHOW_COMMENT_SUBSCRIPT)
        set(value) { Configs.set(ShowConfigKeys.NEED_SHOW_COMMENT_SUBSCRIPT, value) }
    
    /* ======================================================= */
    /* Launch 配置                                             */
    /* ======================================================= */
    
    /**
     * 启动时是否自动展示
     */
    var launchNeedShowOnLaunch: Boolean
        get() = Configs.get(LaunchConfigKeys.NEED_SHOW_ON_LAUNCH)
        set(value) { Configs.set(LaunchConfigKeys.NEED_SHOW_ON_LAUNCH, value) }
}
