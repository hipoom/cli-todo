# CLI Todo - 已完成事项绿色打印功能 - 实施计划

## [ ] Task 1: 分析现有的显示逻辑
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 分析 `common.kt` 中的 `List<TreeModeRow>.show()` 方法，了解当前的显示逻辑
  - 分析 `ShowModeUtils.showAsOwnerMode()` 方法，了解所有者模式的显示逻辑
- **Acceptance Criteria Addressed**: [AC-1, AC-2, AC-3]
- **Test Requirements**:
  - `human-judgement` TR-1.1: 确认已理解现有的显示逻辑和样式应用方式

## [ ] Task 2: 实现树状模式下的绿色显示
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 修改 `List<TreeModeRow>.show()` 方法，为已完成的任务应用绿色样式
  - 确保其他状态的任务保持原有颜色
- **Acceptance Criteria Addressed**: [AC-1, AC-2]
- **Test Requirements**:
  - `human-judgement` TR-2.1: 已完成的任务在树状模式下显示为绿色
  - `human-judgement` TR-2.2: 未完成的任务保持原有颜色

## [ ] Task 3: 实现所有者模式下的绿色显示
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 修改 `ShowModeUtils.showAsOwnerMode()` 方法，为已完成的任务应用绿色样式
  - 确保其他状态的任务保持原有颜色
- **Acceptance Criteria Addressed**: [AC-1, AC-2, AC-3]
- **Test Requirements**:
  - `human-judgement` TR-3.1: 已完成的任务在所有者模式下显示为绿色
  - `human-judgement` TR-3.2: 未完成的任务保持原有颜色

## [ ] Task 4: 测试所有显示模式
- **Priority**: P1
- **Depends On**: Task 2, Task 3
- **Description**: 
  - 测试树状模式和所有者模式下的显示效果
  - 确保已完成的任务在所有模式下都显示为绿色
  - 确保其他状态的任务颜色不变
- **Acceptance Criteria Addressed**: [AC-1, AC-2, AC-3]
- **Test Requirements**:
  - `human-judgement` TR-4.1: 已完成的任务在树状模式下显示为绿色
  - `human-judgement` TR-4.2: 已完成的任务在所有者模式下显示为绿色
  - `human-judgement` TR-4.3: 未完成的任务在所有模式下保持原有颜色

## [ ] Task 5: 验证代码质量
- **Priority**: P2
- **Depends On**: Task 4
- **Description**: 
  - 检查代码是否遵循现有代码风格
  - 确保实现简洁，不引入不必要的复杂性
- **Acceptance Criteria Addressed**: [NFR-2]
- **Test Requirements**:
  - `human-judgement` TR-5.1: 代码风格与现有代码一致
  - `human-judgement` TR-5.2: 实现简洁明了