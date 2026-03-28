package com.hipoom.cli.todo.config.keys

import com.hipoom.cli.todo.config.ConfigKey
import com.hipoom.cli.todo.config.ConfigScope

/**
 * 1. Process 级别配置键定义
 * 进程级别的配置，进程结束后消失
 */
object ProcessConfigKeys {
    
    /* ======================================================= */
    /* 1.1 进程状态配置                                        */
    /* ======================================================= */
    
    /** 1.1.1 当前命令前缀 */
    val CURRENT_CMD_PREFIX: ConfigKey<String?> = ConfigKey(
        name = "current_cmd_prefix",
        defaultValue = null,
        scope = ConfigScope.PROCESS,
        description = "当前命令前缀"
    )
}
