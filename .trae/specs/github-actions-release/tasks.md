# Tasks

- [x] Task 1: 创建 GitHub Actions 工作流目录结构
  - [x] SubTask 1.1: 创建 `.github/workflows` 目录
  - [x] SubTask 1.2: 创建 `release.yml` 工作流文件

- [x] Task 2: 配置工作流触发条件
  - [x] SubTask 2.1: 配置在推送 `v*` 标签时触发
  - [x] SubTask 2.2: 配置必要的权限

- [x] Task 3: 配置构建环境
  - [x] SubTask 3.1: 配置 Java 环境（JDK 11 或更高版本）
  - [x] SubTask 3.2: 配置 Gradle 缓存以加速构建

- [x] Task 4: 配置构建步骤
  - [x] SubTask 4.1: 执行 Gradle 构建任务
  - [x] SubTask 4.2: 验证 JAR 文件生成

- [x] Task 5: 配置 Release 发布
  - [x] SubTask 5.1: 提取版本号信息
  - [x] SubTask 5.2: 创建 GitHub Release
  - [x] SubTask 5.3: 上传 JAR 文件到 Release

# Task Dependencies
- [Task 2] depends on [Task 1]
- [Task 3] depends on [Task 1]
- [Task 4] depends on [Task 3]
- [Task 5] depends on [Task 4]
