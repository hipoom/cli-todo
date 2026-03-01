# Tasks

- [x] Task 1: 创建 style 目录和 options.kt 文件
  - [x] SubTask 1.1: 创建 `src/main/java/com/hipoom/cli/todo/handler/style/` 目录
  - [x] SubTask 1.2: 创建 `options.kt`，定义 `-h` (help) 和 `-s` (set) 选项

- [x] Task 2: 创建 StyleHandler.kt
  - [x] SubTask 2.1: 创建 `StyleHandler` 类，继承 `ApacheCliOptionHandler`
  - [x] SubTask 2.2: 实现 `description()` 方法
  - [x] SubTask 2.3: 实现 `onHandle()` 方法，处理 `-h` 和 `-s` 选项
  - [x] SubTask 2.4: 实现预设配色方案的数据结构
  - [x] SubTask 2.5: 实现配色方案切换逻辑

- [x] Task 3: 在 TodoApp.kt 中注册 StyleHandler
  - [x] SubTask 3.1: 在 `getSupportHandlers()` 方法中添加 `StyleHandler()`

# Task Dependencies
- Task 2 依赖 Task 1
- Task 3 依赖 Task 2
