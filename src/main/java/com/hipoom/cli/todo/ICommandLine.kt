package com.hipoom.cli.todo

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.impl.completer.StringsCompleter
import org.jline.reader.impl.history.DefaultHistory
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder

/**
 * @author ZhengHaiPeng
 * @since 2025/11/23 12:56
 *
 */
interface ICommandLine {

    /**
     * 输出到控制台上。
     */
    fun print(msg: Any? = null, needNewLine: Boolean = true)

    /**
     * 读取用户的输入。
     */
    fun readLine(prompt: String? = null): String

}




/**
 * 默认的控制台输入输出。
 */
class DefaultCommandLine: ICommandLine {

    override fun print(msg: Any?, needNewLine: Boolean) {
        if (msg == null) {
            println()
            return
        }

        if (needNewLine) {
            println(msg)
        } else {
            kotlin.io.print(msg)
        }
    }

    override fun readLine(prompt: String?): String {
        return readln()
    }

}

class JLineReader(val app: CliApp): ICommandLine {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    // 初始化 JLine 终端和行读取器
    var terminal: Terminal? = null
    var reader: LineReader? = null



    /* ======================================================= */
    /* Constructors or Instance Creator                        */
    /* ======================================================= */

    init {
        try {
            // 创建终端
            terminal = TerminalBuilder.builder()
                .system(true)
                .build()

            // 字符串补全
            val handlers = app.getSupportHandlers()
            val commands = mutableListOf<String>()
            handlers.forEach { handler ->
                if (handler is ApacheCliOptionHandler) {
                    commands.addAll(handler.supportPrefixes)
                } else {
                    // 添加其他处理器的命令
                    if (handler.canHandle("help")) {
                        commands.add("help")
                    }
                    if (handler.canHandle("workspace")) {
                        commands.add("workspace")
                    }
                    if (handler.canHandle("shell")) {
                        commands.add("shell")
                    }
                }
            }

            // 创建补全器
            val completer = StringsCompleter(commands)

            // 创建行读取器
            reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .history(DefaultHistory()) // 使用默认历史记录
                .completer(completer)
                .build()
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun print(msg: Any?, needNewLine: Boolean) {
        if (msg == null) {
            println()
            return
        }

        if (needNewLine) {
            println(msg)
        } else {
            kotlin.io.print(msg)
        }
    }

    override fun readLine(prompt: String?): String {
        return reader?.readLine(prompt)?.trim() ?: ""
    }

}