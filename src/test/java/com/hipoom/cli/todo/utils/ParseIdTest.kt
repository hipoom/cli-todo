package com.hipoom.cli.todo.utils

import org.junit.jupiter.api.Test

/**
 * @author ZhengHaiPeng
 * @since 2025/3/6 2:12
 *
 */


class ParseIdTest {

    @Test
    fun test() {
        listOf(
            "1",
            "1,2",
            "1 2",
            "1,2 3",
            "1,2..5",
            "1..4",
            "1,2..4,5 6",
            "2..6,7 8"
        ).forEach {
            println(it)
            it.parseIds().also { ids ->
                println("operates: " + ids.operators.joinToString { o -> o.toString() })
                println("target: " + ids.target)
            }
            println()
        }
    }

}