# Show Handler 模块说明

## 文件夹概述

Show Handler 模块负责处理 TODO 事项的展示功能，提供多种展示模式和配置选项，帮助用户以不同方式查看和管理待办事项。

## 文件结构

```
show/
├── ShowHandler.kt          # 核心处理类，处理 show 命令
├── ShowOneItemDetail.kt    # 单个事项详情展示
├── common.kt               # 通用工具函数
├── deleted.kt              # 已删除事项相关功能
├── done.kt                 # 已完成事项相关功能
├── focus.kt                # 聚焦模式相关功能
├── id.kt                   # ID 显示相关功能
├── label.kt                # 标签显示相关功能
├── onlyRoot.kt             # 仅显示根节点功能
├── options.kt              # 命令选项定义
├── owner.kt                # 所有者显示相关功能
└── status.kt               # 状态显示相关功能
```

## 核心功能

1. **树状展示**：以树状结构展示事项及其子事项
2. **聚焦模式**：只显示特定事项及其子事项
3. **分组展示**：按所有者分组展示事项
4. **过滤功能**：可过滤已完成、已删除的事项
5. **配置选项**：可配置是否显示 ID、状态、所有者、标签等
6. **详情展示**：展示单个事项的详细信息
7. **固定事项**：单独展示固定的事项

## 命令选项

| 选项 | 描述 |
|------|------|
| `--enable-done` | 启用显示已完成事项 |
| `--disable-done` | 禁用显示已完成事项 |
| `--enable-deleted` | 启用显示已删除事项 |
| `--disable-deleted` | 禁用显示已删除事项 |
| `--enable-show-on-launch` | 启用启动时显示事项 |
| `--disable-show-on-launch` | 禁用启动时显示事项 |
| `--enable-status` | 启用显示状态 |
| `--disable-status` | 禁用显示状态 |
| `--enable-owner` | 启用显示所有者 |
| `--disable-owner` | 禁用显示所有者 |
| `--enable-label` | 启用显示标签 |
| `--disable-label` | 禁用显示标签 |
| `--enable-id` | 启用显示 ID |
| `--disable-id` | 禁用显示 ID |
| `-h, --help` | 显示帮助信息 |

## 功能模块说明

### ShowHandler

核心处理类，负责处理 show 命令的各种选项和展示逻辑。

- 处理命令行选项，如启用/禁用各种显示选项
- 调用不同的展示模式（树状模式或分组模式）
- 处理虚拟视图的过滤

### ShowOneItemDetail

负责展示单个事项的详细信息，包括：
- ID
- 内容
- 所有者
- 状态
- 标签
- 截止日期
- 评论

### 通用工具函数 (common.kt)

提供各种通用功能：
- `showAutoMode`：自动选择展示模式
- `buildAsParentMode`：构建树状结构的行信息
- `showAsWhoMode`：按所有者分组展示
- `cleanChildrenWithStatus`：清理特定状态的子事项
- `getIdDes`：生成 ID 描述
- `getStatusIcon`：生成状态图标
- `getOwnerDes`：生成所有者描述
- `getLabels`：生成标签描述
- `getCollapseFlag`：生成折叠状态标志
- `showItemsAsTreeMode`：以树状模式展示事项
- `showPins`：展示固定的事项

### 过滤功能

- `tryFilterDeletedItems`：过滤已删除的事项
- `tryFilterDoneItems`：过滤已完成的事项
- `tryFilterForOnlyRoot`：只显示根节点

### 聚焦模式

- `tryShowByFocus`：处理聚焦模式，只显示特定事项及其子事项

### 显示配置

- `enableShowDeleted` / `disableShowDeleted`：控制是否显示已删除事项
- `enableShowDone` / `disableShowDone`：控制是否显示已完成事项
- `enableShowId` / `disableShowId`：控制是否显示 ID
- `enableShowLabel` / `disableShowLabel`：控制是否显示标签
- `enableShowOwner` / `disableShowOwner`：控制是否显示所有者
- `enableShowStatus` / `disableShowStatus`：控制是否显示状态

## 关键类和函数

### TreeModeRow

树状模式下的行信息类，包含：
- ID
- 状态
- 带缩进的内容
- 折叠状态
- 所有者
- 标签
- 截止日期
- 事项对象

### showItemsAsTreeMode

以树状模式展示事项，包括：
- 显示固定事项
- 构建树状结构
- 展示每行信息

### showAsWhoMode

按所有者分组展示事项，将事项按所有者分类并展示。

### tryShowByFocus

处理聚焦模式，只显示特定事项及其子事项，支持过滤和不同展示模式。

## 展示流程

1. 解析命令行选项
2. 处理启用/禁用各种显示选项
3. 尝试聚焦模式处理
4. 加载事项数据
5. 处理虚拟视图过滤
6. 过滤已删除事项
7. 过滤已完成事项
8. 处理仅显示根节点选项
9. 选择展示模式（树状或分组）
10. 展示事项
