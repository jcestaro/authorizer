package com.github.jcestaro.authorizer.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class NumberUtilTest {

    @Test
    @DisplayName("Null number should return false when checking if is zero or null")
    void test01() {
        BigDecimal nullBigDecimal = null;
        boolean isZeroOrNull = NumberUtil.isNullOrZero(nullBigDecimal);
        assertTrue(isZeroOrNull);
    }

    @Test
    @DisplayName("Zero should return true when checking if is zero or null")
    void test02() {
        BigDecimal zero = BigDecimal.ZERO;
        boolean isZeroOrNull = NumberUtil.isNullOrZero(zero);
        assertTrue(isZeroOrNull);
    }

    @Test
    @DisplayName("Negative number should return false when checking if is zero or null")
    void test03() {
        BigDecimal negative = BigDecimal.TEN.negate();
        boolean isZeroOrNull = NumberUtil.isNullOrZero(negative);
        assertFalse(isZeroOrNull);
    }

    @Test
    @DisplayName("Positive number should return false when checking if is zero or null")
    void test04() {
        BigDecimal positive = BigDecimal.TEN;
        boolean isZeroOrNull = NumberUtil.isNullOrZero(positive);
        assertFalse(isZeroOrNull);
    }

    @Test
    @DisplayName("Positive number should return false when checking if is negative")
    void test05() {
        BigDecimal positive = BigDecimal.TEN;
        boolean isZeroOrNull = NumberUtil.isNegative(positive);
        assertFalse(isZeroOrNull);
    }

    @Test
    @DisplayName("Negative number should return true when checking if is negative")
    void test06() {
        BigDecimal positive = BigDecimal.TEN.negate();
        boolean isZeroOrNull = NumberUtil.isNegative(positive);
        assertTrue(isZeroOrNull);
    }

    @Test
    @DisplayName("Parse long should return 0 when passing null value")
    void test07() {
        Long parsed = NumberUtil.parseLong(null);
        assertEquals(0, parsed);
    }

    @Test
    @DisplayName("Parse long should return 0 when passing empty value")
    void test08() {
        Long parsed = NumberUtil.parseLong("");
        assertEquals(0, parsed);
    }

    @Test
    @DisplayName("Parse long should return 0 when passing empty value with whitespaces")
    void test09() {
        Long parsed = NumberUtil.parseLong("        ");
        assertEquals(0, parsed);
    }

    @Test
    @DisplayName("Parse long should return 0 when passing string value with characteres")
    void test10() {
        Long parsed = NumberUtil.parseLong(" 123abc ");
        assertEquals(0, parsed);
    }

    @Test
    @DisplayName("Parse long should return 0 when passing string value with special characteres")
    void test11() {
        Long parsed = NumberUtil.parseLong(" 123/*.!@# ");
        assertEquals(0, parsed);
    }

    @Test
    @DisplayName("Parse long should return the long value when passing string value with only numbers")
    void test12() {
        Long parsed = NumberUtil.parseLong("123");
        assertEquals(123, parsed);
    }

}