# Text Mapping 功能规范

## Why
用户在添加或编辑事项时，经常需要输入一些特殊符号（如 →、←、✓ 等），但这些符号在键盘上难以直接输入。通过文本映射功能，用户可以定义简单的文本替换规则，例如输入 `->` 自动替换为 `→`，提高输入效率。

## What Changes
- 新增 `text_mapping` 指令，用于管理文本映射关系
- 新增 `TextMappingHandler` 处理器
- 新增文本映射持久化存储
- 在 AddHandler 和 EditHandler 中应用文本映射替换

## Impact
- Affected code: 
  - `handler/add/AddHandler.kt` - 添加时应用映射
  - `handler/edit/EditHandler.kt` - 编辑时应用映射
  - `TodoApp.kt` - 注册新 Handler

## ADDED Requirements

### Requirement: 文本映射管理指令
系统 SHALL 提供 `text_mapping` 指令，支持以下子命令：

#### Scenario: 查看所有映射
- **WHEN** 用户执行 `text_mapping --list` 或 `text_mapping -l`
- **THEN** 系统展示所有已定义的文本映射关系，格式为 `原文本 -> 替换文本`

#### Scenario: 新增映射
- **WHEN** 用户执行 `text_mapping --add "原文本" "替换文本"`
- **THEN** 系统新增一条映射关系并保存
- **AND** 如果原文本已存在，则更新为新的替换文本

#### Scenario: 删除映射
- **WHEN** 用户执行 `text_mapping --delete "原文本"` 或 `text_mapping -d "原文本"`
- **THEN** 系统删除指定的映射关系
- **AND** 如果原文本不存在，提示用户

#### Scenario: 查看帮助
- **WHEN** 用户执行 `text_mapping --help` 或 `text_mapping -h`
- **THEN** 系统打印帮助信息，展示所有可用选项

### Requirement: 自动文本替换
系统 SHALL 在用户添加或编辑事项内容时，自动应用文本映射进行替换。

#### Scenario: 添加事项时应用映射
- **WHEN** 用户执行 `add 事项内容` 且事项内容包含映射的原文本
- **THEN** 系统自动将原文本替换为映射的目标文本

#### Scenario: 编辑事项时应用映射
- **WHEN** 用户执行 `edit -i <id> -c "新内容"` 且新内容包含映射的原文本
- **THEN** 系统自动将原文本替换为映射的目标文本
