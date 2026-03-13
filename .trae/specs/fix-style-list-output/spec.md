# 修复 style --list 无输出问题 Spec

## Why

用户执行 `style --list` 命令时没有任何输出，因为虽然 `--list` 选项在 `options.kt` 中定义了，但 `StyleHandler.onHandle()` 方法没有处理该选项。

## What Changes

- 在 `StyleHandler.onHandle()` 方法中添加对 `--list` 选项的处理
- 实现 `list.kt` 中的 `listStyles()` 函数，用于列出所有可用的配色方案

## Impact

- Affected code:
  - `src/main/java/com/hipoom/cli/todo/handler/style/StyleHandler.kt` (修改，添加 --list 处理逻辑)
  - `src/main/java/com/hipoom/cli/todo/handler/style/actions/list.kt` (修改，实现 listStyles 函数)

## ADDED Requirements

### Requirement: 列出配色方案功能

系统应正确处理 `style --list` 命令，显示所有可用的配色方案。

#### Scenario: 列出所有配色方案

- **WHEN** 用户执行 `style --list` 或 `style -l` 命令
- **THEN** 系统显示所有可用的配色方案列表
- **AND** 每个配色方案显示名称和演示效果
- **AND** 标记当前使用的配色方案
