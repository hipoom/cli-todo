# CLI Todo - 评论显示开关功能 - 验证清单

- [x] 检查 Configs 类中是否添加了 needShowComment 配置项
- [x] 检查配置项默认值是否为 true
- [x] 检查 show 命令选项中是否添加了 --enable-comment 和 --disable-comment 选项
- [x] 检查命令行帮助信息中是否包含评论显示开关的说明
- [x] 检查 WorkspaceContext 中是否添加了 enableShowComment() 和 disableShowComment() 扩展函数
- [x] 检查 ShowHandler 中是否添加了评论显示开关的处理逻辑
- [x] 检查评论显示逻辑是否根据开关状态控制显示
- [x] 测试 `show --enable-comment` 命令是否能正确启用评论显示
- [x] 测试 `show --disable-comment` 命令是否能正确禁用评论显示
- [x] 测试开关状态是否能够持久化保存
- [x] 测试与现有功能的兼容性