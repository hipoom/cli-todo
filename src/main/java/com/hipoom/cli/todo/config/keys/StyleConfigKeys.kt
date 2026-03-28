package com.hipoom.cli.todo.config.keys

import com.hipoom.cli.todo.config.ConfigKey
import com.hipoom.cli.todo.config.ConfigScope

/**
 * Style 相关配置键定义
 * 样式配置属于 App 级别，跨工作空间共享
 */
object StyleConfigKeys {
    
    /* ======================================================= */
    /* 样式配置                                                */
    /* ======================================================= */
    
    /** 当前样式名称 */
    val CURRENT_STYLE_NAME = ConfigKey(
        name = "current_style_name",
        defaultValue = "亮色方案",
        scope = ConfigScope.APP,
        description = "当前选择的样式名称"
    )
    
    /** 样式配置 JSON */
    val STYLES = ConfigKey(
        name = "styles",
        defaultValue = "",
        scope = ConfigScope.APP,
        description = "所有样式配置的 JSON 字符串"
    )
    
    /** 文本映射配置 JSON */
    val TEXT_MAPPINGS = ConfigKey(
        name = "text_mappings",
        defaultValue = "",
        scope = ConfigScope.APP,
        description = "文本映射配置的 JSON 字符串"
    )
}
