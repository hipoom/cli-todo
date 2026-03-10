package com.hipoom.cli.todo.handler.textmapping.persistent

import com.google.gson.annotations.SerializedName

class TextMappingPair {
    @SerializedName("original")
    var original: String? = null
    
    @SerializedName("replacement")
    var replacement: String? = null
}
