package com.github.jcestaro.authorizer.domain.service.strategy.impl;

import com.github.jcestaro.authorizer.configuration.exception.business.BusinessException;
import com.github.jcestaro.authorizer.configuration.exception.business.account.AccountNotFoundException;
import com.github.jcestaro.authorizer.domain.model.account.Account;
import com.github.jcestaro.authorizer.domain.model.account.balance.AccountBalance;
import com.github.jcestaro.authorizer.domain.model.account.balance.BalanceType;
import com.github.jcestaro.authorizer.domain.repository.AccountRepository;
import com.github.jcestaro.authorizer.domain.web.form.AuthorizerForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FallbackAuthorizerTest {

    @InjectMocks
    FallbackAuthorizer fallbackAuthorizer;

    @Mock
    AccountRepository accountRepository;

    AuthorizerForm authorizerForm;
    Account account;
    BusinessException exception;
    boolean satisfiedBy;

    @Test
    @DisplayName("Execute the fallback authorizer strategy must throw a exception if cant find the account")
    void test01() {
        givenAuthorizerFormWithFoodMccWithoutAccount();
        whenExecuteFallbackAuthorizerShouldThrowExceptionForAccountNotFound();
    }

    @Test
    @DisplayName("Execute the fallback authorizer strategy must add the transaction to the account and save it")
    void test02() {
        givenValidAuthorizerForm();
        givenAccountWithBalances();
        whenExecuteFallbackAuthorizer();
        thenShouldAddTheNewTransaction();
        thenShouldSaveTheAccount();
    }

    @Test
    @DisplayName("isSatisfiedBy of the fallback authorizer strategy must return false when given null form")
    void test03() {
        whenCheckingIfIsSatisfiedBy();
        thenSatisfiedByShouldBeFalse();
    }

    @Test
    @DisplayName("isSatisfiedBy of the fallback authorizer strategy must return true when given a form")
    void test04() {
        givenValidAuthorizerForm();
        whenCheckingIfIsSatisfiedBy();
        thenSatisfiedByShouldBeTrue();
    }

    private void givenValidAuthorizerForm() {
        authorizerForm = new AuthorizerForm("123", BigDecimal.TEN, "5411", "validMerchant");
    }

    private void givenAuthorizerFormWithFoodMccWithoutAccount() {
        authorizerForm = new AuthorizerForm("", BigDecimal.TEN, "5411", "");
    }

    private void givenAccountWithBalances() {
        account = Mockito.spy(Account.class);
        AccountBalance foodAccountBalance = Mockito.spy(AccountBalance.class);
        foodAccountBalance.setBalance(BigDecimal.valueOf(100));
        foodAccountBalance.setBalanceType(BalanceType.FOOD);

        AccountBalance cashAccountBalance = Mockito.spy(AccountBalance.class);
        cashAccountBalance.setBalance(BigDecimal.valueOf(150));
        foodAccountBalance.setBalanceType(BalanceType.CASH);

        when(account.getAccountBalances()).thenReturn(List.of(foodAccountBalance, cashAccountBalance));
        when(accountRepository.findById(123L)).thenReturn(Optional.of(account));
    }

    private void whenCheckingIfIsSatisfiedBy() {
        satisfiedBy = fallbackAuthorizer.isSatisfiedBy(authorizerForm);
    }

    private void thenSatisfiedByShouldBeFalse() {
        assertFalse(satisfiedBy);
    }

    private void thenSatisfiedByShouldBeTrue() {
        assertTrue(satisfiedBy);
    }

    private void whenExecuteFallbackAuthorizerShouldThrowExceptionForAccountNotFound() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());
        exception = assertThrows(AccountNotFoundException.class, () -> fallbackAuthorizer.execute(authorizerForm));
        assertEquals("Account not found", exception.getMessage());
    }

    private void thenShouldAddTheNewTransaction() {
        assertEquals(1, account.getTransactions().size());
    }

    private void thenShouldSaveTheAccount() {
        verify(accountRepository).save(account);
    }

    private void whenExecuteFallbackAuthorizer() {
        fallbackAuthorizer.execute(authorizerForm);
    }

}