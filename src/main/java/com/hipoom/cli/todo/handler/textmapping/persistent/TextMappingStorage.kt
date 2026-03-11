package com.hipoom.cli.todo.handler.textmapping.persistent

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.todo.gson
import com.hipoom.cli.todo.persistentData
import java.text.SimpleDateFormat
import java.util.Date

object TextMappingStorage {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    private val sdf = SimpleDateFormat("yyyy-MM-dd")



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */
    
    fun loadAll(app: CliApp): TextMappingConfigs {
        val json = app.persistentData.loadTextMappings()
        
        if (json.isNullOrEmpty()) {
            val configs = TextMappingConfigs()
            configs.mappings = ArrayList()
            app.persistentData.updateTextMappings(gson.toJson(configs))
            return configs
        } else {
            return gson.fromJson(json, TextMappingConfigs::class.java)
        }
    }
    
    fun addOrReplace(app: CliApp, original: String, replacement: String) {
        val configs = loadAll(app)
        configs.addOrReplace(original, replacement)
        app.persistentData.updateTextMappings(gson.toJson(configs))
    }
    
    fun remove(app: CliApp, original: String): Boolean {
        val configs = loadAll(app)
        val removed = configs.remove(original)
        if (removed) {
            app.persistentData.updateTextMappings(gson.toJson(configs))
        }
        return removed
    }
    
    fun applyMappings(app: CliApp, text: String): String {
        val configs = loadAll(app)
        var result = text
        configs.mappings?.forEach { pair ->
            if (pair.original != null && pair.replacement != null) {
                result = result.replace(pair.original!!, pair.replacement!!)
            }
        }

        // 替换日期
        result = result.replace("\${date}", sdf.format(Date(System.currentTimeMillis())))

        return result
    }
}
