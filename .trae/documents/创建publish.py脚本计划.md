# 创建 publish.py 脚本计划

## 任务目标

在项目根目录创建 `publish.py` 脚本，用于自动构建新版本。

## 当前状态分析

### 版本号位置

- 版本号定义在 `build.gradle`：`version = '0.0.26'`
- 版本号定义在 `src/main/java/com/hipoom/cli/todo/VERSION.kt`：`val VERSION = "1.0.0"`
- 格式为 `major.minor.patch`（三位版本号）

### VersionInfo 结构

根据 `src/main/java/com/hipoom/cli/todo/handler/upgrade/VersionInfo.kt` 定义，需要生成的字段：

- `version`: 版本号字符串，如 "0.0.27"
- `versionCode`: 版本码，整数
- `releaseDate`: 发布日期
- `downloadUrl`: 下载地址，格式为 `https://github.com/hipoom/cli-todo/releases/download/v${version}/todo.jar`
- `releaseNotes`: 发布说明

## 实现步骤

### 步骤 1：创建 publish.py 脚本

创建 `c:\Workspace\Github\cli-todo\publish.py`

### 步骤 2：实现版本号解析功能

- 读取 `build.gradle` 文件
- 使用正则表达式提取当前版本号（如 `0.0.26`）

### 步骤 3：实现版本号自增功能

- 解析版本号的三个部分
- 将第 3 位（patch）加 1
- 例如：`0.0.26` → `0.0.27`

### 步骤 4：获取用户输入

- 提示用户输入 `releaseNotes`（发布说明）

### 步骤 5：更新 build.gradle

- 将新版本号写回 `build.gradle` 的 `version = 'x.x.x'` 行

### 步骤 6：更新 VERSION.kt

- 将新版本号写回 `src/main/java/com/hipoom/cli/todo/VERSION.kt` 的 `val VERSION = "x.x.x"` 行

### 步骤 7：执行构建命令

- 运行 `./gradlew jar` 命令触发构建

### 步骤 8：生成 latest_version.json

- 根据新版本号生成 JSON 文件
- 写入到 `.documents/latest_version.json`
- 包含以下字段：
  - `version`: 新版本号字符串
  - `versionCode`: 从版本号计算的整数（如 `0.0.27` → 27）
  - `releaseDate`: 当前日期（YYYY-MM-DD 格式）
  - `downloadUrl`: `https://github.com/hipoom/cli-todo/releases/download/v${version}/todo.jar`
  - `releaseNotes`: 用户输入的发布说明

## 脚本逻辑流程

```
1. 读取 build.gradle
2. 提取当前版本号
3. 计算新版本号（patch + 1）
4. 提示用户输入 releaseNotes（发布说明）
5. 更新 build.gradle 中的版本号
6. 更新 VERSION.kt 中的版本号
7. 运行 ./gradlew jar 执行构建
8. 生成 latest_version.json
9. 输出完成信息
```

## 注意事项

- 不需要命令行参数
- 旧版本 JSON 内容直接覆盖
- 使用 Python 标准库（re, json, datetime, subprocess, pathlib）
