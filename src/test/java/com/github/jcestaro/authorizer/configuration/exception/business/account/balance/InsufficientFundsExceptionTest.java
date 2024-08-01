package com.github.jcestaro.authorizer.configuration.exception.business.account.balance;

import com.github.jcestaro.authorizer.domain.model.account.balance.AccountBalance;
import com.github.jcestaro.authorizer.domain.model.account.balance.BalanceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class InsufficientFundsExceptionTest {

    AccountBalance accountBalance;
    BigDecimal amountToSubtract;
    InsufficientFundsException exception;

    @Test
    @DisplayName("Insufficient funds exception must have clear message")
    public void insufficientFundsExceptionMustHaveClearMessage() {
        givenAccountBalance();
        givenAmountToSubtract();
        whenCreateNewInsufficientFundsException();
        thenShouldHaveExpectedMessage();
    }

    private void givenAccountBalance() {
        accountBalance = Mockito.mock(AccountBalance.class);
        when(accountBalance.getBalance()).thenReturn(BigDecimal.valueOf(100));
        when(accountBalance.getAccountId()).thenReturn(1L);
        when(accountBalance.getBalanceType()).thenReturn(BalanceType.MEAL);
    }

    private void givenAmountToSubtract() {
        amountToSubtract = BigDecimal.valueOf(150);
    }

    private void whenCreateNewInsufficientFundsException() {
        exception = new InsufficientFundsException(accountBalance, amountToSubtract);
    }

    private void thenShouldHaveExpectedMessage() {
        String expectedMessage = "Insufficient funds for the account: 1, balance type: MEAL, total balance: 100, amount to subtract: 150";
        assertEquals(expectedMessage, exception.getMessage());
    }

}