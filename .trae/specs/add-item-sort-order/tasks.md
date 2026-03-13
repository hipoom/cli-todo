# Tasks

- [x] Task 1: 修改 Item 实体，添加 order 字段
  - [x] SubTask 1.1: 在 Item.kt 中添加 `@SerializedName("order") var order: Int? = null` 字段
  - [x] SubTask 1.2: 更新 `copy` 扩展函数，包含 order 字段参数
  - [x] SubTask 1.3: 添加数据迁移逻辑（在 ItemDao.load 中），为旧数据设置默认 order 值（通过排序逻辑兼容）

- [x] Task 2: 修改 ItemDao，添加排序相关方法
  - [x] SubTask 2.1: 添加 `sortMoveUp(id: Int)` 方法 - 与上一个兄弟交换 order
  - [x] SubTask 2.2: 添加 `sortMoveDown(id: Int)` 方法 - 与下一个兄弟交换 order
  - [x] SubTask 2.3: 添加 `sortMoveToTop(id: Int)` 方法 - 移动到最前面
  - [x] SubTask 2.4: 添加 `sortMoveToBottom(id: Int)` 方法 - 移动到最后面

- [x] Task 3: 修改展示逻辑，按 order 排序
  - [x] SubTask 3.1: 在 `buildParentChildRelationships` 方法中，添加子节点按 order 排序的逻辑
  - [x] SubTask 3.2: 确保兼容旧数据（order 为 null 时按 id 排序）

- [x] Task 4: 创建 options.kt 文件
  - [x] SubTask 4.1: 创建 `handler/sort/options.kt` 文件
  - [x] SubTask 4.2: 定义 `-i/--id` 选项（指定事项 ID）
  - [x] SubTask 4.3: 定义 `-u/--up` 选项（上移一位）
  - [x] SubTask 4.4: 定义 `-d/--down` 选项（下移一位）
  - [x] SubTask 4.5: 定义 `-t/--top` 选项（置顶）
  - [x] SubTask 4.6: 定义 `-b/--bottom` 选项（置底）
  - [x] SubTask 4.7: 定义 `-h/--help` 选项（帮助）

- [x] Task 5: 创建 SortHandler.kt 文件
  - [x] SubTask 5.1: 创建 `handler/sort/SortHandler.kt` 文件，继承 ApacheCliOptionHandler
  - [x] SubTask 5.2: 实现 `description()` 方法，返回 "调整子事项的显示顺序"
  - [x] SubTask 5.3: 设置 `supportPrefixes` 为 `listOf("sort")`
  - [x] SubTask 5.4: 实现 `onHandle()` 方法，解析命令行参数并分发到对应处理方法
  - [x] SubTask 5.5: 实现 `moveUp()` 私有方法，调用 ItemDao.sortMoveUp
  - [x] SubTask 5.6: 实现 `moveDown()` 私有方法，调用 ItemDao.sortMoveDown
  - [x] SubTask 5.7: 实现 `moveToTop()` 私有方法，调用 ItemDao.sortMoveToTop
  - [x] SubTask 5.8: 实现 `moveToBottom()` 私有方法，调用 ItemDao.sortMoveToBottom

- [x] Task 6: 注册 SortHandler 到系统
  - [x] SubTask 6.1: 在 TodoApp.kt 或相关注册文件中注册 SortHandler

# Task Dependencies
- Task 2 依赖 Task 1
- Task 3 依赖 Task 1
- Task 5 依赖 Task 2 且 依赖 Task 4
- Task 6 依赖 Task 5
