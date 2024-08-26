package com.github.jcestaro.authorizer.configuration.advice;

import com.github.jcestaro.authorizer.configuration.exception.business.BusinessException;
import com.github.jcestaro.authorizer.configuration.exception.business.account.balance.InsufficientFundsException;
import com.github.jcestaro.authorizer.configuration.response.AuthorizerResponse;
import com.github.jcestaro.authorizer.configuration.response.ResponseCode;
import com.github.jcestaro.authorizer.domain.model.account.balance.AccountBalance;
import com.github.jcestaro.authorizer.domain.model.account.balance.BalanceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


class RestControllerConfigurationTest {

    private final RestControllerConfiguration advice = new RestControllerConfiguration();

    AccountBalance accountBalance;
    InsufficientFundsException insufficientFundsException;
    ResponseEntity<AuthorizerResponse> response;
    BusinessException businessException;
    private Exception exception;

    @Test
    @DisplayName("Handle unexpected exceptions must retorn code for rejected")
    public void test01() {
        givenUnexpectedException();
        whenHandleTheUnexpectedException();
        thenMustReturnResponseCodeForRejected();
    }

    @Test
    @DisplayName("Handle business exceptions must retorn code for rejected")
    public void test02() {
        givenBusinessException();
        whenHandleTheBusinessException();
        thenMustReturnResponseCodeForRejected();
    }

    @Test
    @DisplayName("Handle insufficient funds exceptions must retorn code for insufficient funds")
    public void test03() {
        givenAccountBalance();
        givenInsufficientFundsException();
        whenHandleTheInsufficientFundsException();
        thenMustReturnResponseCodeForInsufficientFunds();
    }

    private void givenAccountBalance() {
        accountBalance = Mockito.mock(AccountBalance.class);
        when(accountBalance.getBalance()).thenReturn(BigDecimal.valueOf(5));
        when(accountBalance.getAccountId()).thenReturn(1L);
        when(accountBalance.getBalanceType()).thenReturn(BalanceType.FOOD);
    }

    private void givenInsufficientFundsException() {
        insufficientFundsException = new InsufficientFundsException(accountBalance, BigDecimal.TEN);
    }

    private void givenUnexpectedException() {
        exception = new Exception("Unexpected error");
    }

    private void givenBusinessException() {
        businessException = new BusinessException("Business error");
    }

    private void whenHandleTheInsufficientFundsException() {
        response = advice.handleExpected(insufficientFundsException);
    }

    private void whenHandleTheUnexpectedException() {
        response = advice.handleUnexpected(exception);
    }

    private void whenHandleTheBusinessException() {
        response = advice.handleExpected(businessException);
    }

    private void thenMustReturnResponseCodeForInsufficientFunds() {
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseCode.INSUFFICIENT_FUNDS.getCode(), response.getBody().getCode());
    }

    private void thenMustReturnResponseCodeForRejected() {
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseCode.REJECTED.getCode(), response.getBody().getCode());
    }

}