package com.hipoom.cli.todo.handler.textmapping.persistent

import com.google.gson.annotations.SerializedName

class TextMappingConfigs {
    @SerializedName("mappings")
    var mappings: ArrayList<TextMappingPair>? = null
    
    fun find(original: String): TextMappingPair? {
        return mappings?.find { it.original == original }
    }
    
    fun addOrReplace(original: String, replacement: String) {
        mappings?.removeIf { it.original == original }
        if (mappings == null) {
            mappings = ArrayList()
        }
        mappings?.add(TextMappingPair().apply {
            this.original = original
            this.replacement = replacement
        })
    }
    
    fun remove(original: String): Boolean {
        return mappings?.removeIf { it.original == original } ?: false
    }
}
