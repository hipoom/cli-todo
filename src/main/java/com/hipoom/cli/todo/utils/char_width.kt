package com.hipoom.cli.todo.utils

import com.hipoom.cli.todo.printLine

fun String.forEachCodePoint(callback: (Int)->Unit) {
    val codePointCount = codePointCount(0, length)
    for (i in 0 until codePointCount) {
        val point = codePointAt(i)
        callback(point)
    }
}

fun String.displayWidth(): Int {
    var sum = 0
    var preCode = 0
    forEachCodePoint { code ->
        // 特殊情况
        if (code == 56785 && preCode == 128465) {
            sum += 0
        }
        // 中文字符
        else if (code in '\u4E00'.code..'\u9FFF'.code) {
            sum += 2
        }
        // 全角标点符号
        else if (code in '\u3000'.code .. '\u303F'.code) {
            sum += 2
        }
        // 全角字母、数字
        else if (code in '\uFF00'.code..'\uFFEF'.code) {
            sum += 2
        }
        // 占据两个字符宽度的 emoji，这里不准确，遇到问题再改
        else if (code in 127300 .. 129500 || code == 9203 || code in 9193..9196 || code == 9200 || code in 9472..9547) {
            sum += 2
        }
        else {
            sum += 1
        }
        preCode = code
    }
    return sum
}


fun main() {
    "\uD83D\uDC64".also {
        it.forEachCodePoint { code ->
            printLine(code)
        }
        printLine(it)
        printLine(it.displayWidth())
        printLine("----")
    }


//    for (i in 9103 .. 9803) {
//        print(i)
//        print(": ")
//        Character.toChars(i).also {
//            printLine(it)
//        }
//    }



//    val callback: (String)->Unit = {
//        printLine(it)
//        printLine("displayWidth: " + it.displayWidth())
//        printLine(it.appendEmpty(25) + "'")
//    }
//
//    callback("|-- 这是一条中文事项")
//    callback(".   `-- this is 3's child")
}