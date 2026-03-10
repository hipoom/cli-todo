package com.hipoom.cli.todo.handler.edit

import com.hipoom.cli.core.ui.TextEditor
import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.Configs
import com.hipoom.cli.todo.app
import com.hipoom.cli.todo.entity.item.Item
import com.hipoom.cli.todo.entity.item.copyFromAnotherWithoutChildren
import com.hipoom.cli.todo.gson
import com.hipoom.cli.todo.handler.DeadLineEditor
import com.hipoom.cli.todo.handler.show.ShowOneItemDetail
import com.hipoom.cli.todo.handler.show.show
import com.hipoom.cli.todo.handler.textmapping.persistent.TextMappingStorage
import com.hipoom.cli.todo.itemDao
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options

/**
 * @author ZhengHaiPeng
 * @since 2025/2/2 17:22
 *
 */
class EditHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options: Options = editOptions

    override val supportPrefixes: List<String> = listOf("edit")



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Edit Item"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        when {
            commandLine.hasOption("h") -> printHelp()
            commandLine.hasOption("i") && commandLine.hasOption("a") -> editWithAdvanceMode(app, workspace, commandLine)
            commandLine.hasOption("i") && !commandLine.hasOption("a") -> editWithCliMode(workspace, commandLine)
        }

        return true
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun editWithAdvanceMode(app: CliApp, workspace: WorkspaceContext, commandLine: CommandLine) {
        val id = commandLine.getOptionValue("i")?.toIntOrNull()
        if (id == null) {
            printLine("无法解析 id.")
            return
        }

        workspace.itemDao().useItem(
            id = id,
            ifNotFound = {
                printLine("没有找到 id = $id 的事项")
            },
            onFound = { item ->
                var json = gson.toJson(item)
                // 复制一份转为 json string，再转为 obj，去掉 children
                val removeChildren = gson.fromJson(json, Item::class.java)
                removeChildren.children = null
                if (removeChildren.labels == null) {
                    removeChildren.labels = ArrayList()
                }
                json = gson.toJson(removeChildren)

                val afterEdit = TextEditor.edit(
                    input = json,
                    prompt = "请按照 json 格式编辑内容"
                )
                val editItem = gson.fromJson(afterEdit, Item::class.java)
                item.copyFromAnotherWithoutChildren(editItem)
            }
        )

        app.show()
    }

    private fun editWithCliMode(workspace: WorkspaceContext, commandLine: CommandLine) {
        val id = commandLine.getOptionValue("i")?.toIntOrNull()
        if (id == null) {
            printLine("无法解析 id.")
            return
        }

        workspace.itemDao().useItem(
            id = id,
            ifNotFound = {
                printLine("没有找到 id = $id 的事项。")
                printLine("")
            },
            onFound = { item ->
                // 如果直接指定了带修改项
                val newContent = commandLine.getOptionValue("c")
                val newOwner = commandLine.getOptionValue("o")
                val newDeadline = commandLine.getOptionValue("d")
                val newLabel = commandLine.getOptionValue("l")

                if (newContent != null || newOwner != null || newDeadline != null || newLabel != null) {
                    if (newContent != null) {
                        item.content = TextMappingStorage.applyMappings(app, newContent)
                    }
                    if (newOwner != null) {
                        item.owner = newOwner
                    }
                    if (newDeadline != null) {
                        item.deadline = DeadLineEditor.parseTimestamp(newDeadline)
                    }
                    if (newLabel != null) {
                        item.labels = newLabel.split(",").map { it.trim() }.toMutableList()
                    }
                } else {
                    while(true) {
                        printLine("输入想编辑的项目:")
                        printLine("1: 内容")
                        printLine("2: 负责人")
                        printLine("3: 截止时间")
                        printLine("exit: 退出编辑")
                        printLine("> ", false)
                        val index = readln().trim()
                        when(index) {
                            "1" -> {
                                printLine("当前内容是:")
                                printLine(item.content)

                                // 如果 JFrame 窗口可用，打开 JFrame 窗口
                                if (Configs.window.isEnable) {
                                    printLine("请在新窗口中输入新内容(如果没有出现新的窗口，请使用 edit --disable-window 关闭窗口模式)。")
                                    val newContent2 = TextEditor.edit(
                                        input = item.content ?: "",
                                        prompt = "请输入新内容:"
                                    )
                                    item.content = newContent2
                                }
                                // 否则，在命令行窗口中输入
                                else {
                                    printLine("请输入新内容:")
                                    item.content = TextMappingStorage.applyMappings(app, readln().trim())
                                }
                            }
                            "2" -> {
                                printLine("当前负责人是:")
                                printLine(item.owner)
                                printLine("请输入新负责人:")
                                item.owner = readln().trim()
                            }
                            "3" -> {
                                val newDeadLine = DeadLineEditor.edit()
                                if (newDeadLine != null) {
                                    item.deadline = newDeadLine
                                }
                            }
                            "exit" -> {
                                break
                            }
                        }
                    }
                }

                showOne(item)
            }
        )
    }

    private fun showOne(item: Item?) {
        if (item == null) {
            return
        }
        printLine("")
        printLine("更新后的内容：")
        ShowOneItemDetail.show(item)
    }

}