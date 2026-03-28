package com.hipoom.cli.todo

import com.hipoom.cli.core.ui.palette.Color
import com.hipoom.cli.core.ui.palette.Colors
import com.hipoom.cli.scaffold.config.ConfigGroup
import com.hipoom.cli.todo.handler.config.default
import com.hipoom.cli.workspace.WorkspaceContext
import com.hipoom.files.child
import com.hipoom.files.createNewFileIfNotExist
import kotlin.reflect.KProperty

/**
 * 这个文件，记录着和 Workspace 绑定的各种配置。
 * 所有属于某个 workspace 的配置，都应该放到这里。
 */


/**
 * 工作空间变更的回调。 用于更新配置。
 */
fun updateConfigOnWorkspaceChanged(workspace: WorkspaceContext) {
    val file = workspace.workspaceDir.child("configs.json")
    file.createNewFileIfNotExist {
        val json = gson.toJson(default)
        writeText(json)
    }
    currentConfigs = gson.fromJson(file.readText(), ConfigGroup::class.java)
}

/**
 * 当前的配置信息。
 */
lateinit var currentConfigs: ConfigGroup

/**
 * 将当前的配置保存到当前的工作空间中。
 */
fun WorkspaceContext.storeCurrentConfigs() {
    val file = workspaceDir.child("configs.json")
    val json = gson.toJson(currentConfigs)
    file.writeText(json)
}


object Configs {
    val window = Window
    val show = Show
    val launch = Launch
}

object Window {

    private val window: ConfigGroup
        get() {
            val temp = currentConfigs["window"]
                ?: throw IllegalStateException("当前配置没有 window 相关的配置！")
            return temp
        }

    /**
     * 全局窗口模式是否开启。
     */
    var isEnable by BooleanField({ window }, "isEnable", true)


}

object Show {

    private val show: ConfigGroup
        get() {
            val temp = currentConfigs["show"]
                ?: throw IllegalStateException("当前配置没有 show 相关的配置！")
            return temp
        }

    var needShowId by BooleanField({show}, "needShowId", true)
    var needShowStatus by BooleanField({show}, "needShowStatus", true)
    var needShowDone by BooleanField({show}, "needShowDone", true)
    var needShowDeleted by BooleanField({show}, "needShowDeleted", false)
    var needShowOwner by BooleanField({show}, "needShowOwner", true)
    var needShowLabel by BooleanField({show}, "needShowLabel", true)
    var useAlignMode by BooleanField({show}, "useAlignMode", false)
    var needShowDeadline by BooleanField({show}, "needShowDeadline", true)
    var needShowCommentSubscript by BooleanField({show}, "needShowCommentSubscript", true)
    var needShowComment by BooleanField({show}, "needShowComment", true)

    val commentStyle = CommentStyle

    object CommentStyle {
        private val commentStyle: ConfigGroup
            get() {
                var temp = show["commentStyle"]
                if (temp == null) {
                    printLine("当前配置没有 show.commentStyle 相关的配置，已自动从默认配置中读取。")
                    show.add(default["show", "commentStyle"]!!)
                    app.processData.getCurrentWorkspaceContext().storeCurrentConfigs()
                    temp = show["commentStyle"]
                }
                return temp!!
            }

        fun getTextColor(): Color? {
            val color = commentStyle.getString("textColor", "None")
            val splits = color.split(",").mapNotNull { it.trim().toIntOrNull() }
            if (splits.size != 3) {
                return null
            }
            return Colors.Bits24.createForeground(splits[0], splits[1], splits[2])
        }

        fun getBackgroundColor(): Color? {
            val color = commentStyle.getString("backgroundColor", "None")
            val splits = color.split(",").mapNotNull { it.trim().toIntOrNull() }
            if (splits.size != 3) {
                return null
            }
            return Colors.Bits24.createForeground(splits[0], splits[1], splits[2])
        }

        fun setTextColor(color: String) {
            var config = commentStyle.configs?.find { it.name == "textColor" }
            if (config == null) {
                config = com.hipoom.cli.scaffold.config.ConfigGroup(
                    name = "textColor",
                    desc = "备注文字的颜色，格式是 RGB，例如 255,0,0 表示红色。",
                    value = color
                )
                commentStyle.configs?.add(config)
            } else {
                config.value = color
            }
        }

        fun setBackgroundColor(color: String) {
            var config = commentStyle.configs?.find { it.name == "backgroundColor" }
            if (config == null) {
                config = com.hipoom.cli.scaffold.config.ConfigGroup(
                    name = "backgroundColor",
                    desc = "备注文字的背景颜色，格式是 RGB，例如 255,0,0 表示红色。None 表示使用默认值。",
                    value = color
                )
                commentStyle.configs?.add(config)
            } else {
                config.value = color
            }
        }

    }

    val status = Status

    object Status {
        private val status: ConfigGroup
            get() {
                var temp = show["status"]
                if (temp == null) {
                    // printLine("当前配置没有 show.status 相关的配置，已自动从默认配置中读取。")
                    show.add(default["show", "status"]!!)
                    app.processData.getCurrentWorkspaceContext().storeCurrentConfigs()
                    temp = show["status"]
                }
                return temp!!
            }

        fun get(name: String): String {
            return status.getString(name, "?")
        }
    }

    val icon = Icon

    object Icon {
        private val icon: ConfigGroup
            get() {
                var temp = show["icon"]
                if (temp == null) {
                    // printLine("当前配置没有 show.icon 相关的配置，已自动从默认配置中读取。")
                    show.add(default["show", "icon"]!!)
                    app.processData.getCurrentWorkspaceContext().storeCurrentConfigs()
                    temp = show["icon"]
                }
                return temp!!
            }

        fun get(name: String): String {
            return icon.getString(name, "[${name}]")
        }
    }

    fun setPinBackgroundColor(color: String) {
        var config = show.configs?.find { it.name == "pinBackgroundColor" }
        if (config == null) {
            config = com.hipoom.cli.scaffold.config.ConfigGroup(
                name = "pinBackgroundColor",
                desc = "置顶事项的背景颜色，格式是 RGB，例如 255,0,0 表示红色。",
                value = color
            )
            show.configs?.add(config)
        } else {
            config.value = color
        }
    }

    fun getPinBackgroundColor(): Color? {
        val color = show.getString("pinBackgroundColor", "200,200,200")
        val splits = color.split(",").mapNotNull { it.trim().toIntOrNull() }
        if (splits.size != 3) {
            return null
        }
        return Colors.Bits24.createForeground(splits[0], splits[1], splits[2])
    }

    /**
     * 获取当前样式名称
     * @deprecated 请使用 StyleStorage.loadAll(app).currentStyleName 或 ConfigManager
     */
    @Deprecated(
        message = "请使用 StyleStorage.loadAll(app).currentStyleName 获取当前样式名称",
        replaceWith = ReplaceWith("StyleStorage.loadAll(app).currentStyleName", "com.hipoom.cli.todo.handler.style.persistent.StyleStorage")
    )
    fun getCurrentStyle(): String? {
        val style = show.getString("currentStyle", "")
        return if (style.isEmpty()) null else style
    }

    /**
     * 设置当前样式名称
     * @deprecated 请使用 StyleStorage 或 ConfigManager
     */
    @Deprecated(
        message = "请使用 StyleStorage 或 ConfigManager 设置当前样式名称",
        replaceWith = ReplaceWith("// 请迁移到新的配置系统", "")
    )
    fun setCurrentStyle(styleName: String) {
        var config = show.configs?.find { it.name == "currentStyle" }
        if (config == null) {
            config = com.hipoom.cli.scaffold.config.ConfigGroup(
                name = "currentStyle",
                desc = "当前选择的样式名称",
                value = styleName
            )
            show.configs?.add(config)
        } else {
            config.value = styleName
        }
    }
}

object Launch {

    private val launch: ConfigGroup
        get() {
            var temp = currentConfigs["launch"]
            if (temp == null) {
                // printLine("当前配置没有 launch 相关的配置，已自动从默认配置中读取。")
                currentConfigs.add(default["launch"]!!)
                app.processData.getCurrentWorkspaceContext().storeCurrentConfigs()
                temp = currentConfigs["launch"]
            }
            return temp!!
        }

    /**
     * 全局窗口模式是否开启。
     */
    var needShowOnLaunch by BooleanField({ launch }, "needShowOnLaunch", true)

}

object Focus {
    private val focus: ConfigGroup
        get() {
            var temp = currentConfigs["focus"]
            if (temp == null) {
                temp = ConfigGroup(
                    name = "focus",
                    desc = "关于聚焦事项",
                    value = null,
                    configs = mutableListOf(
                        ConfigGroup(
                            name = "id",
                            desc = "当前聚焦的事项",
                            value = null
                        )
                    )
                )
                if (currentConfigs.configs == null) {
                    currentConfigs.configs = mutableListOf()
                }

                currentConfigs.configs!!.add(temp)
            }
            return temp
        }

    /**
     * 全局窗口模式是否开启。
     */
    var id by BooleanField({ focus }, "id", true)
}

class BooleanField(private val getConfig: ()->ConfigGroup, private val name: String, private val default: Boolean) {

    operator fun getValue(self: Any?, property: KProperty<*>): Boolean {
        return getConfig().getBoolean(name, default)
    }

    operator fun setValue(self: Any?, property: KProperty<*>, value: Boolean) {
        var config = getConfig().configs?.find { it.name == name }
        if (config == null) {
            config = ConfigGroup(
                name = name,
                desc = "",
                value = value
            )
            getConfig().configs?.add(config)
        }
        else {
            config.value = value
        }

        app.processData.getCurrentWorkspaceContext().storeCurrentConfigs()
    }
}
