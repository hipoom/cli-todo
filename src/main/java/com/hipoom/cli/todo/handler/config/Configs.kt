package com.hipoom.cli.todo.handler.config

import com.hipoom.cli.scaffold.config.ConfigGroup
import com.hipoom.cli.todo.entity.item.Item

val default = ConfigGroup(
    name = "configs",
    desc = "应用的所有配置信息",
    configs = mutableListOf(
        ConfigGroup(
            name = "show",
            desc = "展示效果的配置",
            configs = mutableListOf(
                ConfigGroup(
                    name = "needShowDeleted",
                    desc = "是否展示已被标记删除的事项",
                    value = false
                )
                ,
                ConfigGroup(
                    name = "needShowDone",
                    desc = "是否展示已被标记完成的事项",
                    value = false
                )
                ,
                ConfigGroup(
                    name = "needShowId",
                    desc = "是否展示事项的 id",
                    value = true
                )
                ,
                ConfigGroup(
                    name = "needShowStatus",
                    desc = "是否展示事项的状态",
                    value = true
                )
                ,
                ConfigGroup(
                    name = "needShowOwner",
                    desc = "是否展示事项的负责人",
                    value = true
                )
                ,
                ConfigGroup(
                    name = "needShowLabel",
                    desc = "是否展示事项的标签",
                    value = true
                )
                ,
                ConfigGroup(
                    name = "useAlignMode",
                    desc = "是否使用高级对齐模式，仅在控制台字体能够严格保证一个中文字符的宽度是英文字体两倍时使用。",
                    value = false
                )
                ,
                ConfigGroup(
                    name = "needShowDeadline",
                    desc = "是否需要展示截止时间",
                    value = true
                )
                ,
                ConfigGroup(
                    name = "commentStyle",
                    desc = "备注展示的风格",
                    configs = mutableListOf(
                        ConfigGroup(
                            name = "textColor",
                            desc = "备注文字的颜色，格式是 RGB，例如 255,0,0 表示红色。",
                            value = "128,128,128"
                        )
                        ,
                        ConfigGroup(
                            name = "backgroundColor",
                            desc = "备注文字的背景颜色，格式是 RGB，例如 255,0,0 表示红色。None 表示使用默认值。",
                            value = "None"
                        )
                    )
                )
                ,
                ConfigGroup(
                    name = "status",
                    desc = "状态展示映射，各 value 的宽度需要一直，否则展示会有异常。",
                    configs = mutableListOf(
                        ConfigGroup(
                            name = Item.STATUS_NEW,
                            desc = "状态为 ${Item.STATUS_NEW} 的事项怎么展示状态",
                            value = "◌"
                        )
                        ,
                        ConfigGroup(
                            name = Item.STATUS_DOING,
                            desc = "状态为 ${Item.STATUS_DOING} 的事项怎么展示状态",
                            value = "~"
                        )
                        ,
                        ConfigGroup(
                            name = Item.STATUS_DONE,
                            desc = "状态为 ${Item.STATUS_DONE} 的事项怎么展示状态",
                            value = "✔"
                        )
                        ,
                        ConfigGroup(
                            name = Item.STATUS_DELETED,
                            desc = "状态为 ${Item.STATUS_DELETED} 的事项怎么展示状态",
                            value = "×"
                        )
                    )
                )
                ,
                ConfigGroup(
                    name = "icon",
                    desc = "人物、标签等内容在展示时的 icon 配置.",
                    configs = mutableListOf(
                        ConfigGroup(
                            name = "owner",
                            desc = "人物对应的 icon",
                            value = "\uD83D\uDC64"
                        )
                        ,
                        ConfigGroup(
                            name = "label",
                            desc = "标签对应的 icon",
                            value = "\uD83C\uDFF7\uFE0F"
                        )
                    )
                )
            )
        )
        ,
        ConfigGroup(
            name = "window",
            desc = "窗口模式的配置",
            configs = mutableListOf(
                ConfigGroup(
                    name = "isEnable",
                    desc = "是否在编辑和创建模板时使用窗口模式，而不是在命令行中",
                    value = true
                )
            )
        )
        ,
        ConfigGroup(
            name = "launch",
            desc = "启动相关的配置",
            configs = mutableListOf(
                ConfigGroup(
                    name = "needShowOnLaunch",
                    desc = "是否需要在启动时自动展示一次",
                    value = true
                )
            )
        )
    )
)