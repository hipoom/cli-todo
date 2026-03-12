# cmd-prefix 指令功能 Spec

## Why
用户在 shell 模式下经常需要输入相同的指令前缀（如 "add"、"show" 等），这会导致重复输入，降低效率。通过提供 cmd-prefix 功能，用户可以设置一个默认前缀，系统会自动在用户输入时填充该前缀，从而提高输入效率。

## What Changes
- 新增 `CmdPrefixHandler` 处理 `cmd-prefix` 指令
- 支持 `cmd-prefix --set <prefix>` 设置默认前缀
- 支持 `cmd-prefix --clear` 清除已设置的前缀
- 支持 `cmd-prefix` 显示当前设置的前缀
- 在 TodoShellHandler 中实现自动填充前缀功能
- 支持按下方向键时临时取消本次输入的前缀自动填充
- 前缀设置仅在当前会话中有效，不进行持久化

## Impact
- Affected specs: shell 模式交互
- Affected code: 
  - `TodoShellHandler.kt` - 修改 shell 模式的输入逻辑
  - 新增 `handler/cmdprefix/` 目录及相关文件

## ADDED Requirements

### Requirement: cmd-prefix 指令处理
系统应提供 `cmd-prefix` 指令，允许用户管理默认命令前缀。

#### Scenario: 设置命令前缀
- **WHEN** 用户执行 `cmd-prefix --set add`
- **THEN** 系统应保存前缀 "add" 到内存中，并提示用户设置成功

#### Scenario: 清除命令前缀
- **WHEN** 用户执行 `cmd-prefix --clear`
- **THEN** 系统应清除已设置的前缀，并提示用户清除成功

#### Scenario: 显示当前前缀
- **WHEN** 用户执行 `cmd-prefix`
- **THEN** 系统应显示当前设置的前缀，如果没有设置则提示未设置

### Requirement: Shell 模式自动填充前缀
当用户设置了默认命令前缀后，系统应在 shell 模式下自动填充该前缀。

#### Scenario: 自动填充前缀
- **GIVEN** 用户已设置前缀为 "add"
- **WHEN** 用户在 shell 模式下输入 "测试任务"
- **THEN** 系统应自动将输入转换为 "add 测试任务"

#### Scenario: 无前缀时的行为
- **GIVEN** 用户未设置前缀或已清除前缀
- **WHEN** 用户在 shell 模式下输入任意内容
- **THEN** 系统应按原样处理用户输入，不进行任何转换

#### Scenario: 用户输入已包含前缀
- **GIVEN** 用户已设置前缀为 "add"
- **WHEN** 用户在 shell 模式下输入 "show"
- **THEN** 系统应识别 "show" 是一个完整的命令，不自动添加前缀

#### Scenario: 按下方向键临时取消前缀
- **GIVEN** 用户已设置前缀为 "add"
- **WHEN** 用户在 shell 模式下按下方向键后输入内容
- **THEN** 系统应临时取消本次输入的前缀自动填充
- **AND** 后续输入仍正常应用前缀自动填充

### Requirement: 前缀设置会话级别
前缀设置仅在当前应用会话中有效，不进行持久化存储。

#### Scenario: 会话级别前缀
- **WHEN** 用户设置命令前缀
- **THEN** 前缀仅在当前会话中有效
- **AND** 应用重启后前缀设置失效
