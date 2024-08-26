package com.github.jcestaro.authorizer.configuration.exception.business.payload;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidAccountExceptionTest {

    InvalidAccountException exception;

    @Test
    @DisplayName("Invalid account exception must have clear message")
    public void test01() {
        whenCreateNewInvalidAccountException();
        thenShouldHaveExpectedMessage();
    }

    private void whenCreateNewInvalidAccountException() {
        exception = new InvalidAccountException();
    }

    private void thenShouldHaveExpectedMessage() {
        String expectedMessage = "The account must be informed";
        assertEquals(expectedMessage, exception.getMessage());
    }

}