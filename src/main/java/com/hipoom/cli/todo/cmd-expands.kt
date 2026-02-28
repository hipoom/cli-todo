package com.hipoom.cli.todo

import com.hipoom.cli.scaffold.utils.replacePlaceholders


/**
 * 替换日期占位符。
 */
private fun replaceDatePlaceHolder(cmd: String): String {
    // 如果不包含 ${date}，返回原始的命令字符串
    if (!cmd.contains("\${date}")) {
        return cmd
    }

    // 如果包含，则替换后返回
    val currentDate = java.time.LocalDate.now()
    val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy年MM月dd日")
    val dateString = currentDate.format(formatter)
    return cmd.replace("\${date}", dateString)
}

private fun needExpand(cmd: String, mapping: (String)->String): Boolean {
    val placeholderPattern = "\\$\\{\\d+\\}".toRegex()
    val hasNumberPlaceHolder = placeholderPattern.containsMatchIn(cmd)
    if (hasNumberPlaceHolder) {
        return true
    }

    val hasCmdSeparator = cmd.contains(" && ")
    if (hasCmdSeparator) {
        return true
    }

    val f = cmd.split(" ")[0]
    if (f != mapping(f)) {
        return true
    }

    return false
}


/**
 * 将 [originCmd] 指令展开，并替换所有的 ${num}、 ${date} 占位符，但是不会替换首个子指令以外的 ${?}.
 * 例如:  edit -i ${?} && focus ${?} 指令， 第一个 ${?} 会被替换，但是第二个不会。
 */
fun expandCmd(originCmd: String, mapping: (String)->String): List<String> {
    // 去掉前后的空格
    var cmd = originCmd.trim()
    if (cmd.isEmpty()) {
        return listOf(cmd)
    }

    // 替换日期占位符
    cmd = replaceDatePlaceHolder(cmd)

    // 如果不需要展开，直接返回
    if (!needExpand(cmd, mapping)) {
        return listOf(cmd)
    }

    // 如果原始指令包含多个子指令，拆分为多个指令分别处理
    val placeholderPattern = "\\$\\{\\d+\\}".toRegex()
    val hasNumberPlaceHolder = placeholderPattern.containsMatchIn(cmd)
    val hasCmdSeparator = cmd.contains(" && ")

    // 一个命令，不应该同时含有 && 和 ${i}
    if (hasNumberPlaceHolder && hasCmdSeparator) {
        return listOf()
    }

    // 如果有数字占位符，替换占位符
    if (hasNumberPlaceHolder) {
        val splits = cmd.split(" ").toMutableList()
        val prefix = splits.first()

        // 去掉前缀的剩余指令
        val withoutPrefix = cmd.removePrefix(prefix).trim()

        // 替换完数字占位符的指令
        cmd = replacePlaceholders(cmd, withoutPrefix)
    }

    // 如果包含子命令，拆分
    if (hasCmdSeparator) {
        val res = ArrayList<String>()
        cmd.split(" && ").forEach {
            res.addAll(expandCmd(it, mapping))
        }
        return res
    }

    // 既不包含 &&， 也不包含 ${i} 占位符， 则展开 mapping
    val splits = ArrayList(cmd.split(" "))
    val first = splits[0]
    val splits0 = mapping(first)
    // 如果 first 无法展开了，直接返回 cmd
    if (splits0 == first) {
        return listOf(cmd)
    }

    // 替换完数字占位符的指令
    splits.removeFirst()
    val newCmd = replacePlaceholders(splits0, splits.joinToString(separator = " ") { it })

    val expandCmds = expandCmd(newCmd, mapping)

    val cmds = ArrayList<String>()
    expandCmds.forEach {
        cmds.addAll(expandCmd(it, mapping))
    }

    return cmds
}


fun main() {
    val asserts = mapOf(
        "done 1"            to "mark -d -i 1",
        "done 1 && done 2"  to "mark -d -i 1 && mark -d -i 2",
        "fd 1"              to "focus -i 1 && mark -d -i 1"
    )

    asserts.forEach { (input, expect) ->
        val output = expandCmd(input) { cmd ->
            when (cmd) {
                "done" -> "mark -d -i \${1}"
                "fi" -> "focus -i \${1}"
                "fd" -> "fi \${1} && done \${1}"
                else -> cmd
            }
        }.joinToString(separator = " && ") { it }
        printLine("input: $input, output: $output, expect: $expect")
    }
}