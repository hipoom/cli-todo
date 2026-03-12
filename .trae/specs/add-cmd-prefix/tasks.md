# Tasks

- [x] Task 1: 创建 CmdPrefixHandler 及相关文件
  - [x] SubTask 1.1: 创建 `handler/cmdprefix/CmdPrefixHandler.kt` 文件
  - [x] SubTask 1.2: 创建 `handler/cmdprefix/options.kt` 文件定义命令行选项
  - [x] SubTask 1.3: 在 TodoApp.kt 中注册 CmdPrefixHandler

- [x] Task 2: 创建全局变量存储前缀
  - [x] SubTask 2.1: 创建全局变量存储当前前缀设置（会话级别）

- [x] Task 3: 实现 TodoShellHandler 中的自动填充逻辑
  - [x] SubTask 3.1: 在 TodoShellHandler 中读取当前的前缀设置
  - [x] SubTask 3.2: 实现自动填充前缀的逻辑（检查输入是否已是完整命令）
  - [x] SubTask 3.3: 实现按下方向键时临时取消前缀的功能
  - [x] SubTask 3.4: 更新 shell 模式的提示信息，显示当前前缀

- [x] Task 4: 测试功能
  - [x] SubTask 4.1: 测试 cmd-prefix --set 功能
  - [x] SubTask 4.2: 测试 cmd-prefix --clear 功能
  - [x] SubTask 4.3: 测试 shell 模式下的自动填充功能
  - [x] SubTask 4.4: 测试按下方向键临时取消前缀功能

# Task Dependencies
- Task 3 依赖 Task 2（需要全局变量才能读取前缀）
- Task 4 依赖 Task 1, Task 2, Task 3（所有功能实现完成后进行测试）
