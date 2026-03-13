# Checklist

- [x] `--set` 选项已从 options.kt 中移除
- [x] `--choose` 选项已添加到 options.kt 中
- [x] set.kt 文件已重命名为 choose.kt
- [x] 已实现交互式选择器函数 `chooseStyle`
- [x] 支持上下方向键导航
- [x] 支持循环导航（顶部向上到底部，底部向下到顶部）
- [x] 当前选中的方案有高亮或特殊样式显示
- [x] 当前使用的配色方案有 `✓` 标识
- [x] 回车键可以确认选择并保存配置
- [x] ESC 键可以取消操作
- [x] 'q' 键可以取消操作
- [x] StyleHandler 中已移除 `--set` 处理分支
- [x] StyleHandler 中已添加 `--choose` 处理分支
- [x] `style --choose` 命令可以正常选择配色方案
- [x] `style --set` 命令不再可用
- [x] 无可用方案时有友好提示
