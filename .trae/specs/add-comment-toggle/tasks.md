# CLI Todo - 评论显示开关功能 - 实现计划

## [x] Task 1: 在 Configs 类中添加评论显示配置项
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 在 Configs.Show 对象中添加 needShowComment 配置项
  - 设置默认值为 true，保持与当前行为一致
- **Acceptance Criteria Addressed**: [AC-1, AC-2, AC-3]
- **Test Requirements**:
  - `programmatic` TR-1.1: 配置项能够正确保存和读取
  - `programmatic` TR-1.2: 默认值为 true
- **Notes**: 参考现有的配置项实现方式

## [x] Task 2: 在 show 命令选项中添加评论显示开关选项
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 在 showOptions 中添加 --enable-comment 和 --disable-comment 选项
  - 添加相应的帮助信息
- **Acceptance Criteria Addressed**: [AC-1, AC-2, AC-4]
- **Test Requirements**:
  - `programmatic` TR-2.1: 命令行选项能够被正确解析
  - `human-judgment` TR-2.2: 帮助信息中包含评论显示开关的说明
- **Notes**: 参考现有的选项实现方式

## [x] Task 3: 在 WorkspaceContext 中添加评论显示开关的扩展函数
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 添加 enableShowComment() 扩展函数
  - 添加 disableShowComment() 扩展函数
- **Acceptance Criteria Addressed**: [AC-1, AC-2]
- **Test Requirements**:
  - `programmatic` TR-3.1: 函数能够正确设置配置值
- **Notes**: 参考现有的扩展函数实现方式

## [x] Task 4: 在 ShowHandler 中添加评论显示开关的处理逻辑
- **Priority**: P0
- **Depends On**: Task 2, Task 3
- **Description**: 
  - 在 onHandle 方法中添加处理 --enable-comment 和 --disable-comment 选项的逻辑
- **Acceptance Criteria Addressed**: [AC-1, AC-2]
- **Test Requirements**:
  - `programmatic` TR-4.1: 命令能够正确设置评论显示状态
- **Notes**: 参考现有的选项处理逻辑

## [x] Task 5: 修改评论显示逻辑，根据开关状态控制显示
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 找到评论显示的相关代码
  - 修改逻辑，根据 needShowComment 配置项控制是否显示评论
- **Acceptance Criteria Addressed**: [AC-1, AC-2]
- **Test Requirements**:
  - `programmatic` TR-5.1: 当开关启用时，评论正常显示
  - `programmatic` TR-5.2: 当开关禁用时，评论不显示
- **Notes**: 需要找到评论显示的具体实现位置