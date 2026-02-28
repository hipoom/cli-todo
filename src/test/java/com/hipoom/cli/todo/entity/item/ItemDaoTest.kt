package com.hipoom.cli.todo.entity.item

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

/**
 * @author ZhengHaiPeng
 * @since 2025/3/2 15:00
 */
class ItemDaoTest {

    @Test
    fun insert() {
        val file = File.createTempFile("hipoom-todo-", ".json")
        file.deleteOnExit()
        println("path: $file")
        val dao = ItemDao(file.absolutePath)


        val item = Item(
            content = "第一个"
        )
        dao.insert(item)

        println()
    }


}