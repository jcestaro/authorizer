package com.github.jcestaro.authorizer.configuration.exception.business.authorizer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionLockedExceptionTest {

    TransactionLockedException exception;

    @Test
    @DisplayName("Transaction locked exception must have clear message")
    public void test01() {
        whenCreateNewTransactionLockedException();
        thenShouldHaveExpectedMessage();
    }

    private void whenCreateNewTransactionLockedException() {
        exception = new TransactionLockedException();
    }

    private void thenShouldHaveExpectedMessage() {
        String expectedMessage = "There is another transaction running, please wait a few moments and try again";
        assertEquals(expectedMessage, exception.getMessage());
    }

}