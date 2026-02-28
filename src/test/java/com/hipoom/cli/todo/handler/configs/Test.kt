package com.hipoom.cli.todo.handler.configs

import com.hipoom.cli.todo.gson
import com.hipoom.cli.todo.handler.config.default
import org.junit.jupiter.api.Test

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 21:37
 *
 */
class Test {

    @Test
    fun test() {
        println(gson.toJson(default))
    }

}