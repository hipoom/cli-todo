package com.hipoom.cli.todo.handler.style.persistent

import com.google.gson.Gson
import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.todo.TodoApp
import com.hipoom.cli.todo.gson
import com.hipoom.cli.todo.handler.style.Styles
import com.hipoom.cli.todo.handler.style.pojo.Style
import com.hipoom.cli.todo.persistentData
import java.util.ArrayList

/**
 * @author ZhengHaiPeng
 * @since 2026/3/1 15:29
 */
object StyleStorage {

    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 加载所有的样式配置
     */
    fun loadAll(app: CliApp): StyleConfigs {
        val json = app.persistentData.loadStyles()

        // 如果没有配置，就创建一个默认的配置
        if (json.isNullOrEmpty()) {
            val configs = StyleConfigs()
            configs.name2Style = ArrayList()

            // 添加默认的样式配置
            configs.addOrReplace("亮色方案", Styles.light)
            configs.addOrReplace("暗色方案", Styles.dark)


//            run {
//                val color = Styles.light.secondaryTextColor
//                val json1 = Gson().toJson(color)
//                val json2 = gson.toJson(color)
//
//                val color2: Style? = configs.name2Style!![0].style
//                val json3 = gson.toJson(color2)
//                println()
//            }


            // 保存到持久化数据中
            val json = gson.toJson(configs)
            app.persistentData.updateStyles(json)
            return configs
        } else {
            return gson.fromJson(json, StyleConfigs::class.java)
        }
    }

    /**
     * 添加或替换一个样式配置
     */
    fun addOrReplaceStyle(app: CliApp, name: String, style: Style) {
        // 从持久化数据中加载所有的样式配置
        val configs = loadAll(app)
        // 添加或替换新的样式配置
        configs.addOrReplace(name, style)
        // 保存到持久化数据中
        app.persistentData.updateStyles(gson.toJson(configs))
    }

    fun useStyle(app: CliApp, name: String) {
        // 从持久化数据中加载所有的样式配置
        val configs = loadAll(app)
        configs.currentStyleName = name
        // 保存到持久化数据中
        app.persistentData.updateStyles(gson.toJson(configs))
    }

}