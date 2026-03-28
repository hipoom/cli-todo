package com.hipoom.cli.todo.config.keys

import com.hipoom.cli.todo.config.ConfigKey
import com.hipoom.cli.todo.config.ConfigScope

/**
 * Launch 相关配置键定义
 */
object LaunchConfigKeys {
    
    /* ======================================================= */
    /* 启动配置                                                */
    /* ======================================================= */
    
    /** 是否在启动时自动展示 */
    val NEED_SHOW_ON_LAUNCH = ConfigKey(
        name = "launch.needShowOnLaunch",
        defaultValue = true,
        scope = ConfigScope.WORKSPACE,
        description = "是否需要在启动时自动展示一次"
    )
}
