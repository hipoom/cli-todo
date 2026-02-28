package com.hipoom.cli.todo.handler

import com.hipoom.cli.todo.printLine
import java.util.Calendar

/**
 * @author ZhengHaiPeng
 * @since 2025/2/3 11:50
 *
 */
object DeadLineEditor {

    fun edit(): Long? {
        printLine("请输入截止时间:")
        while (true) {
            printLine("示例1: 14:00, 表示今天的 14:00.")
            printLine("示例2: +1d 14:00, 表示 1 天后的 14:00.")
            printLine("如果输入 exit 退出.")
            printLine("> ", false)
            val temp = readln().trim()
            if (temp.isEmpty()) {
                continue
            }

            if (temp == "exit") {
                return null
            }

            val time = parseTimestamp(temp)
            if (time != 0L) {
                return time
            }

            printLine("呜呜，我看不懂你的输入 T_T")
        }
    }

    fun parseTimestamp(desc: String): Long {
        val type = parseType(desc)

        when(type) {
            1 -> {
                val time = parseAsType1(desc)
                if (time != null) {
                    return time
                }
            }

            2 -> {
                val time = parseAsType2(desc)
                if (time != null) {
                    return time
                }
            }
        }

        return 0
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    /**
     * @return 2: 相对时间； 1: 今天的某个时刻; 负数: 未知。
     */
    private fun parseType(input: String): Int {
        if (input.startsWith("+")) {
            return 2
        }

        if (input.contains(":")) {
            return 1
        }

        return -1
    }

    private fun parseAsType1(str: String): Long? {
        val input = str.replace("：", ":")
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = now
        val splits = input.trim().split(":")
        if (splits.size != 2) {
            return null
        }

        val (hourStr, minuteStr) = splits
        val hour = hourStr.toIntOrNull()
        val minute = minuteStr.toIntOrNull()
        if (hour == null || minute == null) {
            return null
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
    }

    private fun parseAsType2(str: String): Long? {
        val temp = str.trim().removePrefix("+").split("d")
        if (temp.isEmpty() || temp.size > 2) {
            return null
        }

        val day = temp[0].toIntOrNull() ?: return null

        val calendar = Calendar.getInstance()
        
        // 如果有时间部分，则解析时间
        if (temp.size == 2 && temp[1].trim().isNotEmpty()) {
            val timestamp = parseAsType1(temp[1].trim()) ?: return null
            calendar.timeInMillis = timestamp
        } else {
            // 如果没有时间部分，则使用当前时间的时分秒
        }
        
        calendar.add(Calendar.DAY_OF_MONTH, day)
        return calendar.timeInMillis
    }

}