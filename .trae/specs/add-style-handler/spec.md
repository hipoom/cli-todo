# 配色方案选择 Handler Spec

## Why

当前应用的颜色配置分散在多个地方（如 `commentStyle.textColor`、`commentStyle.backgroundColor` 等），用户需要手动编辑配置文件才能修改颜色。提供一个配色方案选择 Handler 可以让用户更方便地切换预设的配色方案，提升用户体验。

## What Changes

- 创建新的 `StyleHandler` 处理配色方案选择
- 在 `options.kt` 中定义命令行选项
- 在默认配置中添加配色方案相关配置
- 在 `TodoApp.kt` 中注册新的 Handler

## Impact

- Affected code:
  - `src/main/java/com/hipoom/cli/todo/handler/style/StyleHandler.kt` (新建)
  - `src/main/java/com/hipoom/cli/todo/handler/style/options.kt` (新建)
  - `src/main/java/com/hipoom/cli/todo/handler/config/Configs.kt` (修改，添加配色方案配置)
  - `src/main/java/com/hipoom/cli/todo/TodoApp.kt` (修改，注册新 Handler)

## ADDED Requirements

### Requirement: 配色方案选择功能

系统应提供 `style` 命令，允许用户选择预设的配色方案。

#### Scenario: 查看可用配色方案

- **WHEN** 用户执行 `style` 命令
- **THEN** 系统显示所有可用的配色方案列表
- **AND** 显示当前使用的配色方案

#### Scenario: 选择配色方案

- **WHEN** 用户执行 `style -s <方案名>` 命令
- **AND** 方案名有效
- **THEN** 系统切换到指定的配色方案
- **AND** 保存配置到工作空间

#### Scenario: 查看帮助

- **WHEN** 用户执行 `style -h` 命令
- **THEN** 系统显示帮助信息

#### Scenario: 方案名无效

- **WHEN** 用户执行 `style -s <方案名>` 命令
- **AND** 方案名无效
- **THEN** 系统提示错误信息
- **AND** 显示可用的配色方案列表

## 配色方案包含的颜色配置

配色方案将控制以下颜色设置：

| 配置项 | 说明 | 当前默认值 |
|--------|------|------------|
| `commentStyle.textColor` | 备注文字的颜色 | `128,128,128` (灰色) |
| `commentStyle.backgroundColor` | 备注文字的背景颜色 | `None` (默认) |
| `pinBackgroundColor` | 置顶事项的背景颜色 | `200,200,200` (浅灰) |

## 预设配色方案

### 1. default (默认)
- 适合普通终端使用
- 备注文字颜色: 灰色 (128, 128, 128)
- 备注背景颜色: 无
- 置顶背景颜色: 浅灰 (200, 200, 200)

### 2. dark (暗色模式)
- 适合深色背景终端
- 备注文字颜色: 浅灰色 (180, 180, 180)
- 备注背景颜色: 无
- 置顶背景颜色: 深灰 (80, 80, 80)

### 3. light (亮色模式)
- 适合浅色背景终端
- 备注文字颜色: 深灰色 (80, 80, 80)
- 备注背景颜色: 无
- 置顶背景颜色: 浅灰 (220, 220, 220)

### 4. colorful (彩色模式)
- 适合支持丰富颜色的终端
- 备注文字颜色: 青色 (0, 180, 180)
- 备注背景颜色: 深蓝 (30, 30, 60)
- 置顶背景颜色: 橙色 (255, 200, 100)
