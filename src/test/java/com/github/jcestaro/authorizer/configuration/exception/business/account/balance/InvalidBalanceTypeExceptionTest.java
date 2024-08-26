package com.github.jcestaro.authorizer.configuration.exception.business.account.balance;

import com.github.jcestaro.authorizer.domain.model.account.balance.BalanceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidBalanceTypeExceptionTest {

    BalanceType invalidBalanceType;
    private InvalidBalanceTypeException exception;

    @Test
    @DisplayName("Invalid balance type exception must have clear message")
    public void invalidBalanceTypeExceptionMustHaveClearMessage() {
        givenBalanceType();
        whenCreateNewInvalidBalanceTypeException();
        thenShouldHaveExpectedMessage();
    }

    @Test
    @DisplayName("Invalid balance type exception with account ID must have clear message")
    public void invalidBalanceTypeExceptionWithAccountIdMustHaveClearMessage() {
        givenBalanceType();
        whenCreateNewInvalidBalanceTypeExceptionWithAccountId();
        thenShouldHaveExpectedMessageWithAccountInfo();
    }

    private void givenBalanceType() {
        invalidBalanceType = BalanceType.FOOD;
    }

    private void whenCreateNewInvalidBalanceTypeException() {
        exception = new InvalidBalanceTypeException(invalidBalanceType);
    }

    private void whenCreateNewInvalidBalanceTypeExceptionWithAccountId() {
        exception = new InvalidBalanceTypeException(invalidBalanceType, 1L);
    }

    private void thenShouldHaveExpectedMessage() {
        String expectedMessage = "Balance type not supported for this operation: FOOD";
        assertEquals(expectedMessage, exception.getMessage());
    }

    private void thenShouldHaveExpectedMessageWithAccountInfo() {
        String expectedMessage = "Balance type not supported: FOOD and the account 1 does not have a CASH balance type";
        assertEquals(expectedMessage, exception.getMessage());
    }

}