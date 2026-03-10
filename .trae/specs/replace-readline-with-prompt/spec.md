# 替换 readLine/readString 为 readLineWithPrompt Spec

## Why
项目中存在多种读取用户输入的方式（`readString`、`reader.readLine`），但项目已经提供了统一的 `readLineWithPrompt` 函数。统一使用 `readLineWithPrompt` 可以保持代码一致性，便于维护和未来扩展。

## What Changes
- 将所有使用 `com.hipoom.cli.scaffold.utils.readString` 的地方替换为 `readLineWithPrompt`
- 将所有使用 `reader.readLine(prompt)` 进行用户交互的地方替换为 `readLineWithPrompt`
- 移除不再需要的 `readString` 导入语句

## Impact
- Affected code:
  - `src/main/java/com/hipoom/cli/todo/handler/add/template.kt`
  - `src/main/java/com/hipoom/cli/todo/utils/ColorPicker.kt`
  - `src/main/java/com/hipoom/cli/todo/handler/style/actions/set.kt`
  - `src/main/java/com/hipoom/cli/todo/handler/TodoShellHandler.kt`
  - `src/main/java/com/hipoom/cli/todo/handler/template/TemplateHandler.kt`
  - `src/main/java/com/hipoom/cli/todo/handler/CommentHandler.kt`

## ADDED Requirements

### Requirement: 统一用户输入读取方式
所有用于用户交互提示的输入读取 SHALL 使用 `readLineWithPrompt` 函数。

#### Scenario: 替换 readString 调用
- **WHEN** 代码中使用 `readString("提示信息")` 读取用户输入
- **THEN** 应替换为 `readLineWithPrompt("提示信息")`

#### Scenario: 替换 reader.readLine 调用
- **WHEN** 代码中使用 `reader.readLine("提示信息")` 读取用户输入
- **THEN** 应替换为 `readLineWithPrompt("提示信息")`

#### Scenario: 保留不需要替换的情况
- **WHEN** `readLine` 用于非用户交互场景（如读取文件流、shell 命令循环）
- **THEN** 保持原有实现不变

## 需要替换的具体位置

| 文件 | 行号 | 原调用 | 替换为 |
|------|------|--------|--------|
| template.kt | 38 | `readString("请输入你要添加的事项内容")` | `readLineWithPrompt("请输入你要添加的事项内容")` |
| ColorPicker.kt | 118 | `readString("请输入您要选择的颜色代码")` | `readLineWithPrompt("请输入您要选择的颜色代码")` |
| set.kt | 30 | `reader.readLine("请输入要设置的方案序号:")` | `readLineWithPrompt("请输入要设置的方案序号:")` |
| TodoShellHandler.kt | 164 | `readString("请选择是否继续执行该指令[yes/no]")` | `readLineWithPrompt("请选择是否继续执行该指令[yes/no]")` |
| TemplateHandler.kt | 83 | `readString("请输入模板名称")` | `readLineWithPrompt("请输入模板名称")` |
| CommentHandler.kt | 76 | `readString("请输入要添加的备注")` | `readLineWithPrompt("请输入要添加的备注")` |

## 不需要替换的位置

| 文件 | 行号 | 原因 |
|------|------|------|
| TodoShellHandler.kt | 146 | shell 命令行输入循环，使用动态 prefix |
| UpgradeHandler.kt | 171 | 读取流数据，非用户交互 |
| ICommandLine.kt | 27, 52, 134, 135 | 接口定义和实现 |
| globals.kt | 34, 35 | `readLineWithPrompt` 函数定义 |
