# Tasks

- [x] Task 1: 创建文本映射数据模型和持久化存储
  - [x] SubTask 1.1: 创建 `TextMappingPair` 数据类，存储原文本和替换文本
  - [x] SubTask 1.2: 创建 `TextMappingConfigs` 配置类，管理映射列表
  - [x] SubTask 1.3: 创建 `TextMappingStorage` 对象，提供加载和保存映射的方法
  - [x] SubTask 1.4: 在 `CliApp-ext.kt` 的 `PersistentData` 中添加 `loadTextMappings` 和 `updateTextMappings` 方法

- [x] Task 2: 创建 TextMappingHandler 处理器
  - [x] SubTask 2.1: 创建 `handler/textmapping` 目录
  - [x] SubTask 2.2: 创建 `options.kt`，定义 `--list`、`--add`、`--delete`、`--help` 选项
  - [x] SubTask 2.3: 创建 `TextMappingHandler.kt`，实现处理器主逻辑
  - [x] SubTask 2.4: 创建 `actions/list.kt`，实现列出所有映射的功能
  - [x] SubTask 2.5: 创建 `actions/add.kt`，实现新增映射的功能
  - [x] SubTask 2.6: 创建 `actions/delete.kt`，实现删除映射的功能

- [x] Task 3: 注册 TextMappingHandler 到 TodoApp
  - [x] SubTask 3.1: 在 `TodoApp.kt` 的 `getSupportHandlers()` 中添加 `TextMappingHandler()`

- [x] Task 4: 实现自动文本替换功能
  - [x] SubTask 4.1: 在 `TextMappingStorage` 中添加 `applyMappings(text: String)` 方法
  - [x] SubTask 4.2: 在 `AddHandler.kt` 中应用文本映射到事项内容
  - [x] SubTask 4.3: 在 `EditHandler.kt` 中应用文本映射到事项内容

# Task Dependencies
- Task 2 依赖 Task 1
- Task 3 依赖 Task 2
- Task 4 依赖 Task 1
