# 配置系统重构改进规范

## 1. 为什么

当前配置系统存在架构混乱、访问方式不统一、类型不安全等问题，导致学习成本高、维护困难、运行时错误风险高。需要重构配置系统，提供统一的访问接口、类型安全的配置键、配置变更通知机制等功能，提高代码可维护性和用户体验。

## 2. 变更内容

### 2.1 高优先级改进
- 创建统一的配置访问接口 `ConfigManager`，提供一致的访问方式
- 创建类型安全的配置键 `ConfigKey`，避免字符串键名的拼写错误
- 消除配置重复存储（`current_style` 重复存储问题）
- 添加颜色类型支持 `ColorConfig`，替代字符串格式的颜色配置

### 2.2 中优先级改进
- 实现配置变更通知机制，支持配置变更后自动更新相关功能
- 统一配置存储策略，明确各层配置的职责和存储方式
- 添加配置验证机制，防止非法配置值

### 2.3 低优先级改进
- 引入配置版本管理，支持配置迁移
- 实现配置导入导出，方便用户备份和迁移配置
- 添加配置加密支持，对敏感配置提供加密存储

## 3. 影响范围

- 影响规范：无
- 影响代码：
  - `src/main/java/com/hipoom/cli/todo/Config-ext.kt` - 配置扩展（重构）
  - `src/main/java/com/hipoom/cli/todo/CliApp-ext.kt` - App 级别配置（重构）
  - `src/main/java/com/hipoom/cli/todo/workspace-ext.kt` - Workspace 配置（重构）
  - `src/main/java/com/hipoom/cli/todo/handler/config/` - 配置处理（重构）
  - `src/main/java/com/hipoom/cli/todo/handler/style/` - 样式配置（重构）
  - 新增 `config/` 目录 - 统一配置管理

## 4. 当前问题分析

### 4.1 架构层面问题

#### 4.1.1 问题：配置层级混乱

**问题描述**：
- 当前配置分为三层（进程、App、Workspace），但各层配置的职责边界不够清晰
- 部分配置的存储位置选择不够合理，如 `current_style` 在 Workspace 级别，而 `styles` 在 App 级别
- 样式配置存在重复存储：`show.currentStyle` 和 `workspace.current_style` 都存储当前样式名称

**影响**：
- 开发者难以确定新配置应该放在哪一层
- 配置迁移和同步困难
- 容易出现配置不一致的问题

#### 4.1.2 问题：配置访问方式不统一

**问题描述**：
- Workspace 级别配置有多种访问方式：
  - `Configs.show.xxx` - 便捷对象访问
  - `workspace.saveConfig()` / `workspace.queryConfig()` - 键值对访问
  - `workspace.getCurrentStyleName()` - 专用方法访问
- App 级别配置通过 `PersistentData` 对象访问
- 进程级别配置通过 `ProcessData` 对象访问

**影响**：
- 学习成本高，需要记忆多种访问方式
- 代码风格不统一
- 维护困难

### 4.2 代码层面问题

#### 4.2.1 问题：配置类设计不一致

**问题描述**：
- `Configs`、`Show`、`Window`、`Launch` 等对象使用 object 单例模式
- `Styles` 使用 object 单例但包含可变状态 `styles`
- `StyleStorage`、`TextMappingStorage` 使用 object 单例提供静态方法
- `AutoLabelRule` 是数据类，存储在 Workspace 数据库中

**影响**：
- 代码风格不统一
- 部分配置需要手动加载，部分自动加载
- 状态管理复杂

#### 4.2.2 问题：缺乏类型安全

**问题描述**：
- `ConfigGroup` 使用字符串键名访问配置
- 颜色值使用字符串格式 "R,G,B"，需要手动解析
- 数据库键名使用字符串常量，容易拼写错误

**影响**：
- 运行时错误风险高
- IDE 无法提供代码补全
- 重构困难

#### 4.2.3 问题：配置变更通知缺失

**问题描述**：
- 配置变更后没有通知机制
- 部分配置变更后需要手动调用 `storeCurrentConfigs()` 保存
- 配置变更后相关功能不会自动更新

**影响**：
- 配置变更可能不生效
- 需要重启应用才能看到效果
- 用户体验差

### 4.3 文档层面问题

#### 4.3.1 问题：配置文档缺失

**问题描述**：
- 缺乏统一的配置文档
- 配置项说明分散在代码注释中
- 新开发者需要阅读源码才能了解配置系统

**影响**：
- 学习成本高
- 容易误用配置

## 5. 新增需求

### 5.1 需求：统一配置访问接口

系统应当提供统一的配置访问接口 `ConfigManager`，支持类型安全的配置读写和变更监听。

#### 5.1.1 接口设计

```kotlin
/**
 * 统一配置管理器接口
 */
interface ConfigManager {
    
    /**
     * 获取配置值
     * @param key 配置键
     * @return 配置值
     */
    fun <T> get(key: ConfigKey<T>): T
    
    /**
     * 设置配置值
     * @param key 配置键
     * @param value 配置值
     */
    fun <T> set(key: ConfigKey<T>, value: T)
    
    /**
     * 监听配置变更
     * @param key 配置键
     * @param observer 变更监听器
     */
    fun <T> observe(key: ConfigKey<T>, observer: (T) -> Unit)
    
    /**
     * 移除配置变更监听
     * @param key 配置键
     * @param observer 变更监听器
     */
    fun <T> removeObserver(key: ConfigKey<T>, observer: (T) -> Unit)
}
```

#### 5.1.2 场景：获取配置值

- **当** 开发者需要获取某个配置值时
- **那么** 系统应提供类型安全的 `get` 方法，返回正确类型的配置值

#### 5.1.3 场景：设置配置值

- **当** 开发者需要设置某个配置值时
- **那么** 系统应提供类型安全的 `set` 方法，自动保存配置并触发变更通知

#### 5.1.4 场景：监听配置变更

- **当** 开发者需要监听配置变更时
- **那么** 系统应提供 `observe` 方法，支持注册变更监听器

### 5.2 需求：类型安全的配置键

系统应当提供类型安全的配置键定义，避免字符串键名的拼写错误。

#### 5.2.1 ConfigScope 枚举设计

```kotlin
package com.hipoom.cli.todo.config

/**
 * 配置作用域枚举
 * 定义配置的存储层级和生命周期
 */
enum class ConfigScope {
    /** 
     * 进程级别：运行时临时状态，进程结束后消失
     * 存储位置：内存
     * 适用场景：当前工作空间上下文、命令前缀等临时状态
     */
    PROCESS,
    
    /** 
     * App 级别：跨工作空间共享配置，永久存储
     * 存储位置：数据库
     * 适用场景：样式配置、文本映射、当前工作空间别名等全局配置
     */
    APP,
    
    /** 
     * Workspace 级别：工作空间特定配置，永久存储
     * 存储位置：configs.json 文件或数据库键值对
     * 适用场景：显示配置、模板、自动标签规则等工作空间特定配置
     */
    WORKSPACE
}
```

#### 5.2.2 ConfigKey 类设计

```kotlin
package com.hipoom.cli.todo.config

/**
 * 类型安全的配置键
 * 
 * 用于定义配置项的元数据，包括配置名称、默认值、作用域和验证器。
 * 通过泛型参数 T 确保配置值的类型安全。
 *
 * @param T 配置值的类型
 * @property name 配置名称，用于存储和读取配置时的唯一标识
 * @property defaultValue 配置的默认值，当配置不存在时返回此值
 * @property scope 配置的作用域，决定配置的存储位置和生命周期
 * @property description 配置项的描述说明，用于文档和错误提示
 * @property validator 配置验证器（可选），用于验证配置值的合法性
 *
 * @example
 * ```kotlin
 * // 定义一个布尔类型的配置键
 * val NEED_SHOW_ID = ConfigKey(
 *     name = "show.needShowId",
 *     defaultValue = true,
 *     scope = ConfigScope.WORKSPACE,
 *     description = "是否展示事项的 ID"
 * )
 *
 * // 定义一个颜色类型的配置键，带验证器
 * val COMMENT_TEXT_COLOR = ConfigKey(
 *     name = "show.commentStyle.textColor",
 *     defaultValue = ColorConfig(128, 128, 128),
 *     scope = ConfigScope.WORKSPACE,
 *     description = "备注文字的颜色，格式是 RGB",
 *     validator = ColorValidator()
 * )
 * ```
 */
class ConfigKey<T>(
    val name: String,
    val defaultValue: T,
    val scope: ConfigScope,
    val description: String = "",
    val validator: ConfigValidator<T>? = null
) {
    
    /* ======================================================= */
    /* 公共方法                                                */
    /* ======================================================= */
    
    /**
     * 验证配置值是否合法
     * @param value 待验证的配置值
     * @return 验证结果，合法返回 true，否则返回 false
     */
    fun isValid(value: T): Boolean {
        // 如果没有验证器，默认通过
        if (validator == null) {
            return true
        }
        return validator.validate(value)
    }
    
    /**
     * 获取验证失败的错误信息
     * @param value 验证失败的配置值
     * @return 错误信息字符串
     */
    fun getValidationError(value: T): String {
        if (validator == null) {
            return "配置项 '$name' 没有配置验证器"
        }
        return "配置项 '$name' 验证失败: ${validator.getErrorMessage(value)}"
    }
    
    /**
     * 获取配置的完整路径名称
     * 格式: scope:name
     * @return 完整路径名称
     */
    fun getFullPath(): String {
        return "${scope.name.lowercase()}:$name"
    }
    
    /* ======================================================= */
    /* 重写方法                                                */
    /* ======================================================= */
    
    override fun toString(): String {
        return "ConfigKey(name='$name', scope=$scope, type=${defaultValue?.javaClass?.simpleName ?: "null"})"
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ConfigKey<*>) return false
        return name == other.name && scope == other.scope
    }
    
    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + scope.hashCode()
        return result
    }
}
```

#### 5.2.3 配置键定义文件示例

```kotlin
package com.hipoom.cli.todo.config.keys

import com.hipoom.cli.todo.config.ConfigKey
import com.hipoom.cli.todo.config.ConfigScope
import com.hipoom.cli.todo.config.types.ColorConfig
import com.hipoom.cli.todo.config.validators.ColorValidator

/**
 * Show 相关配置键定义
 * 所有配置键统一在此文件中定义，便于管理和维护
 */
object ShowConfigKeys {
    
    /* ======================================================= */
    /* 显示控制配置                                            */
    /* ======================================================= */
    
    /** 是否展示事项的 ID */
    val NEED_SHOW_ID = ConfigKey(
        name = "show.needShowId",
        defaultValue = true,
        scope = ConfigScope.WORKSPACE,
        description = "是否展示事项的 ID"
    )
    
    /** 是否展示事项的状态 */
    val NEED_SHOW_STATUS = ConfigKey(
        name = "show.needShowStatus",
        defaultValue = true,
        scope = ConfigScope.WORKSPACE,
        description = "是否展示事项的状态"
    )
    
    /** 是否展示已完成事项 */
    val NEED_SHOW_DONE = ConfigKey(
        name = "show.needShowDone",
        defaultValue = false,
        scope = ConfigScope.WORKSPACE,
        description = "是否展示已被标记完成的事项"
    )
    
    /** 是否展示已删除事项 */
    val NEED_SHOW_DELETED = ConfigKey(
        name = "show.needShowDeleted",
        defaultValue = false,
        scope = ConfigScope.WORKSPACE,
        description = "是否展示已被标记删除的事项"
    )
    
    /** 是否展示负责人 */
    val NEED_SHOW_OWNER = ConfigKey(
        name = "show.needShowOwner",
        defaultValue = true,
        scope = ConfigScope.WORKSPACE,
        description = "是否展示事项的负责人"
    )
    
    /** 是否展示标签 */
    val NEED_SHOW_LABEL = ConfigKey(
        name = "show.needShowLabel",
        defaultValue = true,
        scope = ConfigScope.WORKSPACE,
        description = "是否展示事项的标签"
    )
    
    /** 是否使用高级对齐模式 */
    val USE_ALIGN_MODE = ConfigKey(
        name = "show.useAlignMode",
        defaultValue = false,
        scope = ConfigScope.WORKSPACE,
        description = "是否使用高级对齐模式，仅在控制台字体能够严格保证一个中文字符的宽度是英文字体两倍时使用"
    )
    
    /** 是否展示截止时间 */
    val NEED_SHOW_DEADLINE = ConfigKey(
        name = "show.needShowDeadline",
        defaultValue = true,
        scope = ConfigScope.WORKSPACE,
        description = "是否需要展示截止时间"
    )
    
    /* ======================================================= */
    /* 备注样式配置                                            */
    /* ======================================================= */
    
    /** 备注文字颜色 */
    val COMMENT_TEXT_COLOR = ConfigKey(
        name = "show.commentStyle.textColor",
        defaultValue = ColorConfig(128, 128, 128),
        scope = ConfigScope.WORKSPACE,
        description = "备注文字的颜色，格式是 RGB",
        validator = ColorValidator()
    )
    
    /** 备注背景颜色 */
    val COMMENT_BACKGROUND_COLOR = ConfigKey(
        name = "show.commentStyle.backgroundColor",
        defaultValue = ColorConfig.NONE,
        scope = ConfigScope.WORKSPACE,
        description = "备注文字的背景颜色，格式是 RGB。None 表示使用默认值",
        validator = ColorValidator()
    )
    
    /* ======================================================= */
    /* 状态图标配置                                            */
    /* ======================================================= */
    
    /** 新事项状态图标 */
    val STATUS_NEW = ConfigKey(
        name = "show.status.new",
        defaultValue = "◌",
        scope = ConfigScope.WORKSPACE,
        description = "状态为 new 的事项怎么展示状态"
    )
    
    /** 进行中状态图标 */
    val STATUS_DOING = ConfigKey(
        name = "show.status.doing",
        defaultValue = "~",
        scope = ConfigScope.WORKSPACE,
        description = "状态为 doing 的事项怎么展示状态"
    )
    
    /** 已完成状态图标 */
    val STATUS_DONE = ConfigKey(
        name = "show.status.done",
        defaultValue = "✔",
        scope = ConfigScope.WORKSPACE,
        description = "状态为 done 的事项怎么展示状态"
    )
    
    /** 已删除状态图标 */
    val STATUS_DELETED = ConfigKey(
        name = "show.status.deleted",
        defaultValue = "×",
        scope = ConfigScope.WORKSPACE,
        description = "状态为 deleted 的事项怎么展示状态"
    )
}
```

#### 5.2.4 场景：定义配置键

- **当** 开发者需要定义新的配置项时
- **那么** 系统应提供 `ConfigKey` 类，支持指定配置名称、默认值、作用域、描述和验证器

#### 5.2.5 场景：IDE 代码补全

- **当** 开发者在 IDE 中编写配置访问代码时
- **那么** 系统应支持 IDE 代码补全，显示所有可用的配置键（通过 object 单例中的静态属性）

#### 5.2.6 场景：配置值验证

- **当** 开发者设置配置值时
- **那么** 系统应通过 `ConfigKey.validator` 验证配置值的合法性，验证失败时提供清晰的错误信息

### 5.3 需求：颜色类型支持

系统应当提供 `ColorConfig` 类型，替代字符串格式的颜色配置。

#### 5.3.1 类设计

```kotlin
/**
 * 颜色配置数据类
 * @param r 红色分量 (0-255)
 * @param g 绿色分量 (0-255)
 * @param b 蓝色分量 (0-255)
 */
data class ColorConfig(
    val r: Int,
    val g: Int,
    val b: Int
) {
    /**
     * 转换为 Color 对象
     */
    fun toColor(): Color = Colors.Bits24.createForeground(r, g, b)
    
    /**
     * 转换为背景 Color 对象
     */
    fun toBackgroundColor(): Color = Colors.Bits24.createBackground(r, g, b)
    
    /**
     * 转换为字符串格式 "R,G,B"
     */
    override fun toString(): String = "$r,$g,$b"
    
    companion object {
        /**
         * 从字符串解析颜色配置
         * @param str 格式为 "R,G,B" 的字符串
         * @return ColorConfig 对象，解析失败返回 null
         */
        fun fromString(str: String): ColorConfig? {
            val parts = str.split(",").mapNotNull { it.trim().toIntOrNull() }
            if (parts.size != 3) return null
            if (parts.any { it !in 0..255 }) return null
            return ColorConfig(parts[0], parts[1], parts[2])
        }
        
        /**
         * 空颜色配置（表示使用默认值）
         */
        val NONE = ColorConfig(-1, -1, -1)
    }
}
```

#### 5.3.2 场景：解析颜色字符串

- **当** 系统从配置文件读取颜色值时
- **那么** 系统应自动将 "R,G,B" 格式的字符串解析为 `ColorConfig` 对象

#### 5.3.3 场景：转换颜色对象

- **当** 开发者需要使用颜色时
- **那么** 系统应提供 `toColor()` 方法，将 `ColorConfig` 转换为 `Color` 对象

### 5.4 需求：消除配置重复存储

系统应当消除配置重复存储问题，确保配置的一致性。

#### 5.4.1 场景：当前样式名称存储

- **当** 用户选择新的样式时
- **那么** 系统应只在 App 级别存储当前样式名称，删除 Workspace 级别的重复存储

#### 5.4.2 需要删除的重复配置

| 配置项 | 当前存储位置 | 迁移后存储位置 |
|--------|--------------|----------------|
| current_style | Workspace 级别 | App 级别（StyleConfigs.currentStyleName） |

### 5.5 需求：配置变更通知

系统应当提供配置变更通知机制，支持配置变更后自动更新相关功能。

#### 5.5.1 接口设计

```kotlin
/**
 * 配置变更观察者接口
 */
interface ConfigObserver {
    /**
     * 配置变更回调
     * @param key 配置键名
     * @param oldValue 旧值
     * @param newValue 新值
     */
    fun onConfigChanged(key: String, oldValue: Any?, newValue: Any?)
}
```

#### 5.5.2 场景：注册变更监听器

- **当** 某个功能需要响应配置变更时
- **那么** 系统应允许注册配置变更监听器

#### 5.5.3 场景：触发变更通知

- **当** 配置值被修改时
- **那么** 系统应自动通知所有注册的监听器

### 5.6 需求：配置验证机制

系统应当提供配置验证机制，防止非法配置值。

#### 5.6.1 接口设计

```kotlin
/**
 * 配置验证器接口
 */
interface ConfigValidator<T> {
    /**
     * 验证配置值是否合法
     * @param value 配置值
     * @return 是否合法
     */
    fun validate(value: T): Boolean
    
    /**
     * 获取验证失败的错误信息
     * @param value 配置值
     * @return 错误信息
     */
    fun getErrorMessage(value: T): String
}

/**
 * 颜色验证器
 */
class ColorValidator : ConfigValidator<ColorConfig> {
    override fun validate(value: ColorConfig): Boolean {
        // NONE 表示使用默认值，跳过验证
        if (value == ColorConfig.NONE) return true
        return value.r in 0..255 && value.g in 0..255 && value.b in 0..255
    }
    
    override fun getErrorMessage(value: ColorConfig): String {
        return "颜色值必须在 0-255 范围内，当前值: ${value.r},${value.g},${value.b}"
    }
}

/**
 * 布尔验证器
 */
class BooleanValidator : ConfigValidator<Boolean> {
    override fun validate(value: Boolean): Boolean = true
    
    override fun getErrorMessage(value: Boolean): String = "布尔值验证失败"
}
```

#### 5.6.2 场景：保存前验证

- **当** 开发者尝试保存配置值时
- **那么** 系统应先验证配置值是否合法，不合法则拒绝保存并提示错误

#### 5.6.3 场景：验证错误提示

- **当** 配置值验证失败时
- **那么** 系统应提供清晰的错误信息，说明验证失败的原因

### 5.7 需求：统一配置存储策略

系统应当明确各层配置的职责和存储策略。

#### 5.7.1 存储策略定义

| 层级 | 职责 | 存储方式 | 生命周期 | 适用配置 |
|------|------|----------|----------|----------|
| 进程级别 | 运行时临时状态 | 内存 | 进程运行期间 | 当前工作空间、命令前缀 |
| App 级别 | 跨工作空间共享配置 | 数据库 | 永久存储 | 样式、文本映射、当前工作空间别名 |
| Workspace 级别 | 工作空间特定配置 | 文件/数据库 | 永久存储 | 显示配置、模板、自动标签规则 |

#### 5.7.2 场景：选择配置存储层级

- **当** 开发者需要添加新的配置项时
- **那么** 系统应根据配置的用途自动选择合适的存储层级

## 6. 重构建议

### 6.1 当前代码结构

```
Config-ext.kt
├── Configs (object)
│   ├── Window (object)
│   ├── Show (object)
│   │   ├── CommentStyle (object)
│   │   ├── Status (object)
│   │   └── Icon (object)
│   └── Launch (object)
├── Focus (object)
└── BooleanField (class)
```

### 6.2 建议代码结构

```
config/
├── ConfigManager.kt              // 统一配置管理器接口
├── ConfigManagerImpl.kt          // 配置管理器实现
├── ConfigKey.kt                  // 类型安全的配置键
├── ConfigScope.kt                // 配置作用域枚举
├── ConfigObserver.kt             // 配置变更观察者接口
├── validators/                   // 配置验证器
│   ├── ConfigValidator.kt        // 验证器接口
│   ├── ColorValidator.kt         // 颜色验证器
│   └── BooleanValidator.kt       // 布尔验证器
├── types/                        // 配置类型
│   ├── ColorConfig.kt            // 颜色配置类型
│   └── StyleConfig.kt            // 样式配置类型
├── storage/                      // 配置存储
│   ├── ConfigStorage.kt          // 存储接口
│   ├── ProcessConfigStorage.kt   // 进程级别存储
│   ├── AppConfigStorage.kt       // App 级别存储
│   └── WorkspaceConfigStorage.kt // Workspace 级别存储
└── keys/                         // 配置键定义
    ├── ShowConfigKeys.kt         // Show 配置键
    ├── WindowConfigKeys.kt       // Window 配置键
    ├── LaunchConfigKeys.kt       // Launch 配置键
    ├── StyleConfigKeys.kt        // Style 配置键
    └── ProcessConfigKeys.kt      // 进程级别配置键
```

### 6.3 迁移步骤

1. **第一阶段**：创建新的配置管理架构，保持向后兼容
   - 创建新的配置类型和接口
   - 创建配置存储层
   - 创建统一配置管理器
   - 定义配置键常量

2. **第二阶段**：逐步迁移现有配置到新架构
   - 迁移 Workspace 级别配置
   - 迁移 App 级别配置
   - 迁移进程级别配置

3. **第三阶段**：删除旧的配置访问方式
   - 消除配置重复存储
   - 更新所有 Handler 使用新架构

4. **第四阶段**：添加配置迁移和验证功能
   - 实现配置验证功能
   - 实现配置变更通知
   - 清理旧代码

## 7. 实施优先级总结

| 优先级 | 改进项 | 工作量 | 收益 | 阶段 |
|--------|--------|--------|------|------|
| 高 | 统一配置访问接口 | 中等 | 高 | 第一阶段 |
| 高 | 消除配置重复存储 | 小 | 高 | 第三阶段 |
| 高 | 添加颜色类型支持 | 小 | 中 | 第一阶段 |
| 中 | 实现配置变更通知 | 中等 | 高 | 第四阶段 |
| 中 | 统一配置存储策略 | 小 | 中 | 第一阶段 |
| 中 | 添加配置验证机制 | 中等 | 中 | 第四阶段 |
| 低 | 引入配置版本管理 | 大 | 中 | 后续规划 |
| 低 | 实现配置导入导出 | 中等 | 低 | 后续规划 |
| 低 | 添加配置加密支持 | 大 | 低 | 后续规划 |

## 8. 修改的需求

无

## 9. 移除的需求

### 9.1 需求：旧的配置访问方式

**原因**：访问方式不统一，学习成本高
**迁移**：逐步迁移到新的 `ConfigManager` 接口，保持向后兼容过渡期

### 9.2 需求：workspace.getCurrentStyleName() 方法

**原因**：与 App 级别的样式配置重复
**迁移**：使用 `StyleStorage` 或 `ConfigManager` 获取当前样式名称

### 9.3 需求：workspace.setCurrentStyleName() 方法

**原因**：与 App 级别的样式配置重复
**迁移**：使用 `StyleStorage` 或 `ConfigManager` 设置当前样式名称

### 9.4 需求：show.currentStyle 配置项

**原因**：与 StyleConfigs.currentStyleName 重复
**迁移**：使用 StyleConfigs.currentStyleName 存储当前样式名称
