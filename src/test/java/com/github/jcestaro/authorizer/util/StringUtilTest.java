package com.github.jcestaro.authorizer.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    @Test
    @DisplayName("Null string should be empty when testing if is empty")
    void test01() {
        String nullString = null;
        boolean isEmpty = StringUtil.isEmpty(nullString);
        assertTrue(isEmpty);
    }

    @Test
    @DisplayName("Empty string should be empty when testing if is empty")
    void test02() {
        String emptyString = "";
        boolean isEmpty = StringUtil.isEmpty(emptyString);
        assertTrue(isEmpty);
    }

    @Test
    @DisplayName("Empty string with whitespaces should be empty when testing if is empty")
    void test03() {
        String emptyString = "     ";
        boolean isEmpty = StringUtil.isEmpty(emptyString);
        assertTrue(isEmpty);
    }

    @Test
    @DisplayName("Filled string should not be empty when testing if is empty")
    void test04() {
        String string = "Some Text";
        boolean isEmpty = StringUtil.isEmpty(string);
        assertFalse(isEmpty);
    }

    @Test
    @DisplayName("Filled string should not be empty when testing if is empty")
    void test05() {
        String string = "Some Text";
        boolean isEmpty = StringUtil.isEmpty(string);
        assertFalse(isEmpty);
    }

    @Test
    @DisplayName("Must return the own string when its not null and call to empty if null")
    void test06() {
        String string = "Some Text";
        String convertedString = StringUtil.toEmptyIfNull(string);
        assertEquals(string, convertedString);
    }

    @Test
    @DisplayName("Must return empty string when its null and call to empty if null")
    void test07() {
        String convertedString = StringUtil.toEmptyIfNull(null);
        assertEquals("", convertedString);
    }

}