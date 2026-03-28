package com.hipoom.cli.todo.config.keys

import com.hipoom.cli.todo.config.ConfigKey
import com.hipoom.cli.todo.config.ConfigScope
import com.hipoom.cli.todo.config.types.ColorConfig
import com.hipoom.cli.todo.config.validators.ColorValidator

/**
 * 1. Show 相关配置键定义
 * 所有配置键统一在此文件中定义，便于管理和维护
 */
object ShowConfigKeys {
    
    /* ======================================================= */
    /* 1.1 显示控制配置                                        */
    /* ======================================================= */
    
    /** 1.1.1 是否展示事项的 ID */
    val NEED_SHOW_ID = ConfigKey(
        name = "show.needShowId",
        defaultValue = true,
        scope = ConfigScope.WORKSPACE,
        description = "是否展示事项的 ID"
    )
    
    /** 1.1.2 是否展示事项的状态 */
    val NEED_SHOW_STATUS = ConfigKey(
        name = "show.needShowStatus",
        defaultValue = true,
        scope = ConfigScope.WORKSPACE,
        description = "是否展示事项的状态"
    )
    
    /** 1.1.3 是否展示已完成事项 */
    val NEED_SHOW_DONE = ConfigKey(
        name = "show.needShowDone",
        defaultValue = false,
        scope = ConfigScope.WORKSPACE,
        description = "是否展示已被标记完成的事项"
    )
    
    /** 1.1.4 是否展示已删除事项 */
    val NEED_SHOW_DELETED = ConfigKey(
        name = "show.needShowDeleted",
        defaultValue = false,
        scope = ConfigScope.WORKSPACE,
        description = "是否展示已被标记删除的事项"
    )
    
    /** 1.1.5 是否展示负责人 */
    val NEED_SHOW_OWNER = ConfigKey(
        name = "show.needShowOwner",
        defaultValue = true,
        scope = ConfigScope.WORKSPACE,
        description = "是否展示事项的负责人"
    )
    
    /** 1.1.6 是否展示标签 */
    val NEED_SHOW_LABEL = ConfigKey(
        name = "show.needShowLabel",
        defaultValue = true,
        scope = ConfigScope.WORKSPACE,
        description = "是否展示事项的标签"
    )
    
    /** 1.1.7 是否使用高级对齐模式 */
    val USE_ALIGN_MODE = ConfigKey(
        name = "show.useAlignMode",
        defaultValue = false,
        scope = ConfigScope.WORKSPACE,
        description = "是否使用高级对齐模式，仅在控制台字体能够严格保证一个中文字符的宽度是英文字体两倍时使用"
    )
    
    /** 1.1.8 是否展示截止时间 */
    val NEED_SHOW_DEADLINE = ConfigKey(
        name = "show.needShowDeadline",
        defaultValue = true,
        scope = ConfigScope.WORKSPACE,
        description = "是否需要展示截止时间"
    )
    
    /** 1.1.9 是否展示备注 */
    val NEED_SHOW_COMMENT = ConfigKey(
        name = "show.needShowComment",
        defaultValue = true,
        scope = ConfigScope.WORKSPACE,
        description = "是否展示备注"
    )
    
    /** 1.1.10 是否展示备注下标 */
    val NEED_SHOW_COMMENT_SUBSCRIPT = ConfigKey(
        name = "show.needShowCommentSubscript",
        defaultValue = true,
        scope = ConfigScope.WORKSPACE,
        description = "是否展示备注下标"
    )
    
    /* ======================================================= */
    /* 1.2 备注样式配置                                        */
    /* ======================================================= */
    
    /** 1.2.1 备注文字颜色 */
    val COMMENT_TEXT_COLOR = ConfigKey(
        name = "show.commentStyle.textColor",
        defaultValue = ColorConfig(128, 128, 128),
        scope = ConfigScope.WORKSPACE,
        description = "备注文字的颜色，格式是 RGB",
        validator = ColorValidator()
    )
    
    /** 1.2.2 备注背景颜色 */
    val COMMENT_BACKGROUND_COLOR = ConfigKey(
        name = "show.commentStyle.backgroundColor",
        defaultValue = ColorConfig.NONE,
        scope = ConfigScope.WORKSPACE,
        description = "备注文字的背景颜色，格式是 RGB。None 表示使用默认值",
        validator = ColorValidator()
    )
    
    /* ======================================================= */
    /* 1.3 状态图标配置                                        */
    /* ======================================================= */
    
    /** 1.3.1 新事项状态图标 */
    val STATUS_NEW = ConfigKey(
        name = "show.status.new",
        defaultValue = "◌",
        scope = ConfigScope.WORKSPACE,
        description = "状态为 new 的事项怎么展示状态"
    )
    
    /** 1.3.2 进行中状态图标 */
    val STATUS_DOING = ConfigKey(
        name = "show.status.doing",
        defaultValue = "~",
        scope = ConfigScope.WORKSPACE,
        description = "状态为 doing 的事项怎么展示状态"
    )
    
    /** 1.3.3 已完成状态图标 */
    val STATUS_DONE = ConfigKey(
        name = "show.status.done",
        defaultValue = "✔",
        scope = ConfigScope.WORKSPACE,
        description = "状态为 done 的事项怎么展示状态"
    )
    
    /** 1.3.4 已删除状态图标 */
    val STATUS_DELETED = ConfigKey(
        name = "show.status.deleted",
        defaultValue = "×",
        scope = ConfigScope.WORKSPACE,
        description = "状态为 deleted 的事项怎么展示状态"
    )
    
    /* ======================================================= */
    /* 1.4 图标配置                                            */
    /* ======================================================= */
    
    /** 1.4.1 负责人图标 */
    val ICON_OWNER = ConfigKey(
        name = "show.icon.owner",
        defaultValue = "👤",
        scope = ConfigScope.WORKSPACE,
        description = "人物对应的 icon"
    )
    
    /** 1.4.2 标签图标 */
    val ICON_LABEL = ConfigKey(
        name = "show.icon.label",
        defaultValue = "🏷️",
        scope = ConfigScope.WORKSPACE,
        description = "标签对应的 icon"
    )
    
    /* ======================================================= */
    /* 1.5 其他配置                                            */
    /* ======================================================= */
    
    /** 1.5.1 置顶事项背景颜色 */
    val PIN_BACKGROUND_COLOR = ConfigKey(
        name = "show.pinBackgroundColor",
        defaultValue = ColorConfig(200, 200, 200),
        scope = ConfigScope.WORKSPACE,
        description = "置顶事项的背景颜色，格式是 RGB",
        validator = ColorValidator()
    )
}
