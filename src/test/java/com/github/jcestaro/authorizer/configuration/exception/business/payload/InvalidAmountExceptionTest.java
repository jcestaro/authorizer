package com.github.jcestaro.authorizer.configuration.exception.business.payload;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidAmountExceptionTest {

    InvalidAmountException exception;

    @Test
    @DisplayName("Invalid amount exception must have clear message")
    public void test01() {
        whenCreateNewInvalidAmountException();
        thenShouldHaveExpectedMessage();
    }

    private void whenCreateNewInvalidAmountException() {
        exception = new InvalidAmountException();
    }

    private void thenShouldHaveExpectedMessage() {
        String expectedMessage = "The amount of the transaction must be informed";
        assertEquals(expectedMessage, exception.getMessage());
    }

}