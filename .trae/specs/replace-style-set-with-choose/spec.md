# 替换 style --set 为 style --choose 功能 Spec

## Why
当前 `style --set` 功能通过简单的序号输入方式选择配色方案，用户体验不够直观。新的 `style --choose` 功能将提供交互式的键盘导航选择体验，用户可以通过上下方向键浏览和选择配色方案，提供更现代化的用户界面。

## What Changes
- **BREAKING**: 移除 `style --set` 选项及其处理逻辑
- 新增 `style --choose` 选项，提供交互式配色方案选择功能
- 支持键盘导航（上下方向键）选择配色方案
- 支持回车键确认选择，ESC 键取消操作
- 更新帮助文档和命令行选项说明

## Impact
- Affected specs: style 相关功能
- Affected code:
  - `src/main/java/com/hipoom/cli/todo/handler/style/options.kt` (修改选项定义)
  - `src/main/java/com/hipoom/cli/todo/handler/style/StyleHandler.kt` (修改处理逻辑)
  - `src/main/java/com/hipoom/cli/todo/handler/style/actions/set.kt` (重命名为 choose.kt 并重构)

## ADDED Requirements

### Requirement: 交互式配色方案选择功能

系统应提供 `style --choose` 命令，允许用户通过交互式键盘导航选择配色方案。

#### Scenario: 执行交互式选择
- **WHEN** 用户执行 `style --choose` 命令
- **THEN** 系统显示所有可用的配色方案列表
- **AND** 每个方案显示名称和预览效果
- **AND** 当前选中的方案有高亮标识
- **AND** 当前使用的方案有 `✓` 标识
- **AND** 用户可以通过上下方向键移动选择光标
- **AND** 用户可以通过回车键确认选择
- **AND** 选择后系统自动应用并保存配置

#### Scenario: 键盘导航操作
- **WHEN** 用户在交互式选择界面中
- **AND** 用户按下上方向键
- **THEN** 选择光标向上移动一个位置
- **AND** 如果已在顶部，则循环到底部

- **WHEN** 用户在交互式选择界面中
- **AND** 用户按下下方向键
- **THEN** 选择光标向下移动一个位置
- **AND** 如果已在底部，则循环到顶部

#### Scenario: 确认选择
- **WHEN** 用户在交互式选择界面中
- **AND** 用户按下回车键
- **THEN** 系统应用当前选中的配色方案
- **AND** 保存配置到工作空间
- **AND** 显示成功提示信息

#### Scenario: 取消选择
- **WHEN** 用户在交互式选择界面中
- **AND** 用户按下 ESC 键或 'q' 键
- **THEN** 系统取消操作并返回
- **AND** 显示取消提示信息

#### Scenario: 无可用方案
- **WHEN** 用户执行 `style --choose` 命令
- **AND** 没有任何可用的配色方案
- **THEN** 系统提示"暂无可用的配色方案"

## REMOVED Requirements

### Requirement: style --set 选项

**Reason**: 该功能将被 `style --choose` 替代，提供更好的用户体验和现代化的交互方式。

**Migration**: 用户应使用 `style --choose` 命令替代原有的 `style --set` 命令。功能保持一致，但交互体验更友好。

## 功能对比

| 特性 | style --set (旧) | style --choose (新) |
|------|------------------|---------------------|
| 显示方案列表 | ✓ | ✓ |
| 方案预览效果 | ✓ | ✓ |
| 当前方案标识 | ✗ | ✓ |
| 交互方式 | 输入序号 | 键盘导航 |
| 上下键选择 | ✗ | ✓ |
| 循环导航 | ✗ | ✓ |
| 取消操作支持 | ✗ | ✓ (ESC 或 'q') |
| 错误处理 | 基础 | 增强 |

## 实现细节

### 新的 --choose 功能特性
1. 使用 JLine 库实现键盘交互
2. 显示配色方案列表时，当前使用的方案使用 `✓` 标识
3. 当前选中的方案使用高亮或特殊样式显示
4. 支持上下方向键导航，支持循环（顶部向上到底部，底部向上到顶部）
5. 回车键确认选择，ESC 或 'q' 键取消操作
6. 实时更新显示，每次移动光标时重新渲染界面

### 技术实现要点
- 使用 `Terminal` 和 `LineReader` 处理键盘输入
- 使用 ANSI 转义码控制光标位置和屏幕清空
- 监听键盘事件：上箭头、下箭头、回车、ESC、'q'
- 每次选择变化时重新渲染整个列表以更新高亮状态

### 界面示例
```
配色方案选择 (使用 ↑↓ 选择，Enter 确认，ESC 取消):
  ✓ default
▶ dark          ← 当前选中
  light
  colorful
```
