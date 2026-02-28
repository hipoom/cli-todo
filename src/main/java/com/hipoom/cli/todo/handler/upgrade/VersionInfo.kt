package com.hipoom.cli.todo.handler.upgrade

import com.google.gson.annotations.SerializedName

data class VersionInfo(
    @SerializedName("version")
    val version: String,
    
    @SerializedName("versionCode")
    val versionCode: Int,
    
    @SerializedName("releaseDate")
    val releaseDate: String,
    
    @SerializedName("downloadUrl")
    val downloadUrl: String,
    
    @SerializedName("releaseNotes")
    val releaseNotes: String,
    
    @SerializedName("forceUpdate")
    val forceUpdate: Boolean = false
)
