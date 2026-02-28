# Upgrade 指令添加 -update 参数 Spec

## Why

当前 upgrade 指令只支持检查更新和下载更新，用户需要手动替换 jar 文件。添加 `-update` 参数可以实现自动更新并重启应用，提升用户体验。

## What Changes

* 在 `options.kt` 中添加 `-update` 参数选项

* 在 `UpgradeHandler.kt` 中实现自动更新逻辑：下载新版本、替换当前 jar、重启应用

## Impact

* Affected code:

  * `src/main/java/com/hipoom/cli/todo/handler/upgrade/options.kt`

  * `src/main/java/com/hipoom/cli/todo/handler/upgrade/UpgradeHandler.kt`

## ADDED Requirements

### Requirement: 自动更新功能

系统应提供 `-update` 参数，实现一键自动更新功能。

#### Scenario: 成功自动更新

* **WHEN** 用户执行 `upgrade -update` 命令

* **AND** 存在新版本

* **THEN** 系统下载新版本 jar 文件

* **AND** 替换当前运行的 jar 文件

* **AND** 重启应用，新版本生效

#### Scenario: 已是最新版本

* **WHEN** 用户执行 `upgrade -update` 命令

* **AND** 当前已是最新版本

* **THEN** 系统提示用户已是最新版本，无需更新

#### Scenario: 更新失败

* **WHEN** 用户执行 `upgrade -update` 命令

* **AND** 下载或替换过程中发生错误

* **THEN** 系统提示错误信息，保留原版本不变

