# CLI Todo - 已完成事项绿色打印功能

## Overview

* **Summary**: 实现已完成的事项在输出时以绿色打印的功能，提高用户对任务状态的视觉识别度。

* **Purpose**: 让用户能够通过颜色快速区分已完成和未完成的任务，提升用户体验。

* **Target Users**: 使用 CLI Todo 工具的终端用户。

## Goals

* 已完成的事项在显示时使用绿色文本

* 保持其他状态事项的显示颜色不变

* 确保在不同显示模式下（树状模式、所有者模式等）都能正确应用绿色样式

## Non-Goals (Out of Scope)

* 改变其他状态事项的显示颜色

* 修改现有的任务状态逻辑

* 添加新的任务状态类型

## Background & Context

* 参考 `globals.kt` 中的 `printSuccess` 函数，该函数使用绿色样式打印成功信息

* 参考 `common.kt` 中的显示逻辑，特别是 `List<TreeModeRow>.show()` 方法

* 项目使用 `TextStyleBuilder` 来构建文本样式，使用 `TextBlockPrinter` 来打印文本

## Functional Requirements

* **FR-1**: 已完成的事项（status 为 DONE）在显示时使用绿色文本

* **FR-2**: 其他状态的事项保持原有显示颜色不变

* **FR-3**: 绿色样式应适用于所有显示模式（树状模式、所有者模式等）

## Non-Functional Requirements

* **NFR-1**: 颜色应用应保持一致，不影响其他文本的显示

* **NFR-2**: 实现应遵循现有代码风格和架构

## Constraints

* **Technical**: 基于现有的文本样式系统实现，不引入新的依赖

* **Dependencies**: 依赖现有的 `TextStyleBuilder` 和 `TextBlockPrinter` 功能

## Assumptions

* 任务状态 `Item.STATUS_DONE` 表示已完成的任务

* 现有的 `printSuccess` 函数使用的绿色样式是合适的

## Acceptance Criteria

### AC-1: 已完成事项显示为绿色

* **Given**: 存在已完成的任务（status 为 DONE）

* **When**: 使用 `show` 命令显示任务列表

* **Then**: 已完成的任务以绿色文本显示

* **Verification**: `human-judgment`

### AC-2: 其他状态事项颜色不变

* **Given**: 存在未完成的任务（status 为 NEW、DOING 等）

* **When**: 使用 `show` 命令显示任务列表

* **Then**: 未完成的任务保持原有颜色显示

* **Verification**: `human-judgment`

### AC-3: 所有显示模式都应用绿色样式

* **Given**: 存在已完成的任务

* **When**: 在不同显示模式下（树状模式、所有者模式）显示任务列表

* **Then**: 已完成的任务在所有模式下都以绿色文本显示

* **Verification**: `human-judgment`

## Open Questions

* [ ] 无

