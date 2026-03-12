package com.hipoom.cli.todo.handler

import com.hipoom.cli.core.ui.TextStyle
import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.AbsHandler
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.scaffold.handler.cmdMappings
import com.hipoom.cli.todo.currentCmdPrefix
import com.hipoom.cli.todo.defaultTextBlockPrinter
import com.hipoom.cli.todo.entity.item.last_modify_item_id
import com.hipoom.cli.todo.readLineWithPrompt
import com.hipoom.cli.todo.expandCmd
import com.hipoom.cli.todo.getFocusId
import com.hipoom.cli.todo.handler.add.AddHandler
import com.hipoom.cli.todo.handler.style.Styles
import com.hipoom.cli.todo.handler.view.getCurrentVirtualView
import com.hipoom.cli.todo.isQuickMode
import com.hipoom.cli.todo.printHint
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.todo.processData
import com.hipoom.cli.todo.reader
import com.hipoom.cli.todo.setFocusId
import com.hipoom.cli.workspace.WorkspaceContext
import org.jline.keymap.KeyMap
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.Reference
import org.jline.reader.UserInterruptException
import org.jline.reader.Widget
import org.jline.reader.impl.completer.StringsCompleter
import org.jline.reader.impl.history.DefaultHistory
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import java.io.IOException


var need_show_last_modified_item = false
var need_show_expand_cmds = false

/**
 * @author ZhengHaiPeng
 * @since 2025/8/2 13:16
 *
 */
class TodoShellHandler: AbsHandler() {


    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    /**
     * 每一行前面的前缀委托。
     * 就是 "$workpsace>" 这个前缀。
     */
    private var prefixDelegate: ((WorkspaceContext)->String?)? = null
    
    /**
     * 是否跳过当前输入的前缀自动填充
     */
    private var skipPrefixForCurrentInput = false



    /* ======================================================= */
    /* Constructors or Instance Creator                        */
    /* ======================================================= */

    init {
        this.prefixDelegate =  { workspace ->
            val currentView = workspace.getCurrentVirtualView()
            if (currentView != null) {
                // 进入视图模式，退出focus模式
                workspace.setFocusId(null)
                "${workspace.workspaceAlias}#${currentView.name}> "
            } else if (workspace.getFocusId() == null) {
                null
            } else {
                "${workspace.workspaceAlias}@${workspace.getFocusId()}> "
            }
        }
    }




    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description(): String {
        return "Shell Mode"
    }

    override fun printHelp() {
        printLine("shell, 进入 shell 模式")
    }

    override fun canHandle(args: String): Boolean {
        return args.startsWith("shell")
    }

    override fun onHandle(
        originParams: String,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        var ws = workspace

        // 初始化 JLine 终端和行读取器
        var terminal: Terminal? = null

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
            
            // 添加 cmdMappings 中的映射
            cmdMappings.mappings.forEach {
                commands.add(it.quick)
            }
            
            // 创建补全器
            val completer = StringsCompleter(commands)

            // 创建行读取器
            reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .history(DefaultHistory()) // 使用默认历史记录
                .completer(completer)
                .build()

            reader.widgets["skip-prefix-widget"] = Widget {
                skipPrefixForCurrentInput = true
                val basePrompt = "${prefixDelegate?.invoke(ws) ?: "${ws.workspaceAlias}> "}"
                val blanks = " ".repeat(currentCmdPrefix?.length ?: 0)
                print("\r${basePrompt}${blanks}")
                // 这里重复一下，是因为要把光标回退到 "> " 后面
                print("\r${basePrompt}")
                true
            }

            // 绑定 Ctrl+B 来临时取消前缀自动填充
            val keyMaps = reader.keyMaps
            val mainKeyMap = keyMaps[LineReader.MAIN]
            mainKeyMap?.bind(
                Reference("skip-prefix-widget"),
                KeyMap.ctrl('B')
            )

            // 设置历史文件（可选）
            // (reader.history as DefaultHistory).setHistoryFile(File("${System.getProperty("user.home")}/.cli_history"))

            var needContinue = true
            
            while (needContinue) {
                skipPrefixForCurrentInput = false

                // 计算默认的 promt 和 cmd prefix
                val basePrefix = prefixDelegate?.invoke(ws) ?: "${ws.workspaceAlias}> "
                val needCmdPrefix = (currentCmdPrefix != null && !skipPrefixForCurrentInput)
                val prefix = if (needCmdPrefix) {
                    "$basePrefix${currentCmdPrefix} "
                } else {
                    basePrefix
                }


                try {
                    val cmd = reader.readLine(prefix).trim()
                    // println("cmd: $cmd")
                    
                    var finalCmd = cmd
                    if (currentCmdPrefix != null && cmd.isNotEmpty() && !skipPrefixForCurrentInput) {
                        finalCmd = "$currentCmdPrefix $cmd"
                    }
                    // println("finalCmd: $finalCmd")

                    val subCmds = expandCmd(finalCmd, mapping = { originCmd ->
                        val temp = cmdMappings.mappings.find {
                            it.quick == originCmd
                        }?.origin ?: originCmd
                        return@expandCmd temp
                    })

                    if (need_show_expand_cmds) {
                        defaultTextBlockPrinter.printHint(text = "展开后的指令: " + subCmds.joinToString(separator = " -> ") { it })
                    }

                    for (subCmd in subCmds) {
                        // 检查是否还有未替换的占位符
                        val placeholderPattern = "\\$\\{\\d+\\}".toRegex()
                        if (placeholderPattern.containsMatchIn(subCmd)) {
                            printLine("【警告】指令展开后存在没有替换的占位符: $subCmd")
                            val yesOrNo = readLineWithPrompt("请选择是否继续执行该指令[yes/no]")
                            if (yesOrNo?.trim() != "yes") {
                                break
                            }
                        }

                        // 跳过空指令
                        if (subCmd.isEmpty()) {
                            continue
                        }

                        // 退出 shell 模式
                        if (subCmd == "exit" || subCmd == "exit()") {
                            needContinue = false
                            break
                        }

                        val tempHandlers = app.getSupportHandlers().filter { it.canHandle(subCmd) }
                        for (handler in tempHandlers) {
                            val isConsumed = handler.handle(
                                originParams = subCmd,
                                app = app,
                                workspace = app.processData.getCurrentWorkspaceContext()
                            )
                            if (isConsumed) {
                                break
                            }
                        }

                        if (tempHandlers.isEmpty()) {
                            if (workspace.isQuickMode()) {
                                // 使用最新的工作区而不是传入的旧工作区
                                AddHandler().onHandle("add $subCmd", app, app.processData.getCurrentWorkspaceContext())
                            } else {
                                printLine("无法处理该指令: '${subCmd}'")
                            }
                        }

                        // 获取当前的工作空间
                        ws = app.processData.getCurrentWorkspaceContext()
                        if (need_show_last_modified_item) {
                            defaultTextBlockPrinter.printHint(text = "? = $last_modify_item_id")
                        }
                    }

                } catch (e: UserInterruptException) {
                    // 用户按下 Ctrl+C 等中断键，不做处理，继续循环
                } catch (e: EndOfFileException) {
                    // 文件结束，退出循环
                    break
                }
            }
        } catch (e: IOException) {
            printLine("终端初始化失败: ${e.message}")
        } finally {
            // 关闭终端资源
            try {
                terminal?.close()
            } catch (e: IOException) {
                // 忽略关闭异常
            }
        }

        return true
    }



}