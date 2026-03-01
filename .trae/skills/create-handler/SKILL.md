---
name: create-handler
description: 创建新 Handler 的指南
---

# 创建新 Handler 的指南

本文档描述了如何在 todo 项目中创建一个新的 Handler，用于处理特定的命令。

## 1. 基本概念

Handler 是处理用户命令的核心组件，每个 Handler 负责处理一种或多种相关的命令。在 todo 项目中，所有 Handler 都继承自 `ApacheCliOptionHandler` 类，该类提供了命令行参数解析和处理的基本功能。

## 2. 创建步骤

### 2.1 创建目录结构

首先，为新的 Handler 创建一个目录。通常，每个 Handler 都有自己的目录，位于 `src/main/java/com/hipoom/cli/todo/handler/` 下。

例如，创建一个名为 `example` 的 Handler：

```
src/main/java/com/hipoom/cli/todo/handler/example/
```

### 2.2 创建 Handler 类

在新创建的目录中，创建一个 Handler 类，继承自 `ApacheCliOptionHandler`。

```kotlin
package com.hipoom.cli.todo.handler.example

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine

class ExampleHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options = exampleOptions

    override val supportPrefixes: List<String> = listOf("example")


    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Example Handler"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        // 处理 help 选项
        if (commandLine.hasOption("h")) {
            printHelp()
            return true
        }

        // 处理其他选项和命令逻辑
        // ...

        printLine("Example handler executed successfully!")
        return true
    }


    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    // 可以添加私有方法来处理具体的逻辑
    // ...
}
```

### 2.3 创建选项配置文件

在同一个目录中，创建一个 `options.kt` 文件，定义该 Handler 支持的命令行选项。

```kotlin
package com.hipoom.cli.todo.handler.example

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

val exampleOptions: Options = Options()
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .hasArg(false)
            .desc("Help")
            .build()
    )
    .addOption(
        Option.builder("o")
            .longOpt("option")
            .hasArg(true)
            .desc("Example option")
            .build()
    )
    // 可以添加更多选项
    // ...
```

### 2.4 注册 Handler

在 `TodoApp.kt` 文件的 `getSupportHandlers` 方法中注册新的 Handler。

```kotlin
override fun getSupportHandlers(): List<AbsHandler> {
    val handlers = mutableListOf(
        // 现有的 Handler
        // ...
        
        // 添加新的 Handler
        ExampleHandler(),
        
        // 其他 Handler
        // ...
    )

    // 通知插件
    notifyOnGetSupportHandlers(handlers)

    return handlers
}
```

## 3. 实现细节

### 3.1 命令行选项

使用 Apache Commons CLI 库来定义和解析命令行选项。每个选项可以有短选项（如 `-h`）和长选项（如 `--help`）。

### 3.2 处理逻辑

在 `onHandle` 方法中实现具体的处理逻辑：

1. 首先处理 `help` 选项
2. 然后处理其他选项和命令参数
3. 执行具体的业务逻辑
4. 返回 `true` 表示处理成功

### 3.3 访问数据

可以通过 `workspace` 参数访问工作区相关的数据：

- `workspace.itemDao()`: 获取待办事项的数据访问对象
- `workspace.virtualViews()`: 获取虚拟视图相关功能
- 其他工作区相关方法

### 3.4 输出信息

使用 `printLine` 函数输出信息到控制台：

```kotlin
printLine("Message to display")
```

## 4. 最佳实践

1. **命名规范**：Handler 类名应以 `Handler` 结尾，如 `ExampleHandler`

2. **目录结构**：每个 Handler 应该有自己的目录，包含 Handler 类和选项配置文件

3. **代码组织**：
   - 使用 `/* ======================================================= */` 注释分隔不同的代码部分
   - 清晰地组织字段、重写方法和私有方法

4. **错误处理**：
   - 处理可能的异常情况
   - 提供清晰的错误信息

5. **文档**：
   - 为 Handler 类添加注释，说明其功能
   - 为命令行选项添加清晰的描述

## 5. 示例

### 5.1 完整的 ExampleHandler

```kotlin
package com.hipoom.cli.todo.handler.example

import com.hipoom.cli.scaffold.CliApp
import com.hipoom.cli.scaffold.handler.ApacheCliOptionHandler
import com.hipoom.cli.todo.printLine
import com.hipoom.cli.workspace.WorkspaceContext
import org.apache.commons.cli.CommandLine

class ExampleHandler : ApacheCliOptionHandler() {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    override val options = exampleOptions

    override val supportPrefixes: List<String> = listOf("example")


    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun description() = "Example Handler"

    override fun onHandle(
        originParams: String,
        commandLine: CommandLine,
        app: CliApp,
        workspace: WorkspaceContext
    ): Boolean {
        // 处理 help 选项
        if (commandLine.hasOption("h")) {
            printHelp()
            return true
        }

        // 处理自定义选项
        if (commandLine.hasOption("o")) {
            val optionValue = commandLine.getOptionValue("o")
            printLine("Option value: $optionValue")
        }

        // 处理命令参数
        val args = commandLine.args
        if (args.isNotEmpty()) {
            printLine("Arguments: ${args.joinToString(", ")}")
        }

        printLine("Example handler executed successfully!")
        return true
    }
}
```

### 5.2 完整的 options.kt

```kotlin
package com.hipoom.cli.todo.handler.example

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

val exampleOptions: Options = Options()
    .addOption(
        Option.builder("h")
            .longOpt("help")
            .hasArg(false)
            .desc("Help")
            .build()
    )
    .addOption(
        Option.builder("o")
            .longOpt("option")
            .hasArg(true)
            .desc("Example option with value")
            .build()
    )
    .addOption(
        Option.builder("f")
            .longOpt("flag")
            .hasArg(false)
            .desc("Example flag option")
            .build()
    )
```

## 6. 测试

创建新的 Handler 后，应该测试其功能：

1. 构建项目
2. 运行 todo 命令，测试新的 Handler
3. 测试各种选项和参数

例如：

```bash
# 显示帮助
./todo example -h

# 使用自定义选项
./todo example -o value

# 使用标志选项
./todo example -f

# 带参数
./todo example arg1 arg2
```

## 7. 总结

创建新的 Handler 是扩展 todo 项目功能的重要方式。通过遵循上述步骤和最佳实践，可以创建功能完整、代码清晰的 Handler，为用户提供更多有用的命令和功能。