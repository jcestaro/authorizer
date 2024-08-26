package com.github.jcestaro.authorizer.configuration.exception.business.payload;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidMerchantExceptionTest {

    InvalidMerchantException exception;

    @Test
    @DisplayName("Invalid merchant exception must have clear message")
    public void test01() {
        whenCreateNewInvalidMerchantException();
        thenShouldHaveExpectedMessage();
    }

    private void whenCreateNewInvalidMerchantException() {
        exception = new InvalidMerchantException();
    }

    private void thenShouldHaveExpectedMessage() {
        String expectedMessage = "The merchant must be informed";
        assertEquals(expectedMessage, exception.getMessage());
    }

}