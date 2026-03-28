package com.hipoom.cli.todo.config.keys

import com.hipoom.cli.todo.config.ConfigKey
import com.hipoom.cli.todo.config.ConfigScope

/**
 * Window 相关配置键定义
 */
object WindowConfigKeys {
    
    /* ======================================================= */
    /* 窗口模式配置                                            */
    /* ======================================================= */
    
    /** 是否启用窗口模式 */
    val IS_ENABLE = ConfigKey(
        name = "window.isEnable",
        defaultValue = true,
        scope = ConfigScope.WORKSPACE,
        description = "是否在编辑和创建模板时使用窗口模式，而不是在命令行中"
    )
}
