# Checklist

- [x] Item 实体包含 order 字段，并能正确序列化/反序列化
- [x] 旧数据迁移逻辑正常工作，order 字段有合理的默认值
- [x] ItemDao 的排序方法（moveUp, moveDown, moveToTop, moveToBottom）正确实现
- [x] 展示时子事项按 order 字段升序排列
- [x] 兼容旧数据：order 为 null 时按 id 排序
- [x] `sort --up` 命令正确上移事项
- [x] `sort --down` 命令正确下移事项
- [x] `sort --top` 命令正确置顶事项
- [x] `sort --bottom` 命令正确置底事项
- [x] SortHandler 已注册到系统中，命令可正常调用
