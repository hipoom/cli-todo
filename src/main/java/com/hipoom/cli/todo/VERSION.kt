package com.hipoom.cli.todo

const val VERSION_NAME = "1.0.11"

val VERSION_CODE: Long
    get() {
        return VERSION_NAME.split(".")
            .map { it.toLong() }
            .reduce { acc, i -> acc * 100 + i }
    }