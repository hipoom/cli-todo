package com.hipoom.cli.todo

import com.hipoom.cli.core.timer.ForEachMinute
import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.AbsHandler
import com.hipoom.cli.scaffold.handler.HelpHandler
import com.hipoom.cli.scaffold.handler.QuickCmdHandler
import com.hipoom.cli.scaffold.handler.WorkspaceHandler
import com.hipoom.cli.scaffold.handler.cmdMappings
import com.hipoom.cli.scaffold.handler.plugin.PluginHandler
import com.hipoom.cli.scaffold.utils.replacePlaceholders
import com.hipoom.cli.todo.entity.item.Item
import com.hipoom.cli.todo.entity.item.last_modify_item_id
import com.hipoom.cli.todo.handler.add.AddHandler
import com.hipoom.cli.todo.handler.CommentHandler
import com.hipoom.cli.todo.handler.TodoShellHandler
import com.hipoom.cli.todo.handler.cmdprefix.CmdPrefixHandler
import com.hipoom.cli.todo.handler.collapse.CollapseHandler
import com.hipoom.cli.todo.handler.delete.DeleteHandler
import com.hipoom.cli.todo.handler.detail.DetailHandler
import com.hipoom.cli.todo.handler.edit.EditHandler
import com.hipoom.cli.todo.handler.focus.FocusHandler
import com.hipoom.cli.todo.handler.group.GroupHandler
import com.hipoom.cli.todo.handler.mark.MarkHandler
import com.hipoom.cli.todo.handler.move.MoveHandler
import com.hipoom.cli.todo.handler.config.ConfigHandler
import com.hipoom.cli.todo.handler.developer.DevelopHandler
import com.hipoom.cli.todo.handler.expand.ExpandHandler
import com.hipoom.cli.todo.handler.find.FindHandler
import com.hipoom.cli.todo.handler.label.LabelHandler
import com.hipoom.cli.todo.handler.mapping.MappingHandler
import com.hipoom.cli.todo.handler.path.PathHandler
import com.hipoom.cli.todo.handler.time.TimeHandler
import com.hipoom.cli.todo.handler.owner.OwnerHandler
import com.hipoom.cli.todo.handler.pin.PinHandler
import com.hipoom.cli.todo.handler.screen.ScreenHandler
import com.hipoom.cli.todo.handler.window.WindowHandler
import com.hipoom.cli.todo.handler.show.ShowHandler
import com.hipoom.cli.todo.handler.sort.SortHandler
import com.hipoom.cli.todo.handler.style.StyleHandler
import com.hipoom.cli.todo.handler.style.StyleInitializer
import com.hipoom.cli.todo.handler.textmapping.TextMappingHandler
import com.hipoom.cli.todo.handler.view.ViewHandler
import com.hipoom.cli.todo.handler.template.TemplateHandler
import com.hipoom.cli.todo.handler.upgrade.UpgradeHandler
import com.hipoom.cli.workspace.Workspace
import com.hipoom.cli.workspace.WorkspaceContext


lateinit var app : TodoApp

/**
 * @author ZhengHaiPeng
 * @since 2025/2/1 23:53
 *
 */
class TodoApp: CliApp() {

    /* ======================================================= */
    /* Constructors or Instance Creator                        */
    /* ======================================================= */

    init {
        app = this
    }



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun getSupportHandlers(): List<AbsHandler> {
        val handlers = mutableListOf(
            TodoShellHandler(),
            HelpHandler(),
            WorkspaceHandler(),

            AddHandler(),
            ShowHandler(),
            MarkHandler(),
            EditHandler(),
            DeleteHandler(),
            MoveHandler(),
            DetailHandler(),
            CommentHandler(),
            GroupHandler(),
            FocusHandler(),
            TemplateHandler(),
            WindowHandler(),
            ConfigHandler(),
            CollapseHandler(),
            ExpandHandler(),
            LabelHandler(),
            MappingHandler(),
            PathHandler(),
            TimeHandler(),
            OwnerHandler(),
            PinHandler(),
            ScreenHandler(),
            ViewHandler(),
            StyleHandler(),
            SortHandler(),
            TextMappingHandler(),
            DevelopHandler(),
            FindHandler(),
            UpgradeHandler(),
            CmdPrefixHandler(),

            PluginHandler()
        )

        // 通知插件
        notifyOnGetSupportHandlers(handlers)

        return handlers
    }

    override fun onStart() {
        super.onStart()

        // 从磁盘读取最近一次打开的 workspace
        var workspace = persistentData.queryLastestWorkspaceContext()
        if (workspace == null) {
            workspace = Workspace.relative.createAndSaveWorkspace(getAppName(), "default")
        }

        // 更新到进程中
        processData.updateCurrentWorkspaceContext(workspace)

        // 更新这个 workspace 的配置
        updateConfigOnWorkspaceChanged(workspace)

        // 如果需要在启动时就展示事项，则此时展示
        val need = Configs.launch.needShowOnLaunch
        if (need) {
            ShowHandler().onHandle("show", this, workspace)
        }

        // 开始定时任务
        observeTimeTick()
    }

    override fun getCurrentWorkspace(): WorkspaceContext {
        return processData.getCurrentWorkspaceContext()
    }

    override fun getAppName(): String {
        return "todo"
    }

    /**
     * 这个方法，会在 WorkspaceHandler 切换 workspace 后执行，
     */
    override fun onWorkspaceChanged(newContext: WorkspaceContext) {
        // 通知插件
        notifyOnWorkspaceChanged(newContext)

        // 更新到进程中
        processData.updateCurrentWorkspaceContext(newContext)

        // 更新到磁盘中
        persistentData.updateCurrentWorkspaceContext(newContext)

        // 重新加载这个 Workspace 的配置
        updateConfigOnWorkspaceChanged(newContext)
    }

    override fun onWillHandleCmd(oCmd: String): String {
        val cmd = oCmd.trim()
        if (cmd.isEmpty()) {
            return ""
        }

        val prefix = cmd.split(" ").first()
        val found = cmdMappings.mappings.find { it.quick == prefix }
        if (found == null) {
            return cmd
        }

        val foundOrigin = found.origin

        // 占位符替换，子类先处理
        val tempCmd = onWillReplacePlaceHolder(foundOrigin)

        // 去掉前缀的剩余指令
        val withoutPrefix = cmd.removePrefix(prefix).trim()

        // 替换占位符
        val newCmd = replacePlaceholders(
            cmd = tempCmd,
            params = withoutPrefix
        )

        return newCmd
    }

    override fun onWillReplacePlaceHolder(oCmd: String): String {
        // 只处理 第一个子指令
        val cmd = oCmd.split(" && ").first()

        var result = cmd
        
        // 替换 ${date} 占位符为当前年月日
        if (result.contains("\${date}")) {
            val currentDate = java.time.LocalDate.now()
            val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy年MM月dd日")
            val dateString = currentDate.format(formatter)
            result = result.replace("\${date}", dateString)
        }

        // 替换 ${?} 占位符为上一个操作过的事项的 id
        if (result.contains("\${?}")) {
            result = result.replace("\${?}", last_modify_item_id.toString())
        }

        val temp = ArrayList(oCmd.split(" && "))
        temp[0] = result
        result = temp.joinToString(separator = " && ") { it }
        return result
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private fun observeTimeTick() {
//       ForEachMinute.addCallback {
//           // 找到所有 workspace
//           val workspaces = Workspace.listAllWorkspace(app.getAppName())
//
//           // 找到每个 workspace 的所有事件
//           val items = ArrayList<Item>()
//           workspaces.forEach { workspace ->
//               items.addAll(workspace.itemDao().loadAllItems())
//           }
//
//           // 找到有时间戳的事项
//           items.filter {
//               it.deadline != null && it.status != Item.STATUS_DELETED && it.status != Item.STATUS_DONE
//           }.forEach { item ->
//               // 检查是否到时间了
//               val now = System.currentTimeMillis()
//               if (item.deadline!! < now && true != item.isNotified) {
//                   Notification
//               }
//           }
//       }
    }
}
