@file:Suppress("KotlinConstantConditions")

package com.hipoom.cli.todo.utils

import com.hipoom.cli.scaffold.utils.removeContinuousEmpty
import com.hipoom.cli.scaffold.utils.removeEmptyCharAfterSeparator
import com.hipoom.cli.scaffold.utils.removeEmptyCharBeforeSeparator
import com.hipoom.cli.scaffold.utils.removeEmptyStrings
import com.hipoom.cli.todo.printLine


data class Ids(
    val operators: List<Int>,
    val target: Int?
)

/**
 * 多种类型的输入：
 * 3
 * 3 9
 * 1,2,3 9
 * 1..10
 * 1..10 11
 */
fun String?.parseIds(): Ids {
    // 将输入规范化
    val trimmed: String = (this ?: "").trim()
        .removeContinuousEmpty()
        .removeEmptyCharAfterSeparator()
        .removeEmptyCharBeforeSeparator()

    if (trimmed.isEmpty()) {
        return Ids(emptyList(), null)
    }

    val hasRange = trimmed.contains("..") || trimmed.contains("~")
    if (hasRange) {
        val splits = trimmed.split(" ").toMutableList()
        val t = splits[0].expandNumbers()
        splits[0] = t
        val new = splits.joinToString(" ")
        return new.parseIds()
    }

    val hasSeparator = trimmed.contains(",")
    val hasBlank = trimmed.contains(" ")

    // 如果既没有 ',' 也没有 ' ', 说明只有一个数字
    if (!hasBlank && !hasSeparator) {
        val id = trimmed.toIntOrNull()
        return if (id == null) {
            Ids(emptyList(), null)
        } else {
            Ids(listOf(id), null)
        }
    }

    // 如果有 ' '，但没有 ','
    if (hasBlank && !hasSeparator) {
        val ids = trimmed.split(" ").removeEmptyStrings().mapNotNull { it.toIntOrNull() }
        if (ids.isEmpty()) {
            return Ids(emptyList(), null)
        }

        if (ids.size == 1) {
            return Ids(listOf(ids[0]), null)
        }

        val target = ids.last()
        val operators = ids.toMutableList()
        operators.removeLast()
        return Ids(operators, target)
    }

    // 如果有 ',' 但没有 ' ', 说明没有输入 target
    if (hasSeparator && !hasBlank) {
        val operators = trimmed
            .split(",")
            .removeEmptyStrings() // 防止输入 1,,2 这种
            .map { it.trim() }    // 防止输入 1, 2, 3 这种 , 后面跟了空格的情况
            .mapNotNull { it.toIntOrNull() }
        return Ids(operators, null)
    }

    // 既有 ' ', 也有 ','
    val target = trimmed.split(" ").last().toIntOrNull()
    if (target == null) {
        printLine("你的输入好奇怪哦 o.o")
        // 这是一种异常
        return Ids(emptyList(), null)
    }

    val operatorsWithSeparator = trimmed.removeSuffix(target.toString())
    val operators = operatorsWithSeparator
        .split(",")
        .removeEmptyStrings() // 防止输入 1,,2 这种
        .map { it.trim() }    // 防止输入 1, 2, 3 这种 , 后面跟了空格的情况
        .mapNotNull { it.toIntOrNull() }

    return Ids(operators, target)
}

/**
 * 需要确保 [this] 不包含空格等其它字符，
 * [this] 只能是以下这些情况：
 * 1
 * 1,2
 * 1,2..5
 * 3..7
 * 等。
 * 注意不能是  1,2..5,6 8 这种含空格的情况。
 */
fun String.expandNumbers(): String {
    val parts = split(',')
    val result = mutableListOf<String>()

    for (part in parts) {
        if (".." in part) {
            val (start, end) = part.split("..")
            val startNum = start.toInt()
            val endNum = end.toInt()
            val expanded = (startNum..endNum).map { it.toString() }
            result.addAll(expanded)
        }
        else if ("~" in part) {
            val (start, end) = part.split("~")
            val startNum = start.toInt()
            val endNum = end.toInt()
            val expanded = (startNum..endNum).map { it.toString() }
            result.addAll(expanded)
        }
        else {
            result.add(part)
        }
    }

    return result.joinToString(",")
}


/**
 * 转为等长的星号（*）
 */
fun String.toAsterisks(): String {
    val sb = StringBuilder()

    for (i in indices) {
        sb.append("*")
    }

    return sb.toString()
}

fun String?.appendOrNull(suffix: String): String? {
    if (this == null) {
        return null
    }

    return this + suffix
}


fun List<String>.containsAny(keywords: List<String>): Boolean {
    for (keyword in keywords) {
        if (this.contains(keyword)) {
            return true
        }
    }
    return false
}

fun number2Subscript(num: Int): String {
    if (num <= 0 || num > 9) {
        return "";
    }
    return "¹²³⁴⁵⁶⁷⁸⁹"[num - 1].toString()
}

val number2Subscript = mapOf(
    // ¹²³⁴⁵⁶⁷⁸⁹
    "1" to "¹",
)