package com.github.jcestaro.authorizer.configuration.exception.business.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountNotFoundExceptionTest {

    AccountNotFoundException exception;

    @Test
    @DisplayName("Account not found exception must have clear message")
    public void test01() {
        whenCreateNewAccountNotFoundException();
        thenShouldHaveExpectedMessage();
    }

    private void whenCreateNewAccountNotFoundException() {
        exception = new AccountNotFoundException();
    }

    private void thenShouldHaveExpectedMessage() {
        String expectedMessage = "Account not found";
        assertEquals(expectedMessage, exception.getMessage());
    }

}