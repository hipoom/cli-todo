# GitHub Actions 自动发布 Spec

## Why
当前项目需要手动构建和发布 Release，效率低下且容易出错。通过 GitHub Actions 可以在推送以 "v*" 开头的标签时自动构建 JAR 文件并创建 GitHub Release，实现自动化发布流程。

## What Changes
- 创建 `.github/workflows/release.yml` 工作流文件
- 配置在推送 `v*` 标签时触发
- 自动构建 JAR 文件
- 自动创建 GitHub Release 并上传构建产物

## Impact
- Affected specs: 无
- Affected code: 新增 `.github/workflows/release.yml` 文件

## ADDED Requirements

### Requirement: 自动发布工作流
系统 SHALL 在推送以 "v" 开头的标签时自动触发发布流程。

#### Scenario: 推送 v 开头的标签触发构建
- **WHEN** 用户推送一个以 "v" 开头的标签（如 v1.0.0）
- **THEN** 系统自动触发 GitHub Actions 工作流
- **AND** 工作流执行 Gradle 构建任务
- **AND** 工作流创建 GitHub Release
- **AND** 工作流上传 JAR 文件到 Release

#### Scenario: 构建产物正确命名
- **WHEN** 构建完成
- **THEN** JAR 文件命名为 `todo-{version}.jar`
- **AND** JAR 文件包含所有依赖（fat jar）

#### Scenario: Release 信息自动生成
- **WHEN** 创建 GitHub Release
- **THEN** Release 标题与标签名一致
- **AND** Release 包含构建的 JAR 文件

## MODIFIED Requirements
无

## REMOVED Requirements
无
