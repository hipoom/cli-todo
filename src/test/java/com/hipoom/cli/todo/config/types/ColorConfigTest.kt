package com.hipoom.cli.todo.config.types

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * ColorConfig 单元测试
 */
class ColorConfigTest {
    
    /* ======================================================= */
    /* 测试 fromString 方法                                     */
    /* ======================================================= */
    
    /**
     * 1.1 测试有效的颜色字符串解析
     */
    @Test
    fun testFromString_validColor() {
        // 1.1.1 测试标准格式
        val color = ColorConfig.fromString("128,128,128")
        assertNotNull(color)
        assertEquals(128, color!!.r)
        assertEquals(128, color.g)
        assertEquals(128, color.b)
        
        // 1.1.2 测试带空格的格式
        val colorWithSpaces = ColorConfig.fromString("255, 0, 128")
        assertNotNull(colorWithSpaces)
        assertEquals(255, colorWithSpaces!!.r)
        assertEquals(0, colorWithSpaces.g)
        assertEquals(128, colorWithSpaces.b)
    }
    
    /**
     * 1.2 测试无效的颜色字符串解析
     */
    @Test
    fun testFromString_invalidColor() {
        // 1.2.1 测试格式错误
        assertNull(ColorConfig.fromString("128,128"))
        assertNull(ColorConfig.fromString("128,128,128,128"))
        assertNull(ColorConfig.fromString(""))
        assertNull(ColorConfig.fromString("invalid"))
        
        // 1.2.2 测试超出范围的值
        assertNull(ColorConfig.fromString("256,128,128"))
        assertNull(ColorConfig.fromString("128,-1,128"))
    }
    
    /* ======================================================= */
    /* 测试 toString 方法                                       */
    /* ======================================================= */
    
    /**
     * 2.1 测试 toString 方法
     */
    @Test
    fun testToString() {
        val color = ColorConfig(128, 64, 32)
        assertEquals("128,64,32", color.toString())
    }
    
    /* ======================================================= */
    /* 测试 NONE 常量                                          */
    /* ======================================================= */
    
    /**
     * 3.1 测试 NONE 常量
     */
    @Test
    fun testNone() {
        assertEquals(-1, ColorConfig.NONE.r)
        assertEquals(-1, ColorConfig.NONE.g)
        assertEquals(-1, ColorConfig.NONE.b)
    }
    
    /* ======================================================= */
    /* 测试数据类特性                                          */
    /* ======================================================= */
    
    /**
     * 4.1 测试 equals 方法
     */
    @Test
    fun testEquals() {
        val color1 = ColorConfig(128, 128, 128)
        val color2 = ColorConfig(128, 128, 128)
        val color3 = ColorConfig(255, 255, 255)
        
        assertEquals(color1, color2)
        assertNotEquals(color1, color3)
    }
    
    /**
     * 4.2 测试 copy 方法
     */
    @Test
    fun testCopy() {
        val color1 = ColorConfig(128, 128, 128)
        val color2 = color1.copy(r = 255)
        
        assertEquals(255, color2.r)
        assertEquals(128, color2.g)
        assertEquals(128, color2.b)
    }
}
