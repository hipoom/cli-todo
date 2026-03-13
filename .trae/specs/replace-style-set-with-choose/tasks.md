# Tasks

- [x] Task 1: 更新命令行选项定义
  - [x] SubTask 1.1: 在 options.kt 中移除 `--set` 选项定义
  - [x] SubTask 1.2: 在 options.kt 中添加 `--choose` 选项定义

- [x] Task 2: 实现交互式选择器核心功能
  - [x] SubTask 2.1: 将 set.kt 重命名为 choose.kt
  - [x] SubTask 2.2: 创建交互式选择器函数 `chooseStyle`
  - [x] SubTask 2.3: 实现键盘监听逻辑（上下箭头、回车、ESC、'q'）
  - [x] SubTask 2.4: 实现列表渲染和高亮显示逻辑
  - [x] SubTask 2.5: 实现循环导航逻辑（顶部向上到底部，底部向下到顶部）
  - [x] SubTask 2.6: 实现确认选择和保存配置逻辑
  - [x] SubTask 2.7: 实现取消操作逻辑

- [x] Task 3: 更新 StyleHandler 处理逻辑
  - [x] SubTask 3.1: 移除 `--set` 选项的处理分支
  - [x] SubTask 3.2: 添加 `--choose` 选项的处理分支
  - [x] SubTask 3.3: 更新导入语句

- [x] Task 4: 验证功能
  - [x] SubTask 4.1: 测试 `style --choose` 命令正常工作
  - [x] SubTask 4.2: 测试上下方向键导航功能
  - [x] SubTask 4.3: 测试循环导航功能
  - [x] SubTask 4.4: 测试回车键确认选择
  - [x] SubTask 4.5: 测试 ESC 和 'q' 键取消操作
  - [x] SubTask 4.6: 确认 `style --set` 已被移除

# Task Dependencies
- Task 2 依赖 Task 1 (需要先定义选项)
- Task 3 依赖 Task 2 (需要先实现选择功能)
- Task 4 依赖 Task 1, Task 2, Task 3 (需要所有代码修改完成)
