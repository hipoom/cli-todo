package com.hipoom.cli.todo.config

import com.hipoom.cli.todo.config.keys.ShowConfigKeys
import com.hipoom.cli.todo.config.storage.ProcessConfigStorage
import com.hipoom.cli.todo.config.types.ColorConfig
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * ConfigManager 单元测试
 */
class ConfigManagerTest {
    
    private lateinit var manager: ConfigManager
    
    /* ======================================================= */
    /* 初始化                                                   */
    /* ======================================================= */
    
    @BeforeEach
    fun setup() {
        // 1.1 清空进程级别存储
        ProcessConfigStorage.clear()
        // 1.2 创建新的配置管理器实例
        manager = ConfigManagerImpl()
    }
    
    /* ======================================================= */
    /* 测试 get/set 方法                                        */
    /* ======================================================= */
    
    /**
     * 2.1 测试获取默认值
     */
    @Test
    fun testGet_defaultValue() {
        // 2.1.1 测试布尔类型默认值
        val showId = manager.get(ShowConfigKeys.NEED_SHOW_ID)
        assertTrue(showId)
        
        // 2.1.2 测试字符串类型默认值
        val statusNew = manager.get(ShowConfigKeys.STATUS_NEW)
        assertEquals("◌", statusNew)
    }
    
    /**
     * 2.2 测试设置和获取值
     */
    @Test
    fun testSetAndGet() {
        // 2.2.1 设置布尔值
        manager.set(ShowConfigKeys.NEED_SHOW_ID, false)
        assertFalse(manager.get(ShowConfigKeys.NEED_SHOW_ID))
        
        // 2.2.2 设置字符串值
        manager.set(ShowConfigKeys.STATUS_NEW, "●")
        assertEquals("●", manager.get(ShowConfigKeys.STATUS_NEW))
    }
    
    /* ======================================================= */
    /* 测试验证功能                                             */
    /* ======================================================= */
    
    /**
     * 3.1 测试颜色验证 - 有效值
     */
    @Test
    fun testValidation_validColor() {
        // 3.1.1 有效的颜色值应该可以设置
        val validColor = ColorConfig(255, 128, 64)
        assertDoesNotThrow {
            manager.set(ShowConfigKeys.COMMENT_TEXT_COLOR, validColor)
        }
        assertEquals(validColor, manager.get(ShowConfigKeys.COMMENT_TEXT_COLOR))
    }
    
    /**
     * 3.2 测试颜色验证 - 无效值
     */
    @Test
    fun testValidation_invalidColor() {
        // 3.2.1 无效的颜色值应该抛出异常
        val invalidColor = ColorConfig(300, -1, 128)
        assertThrows(IllegalArgumentException::class.java) {
            manager.set(ShowConfigKeys.COMMENT_TEXT_COLOR, invalidColor)
        }
    }
    
    /* ======================================================= */
    /* 测试观察者模式                                           */
    /* ======================================================= */
    
    /**
     * 4.1 测试配置变更通知
     */
    @Test
    fun testObserve_configChange() {
        // 4.1.1 记录变更通知次数
        var notificationCount = 0
        var lastValue: Boolean? = null
        
        // 4.1.2 注册观察者
        manager.observe(ShowConfigKeys.NEED_SHOW_ID) { newValue ->
            notificationCount++
            lastValue = newValue
        }
        
        // 4.1.3 修改配置
        manager.set(ShowConfigKeys.NEED_SHOW_ID, false)
        
        // 4.1.4 验证通知
        assertEquals(1, notificationCount)
        assertEquals(false, lastValue)
    }
    
    /**
     * 4.2 测试移除观察者
     */
    @Test
    fun testRemoveObserver() {
        // 4.2.1 记录变更通知次数
        var notificationCount = 0
        
        // 4.2.2 定义观察者
        val observer: (Boolean) -> Unit = { notificationCount++ }
        
        // 4.2.3 注册观察者
        manager.observe(ShowConfigKeys.NEED_SHOW_ID, observer)
        
        // 4.2.4 修改配置，应该收到通知
        manager.set(ShowConfigKeys.NEED_SHOW_ID, false)
        assertEquals(1, notificationCount)
        
        // 4.2.5 移除观察者
        manager.removeObserver(ShowConfigKeys.NEED_SHOW_ID, observer)
        
        // 4.2.6 再次修改配置，不应该收到通知
        manager.set(ShowConfigKeys.NEED_SHOW_ID, true)
        assertEquals(1, notificationCount) // 仍然是 1
    }
    
    /* ======================================================= */
    /* 测试 exists 和 remove 方法                              */
    /* ======================================================= */
    
    /**
     * 5.1 测试 exists 方法
     */
    @Test
    fun testExists() {
        // 5.1.1 初始状态不存在（因为使用的是 Show 配置键，存储在 WORKSPACE 级别）
        // 注意：这个测试可能受到其他测试的影响，所以这里只测试设置后存在
        manager.set(ShowConfigKeys.NEED_SHOW_ID, true)
        assertTrue(manager.exists(ShowConfigKeys.NEED_SHOW_ID))
    }
    
    /**
     * 5.2 测试 remove 方法
     */
    @Test
    fun testRemove() {
        // 5.2.1 设置配置
        manager.set(ShowConfigKeys.NEED_SHOW_ID, false)
        assertTrue(manager.exists(ShowConfigKeys.NEED_SHOW_ID))
        
        // 5.2.2 删除配置
        manager.remove(ShowConfigKeys.NEED_SHOW_ID)
        assertFalse(manager.exists(ShowConfigKeys.NEED_SHOW_ID))
        
        // 5.2.3 删除后获取默认值
        val value = manager.get(ShowConfigKeys.NEED_SHOW_ID)
        assertTrue(value) // 默认值是 true
    }
}
