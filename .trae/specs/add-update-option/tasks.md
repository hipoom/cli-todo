# Tasks

- [x] Task 1: 在 options.kt 中添加 -update 参数选项
  - [x] SubTask 1.1: 添加 `-update` Option，长参数名为 `--update`，描述为 "Auto update and restart application"

- [x] Task 2: 在 UpgradeHandler.kt 中实现自动更新逻辑
  - [x] SubTask 2.1: 在 onHandle 方法中添加对 `-update` 参数的处理分支
  - [x] SubTask 2.2: 实现 `autoUpdate` 方法：检查版本、下载新版本、替换 jar、重启应用
  - [x] SubTask 2.3: 实现 `getCurrentJarPath` 方法：获取当前运行的 jar 文件路径
  - [x] SubTask 2.4: 实现 `replaceJarAndRestart` 方法：替换 jar 文件并重启应用

# Task Dependencies
- Task 2 依赖 Task 1
