# Todo 命令行待办事项软件使用手册

## 1. 软件介绍

Todo 是一个功能强大的命令行待办事项管理软件，专为开发者和命令行爱好者设计。它提供了丰富的功能，帮助您高效地管理和跟踪任务。

### 主要特性

- 📋 支持创建、编辑、删除任务
- 🏷️ 支持任务标签管理
- 👥 支持任务负责人分配
- ⏰ 支持任务截止时间设置
- 🔍 支持任务查找和过滤
- 📁 支持多个工作区管理
- 👁️ 支持虚拟视图
- 🎨 支持样式自定义
- ✨ 支持任务优先级和状态管理
- 📝 支持任务评论

## 2. 安装和设置

### 2.1 构建项目

1. 在 `build.gradle` 中修改 version 对应的版本号
2. 执行 `./gradlew jar` 命令构建项目
3. 构建产物会生成在 `build/libs/` 目录中

### 2.2 运行软件

```bash
java -jar todo.jar
```

## 3. 基本使用方法

### 3.1 查看任务列表

```bash
show
```

这将显示当前工作区中的所有任务，以树状结构展示。

### 3.2 添加新任务

```bash
add 任务内容
```

例如：

```bash
add 完成项目文档
```

### 3.3 标记任务状态

```bash
mark -i <任务ID> -n|--doing|-d|--del
```

- `-n`：标记为新任务
- `--doing`：标记为进行中
- `-d`：标记为已完成
- `--del`：标记为已删除

例如：

```bash
mark -i 1 --doing
```

### 3.4 编辑任务

```bash
edit -i <任务ID>
```

这将进入交互式编辑模式，您可以选择编辑任务的内容、负责人或截止时间。

也可以直接在命令行中指定要修改的内容：

```bash
edit -i <任务ID> -c "新内容" -o "负责人" -d "截止时间"
```

### 3.5 删除任务

```bash
delete -i <任务ID>
```

## 4. 高级功能

### 4.1 批量添加任务

```bash
add -b
```

这将进入批量添加模式，您可以一行一个任务地添加多个任务。输入 `exit` 退出批量模式。

在批量模式中，您可以使用 `child` 命令将后续输入的任务作为上一个任务的子任务，使用 `parent` 命令返回上一级。

### 4.2 高级模式添加任务

```bash
add -a
```

这将打开一个文本编辑器，您可以以 JSON 格式详细定义任务的所有属性。

### 4.3 任务分组

```bash
group -i <任务ID> -n "分组名称"
```

### 4.4 任务移动

```bash
move -i <任务ID> -p <父任务ID>
```

### 4.5 任务详情查看

```bash
detail -i <任务ID>
```

### 4.6 任务评论

```bash
comment -i <任务ID> -c "评论内容"
```

### 4.7 任务查找

```bash
find <关键词>
```

### 4.8 工作区管理

```bash
workspace list          # 列出所有工作区
workspace create <名称> # 创建新工作区
workspace use <名称>    # 切换到指定工作区
```

## 5. 虚拟视图

虚拟视图是一种特殊的任务集合，可以帮助您从不同角度查看和管理任务。

### 5.1 创建虚拟视图

```bash
view create <视图名称>
```

### 5.2 添加任务到视图

```bash
view add -i <任务ID> -v <视图名称>
```

### 5.3 切换到视图

```bash
view use <视图名称>
```

### 5.4 列出所有视图

```bash
view list
```

## 6. 标签管理

### 6.1 添加标签

```bash
label add -i <任务ID> -l <标签名称>
```

### 6.2 移除标签

```bash
label remove -i <任务ID> -l <标签名称>
```

### 6.3 自动标签规则

```bash
label rule add -c "包含文本" -l "标签名称"
```

当添加包含指定文本的任务时，会自动添加对应的标签。

## 7. 配置选项

### 7.1 显示选项

```bash
show --enable-done      # 显示已完成的任务
show --disable-done     # 隐藏已完成的任务
show --enable-deleted   # 显示已删除的任务
show --disable-deleted  # 隐藏已删除的任务
show --enable-status    # 显示任务状态
show --disable-status   # 隐藏任务状态
show --enable-id        # 显示任务ID
show --disable-id       # 隐藏任务ID
show --enable-owner     # 显示任务负责人
show --disable-owner    # 隐藏任务负责人
show --enable-label     # 显示任务标签
show --disable-label    # 隐藏任务标签
show --enable-comment   # 显示任务评论
show --disable-comment  # 隐藏任务评论
```

### 7.2 启动选项

```bash
show --enable-show-on-launch  # 启动时显示任务列表
show --disable-show-on-launch # 启动时不显示任务列表
```

## 8. 样式自定义

### 8.1 查看当前样式

```bash
style
```

### 8.2 自定义样式

```bash
style set <样式名称> <颜色值>
```

例如：

```bash
style set new #FF0000
```

## 9. 命令速查表

| 命令 | 描述 | 示例 |
|------|------|------|
| `add` | 添加任务 | `add 完成项目文档` |
| `show` | 显示任务列表 | `show` |
| `mark` | 标记任务状态 | `mark -i 1 -d` |
| `edit` | 编辑任务 | `edit -i 1` |
| `delete` | 删除任务 | `delete -i 1` |
| `move` | 移动任务 | `move -i 1 -p 2` |
| `detail` | 查看任务详情 | `detail -i 1` |
| `comment` | 评论任务 | `comment -i 1 -c "需要进一步讨论"` |
| `group` | 分组任务 | `group -i 1 -n "项目规划"` |
| `find` | 查找任务 | `find 文档` |
| `workspace` | 管理工作区 | `workspace create project` |
| `view` | 管理虚拟视图 | `view create important` |
| `label` | 管理标签 | `label add -i 1 -l urgent` |
| `style` | 自定义样式 | `style set new #FF0000` |

## 10. 最佳实践

### 10.1 任务组织

- 使用树状结构组织任务，将相关任务放在同一个父任务下
- 使用标签对任务进行分类
- 为重要任务设置截止时间
- 为任务分配负责人

### 10.2 工作区管理

- 为不同的项目创建不同的工作区
- 定期清理已完成的任务
- 使用虚拟视图快速访问特定类型的任务

### 10.3 效率提升

- 使用批量添加模式快速添加多个任务
- 设置自动标签规则，减少手动标签操作
- 使用快速模式简化任务添加流程
- 利用命令别名提高操作速度

## 11. 常见问题

### 11.1 如何恢复已删除的任务？

```bash
show --enable-deleted
mark -i <已删除任务ID> -n
```

### 11.2 如何设置任务的截止时间？

在添加任务时使用 `-d` 参数：

```bash
add -d "2026-04-01" 完成项目报告
```

或在编辑任务时设置：

```bash
edit -i <任务ID> -d "2026-04-01"
```

### 11.3 如何查看任务的完整历史？

```bash
detail -i <任务ID>
```

## 12. 总结

Todo 命令行待办事项软件是一个功能强大、灵活易用的任务管理工具，通过命令行界面提供了丰富的任务管理功能。它不仅支持基本的任务创建、编辑和删除，还提供了高级功能如批量操作、虚拟视图、标签管理等，帮助您更高效地管理和跟踪任务。

通过本手册的指导，您应该能够充分利用 Todo 软件的各种功能，提高工作效率，更好地组织和管理您的任务。

---

**版本信息：** 本手册基于 Todo 命令行待办事项软件的最新版本编写。

**更新日期：** 2026-03-11

**作者：** ZhengHaiPeng