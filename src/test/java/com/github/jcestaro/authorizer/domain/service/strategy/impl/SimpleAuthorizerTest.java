package com.github.jcestaro.authorizer.domain.service.strategy.impl;

import com.github.jcestaro.authorizer.configuration.exception.business.BusinessException;
import com.github.jcestaro.authorizer.configuration.exception.business.account.AccountNotFoundException;
import com.github.jcestaro.authorizer.configuration.exception.business.merchant.MerchantCategoryNotFoundException;
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
class SimpleAuthorizerTest {

    @InjectMocks
    SimpleAuthorizer simpleAuthorizer;

    @Mock
    AccountRepository accountRepository;

    AuthorizerForm authorizerForm;
    Account account;
    BusinessException exception;
    boolean satisfiedBy;

    @Test
    @DisplayName("Execute the simple authorizer strategy must throw a exception if cant find a merchant category by code")
    void test01() {
        givenAuthorizerFormWithoutMcc();
        whenExecuteSimpleAuthorizerShouldThrowExceptionForMerchantCategoryNotFound();
    }

    @Test
    @DisplayName("Execute the simple authorizer strategy must throw a exception if cant find the account")
    void test02() {
        givenAuthorizerFormWithFoodMccWithoutAccount();
        whenExecuteSimpleAuthorizerShouldThrowExceptionForAccountNotFound();
    }

    @Test
    @DisplayName("Execute the simple authorizer strategy must add the transaction to the account and save it")
    void test03() {
        givenValidAuthorizerFormWithFoodMcc();
        givenAccountWithBalances();
        whenExecuteSimpleAuthorizer();
        thenShouldAddTheNewTransaction();
        thenShouldSaveTheAccount();
    }

    @Test
    @DisplayName("isSatisfiedBy of the simple authorizer strategy must return false when given null form")
    void test04() {
        whenCheckingIfIsSatisfiedBy();
        thenSatisfiedByShouldBeFalse();
    }

    @Test
    @DisplayName("isSatisfiedBy of the simple authorizer strategy must return false when given a valid form with no existing mcc")
    void test05() {
        givenAuthorizerFormWithoutMcc();
        whenCheckingIfIsSatisfiedBy();
        thenSatisfiedByShouldBeFalse();
    }

    @Test
    @DisplayName("isSatisfiedBy of the simple authorizer strategy must return true when given a valid form with existing mcc")
    void test06() {
        givenValidAuthorizerFormWithFoodMcc();
        whenCheckingIfIsSatisfiedBy();
        thenSatisfiedByShouldBeTrue();
    }

    private void givenAuthorizerFormWithoutMcc() {
        authorizerForm = new AuthorizerForm("", BigDecimal.TEN, "", "");
    }

    private void givenValidAuthorizerFormWithFoodMcc() {
        authorizerForm = new AuthorizerForm("123", BigDecimal.TEN, "5411", "validMerchant");
    }

    private void givenAuthorizerFormWithFoodMccWithoutAccount() {
        authorizerForm = new AuthorizerForm("", BigDecimal.TEN, "5411", "");
    }

    private void givenAccountWithBalances() {
        account = Mockito.spy(Account.class);
        AccountBalance accountBalance = Mockito.spy(AccountBalance.class);
        accountBalance.setBalance(BigDecimal.valueOf(100));

        when(account.getAccountBalances()).thenReturn(List.of(accountBalance));
        when(accountBalance.getBalanceType()).thenReturn(BalanceType.FOOD);
        when(accountRepository.findById(123L)).thenReturn(Optional.of(account));
    }

    private void whenCheckingIfIsSatisfiedBy() {
        satisfiedBy = simpleAuthorizer.isSatisfiedBy(authorizerForm);
    }

    private void thenSatisfiedByShouldBeFalse() {
        assertFalse(satisfiedBy);
    }

    private void thenSatisfiedByShouldBeTrue() {
        assertTrue(satisfiedBy);
    }

    private void whenExecuteSimpleAuthorizerShouldThrowExceptionForMerchantCategoryNotFound() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(Mockito.mock(Account.class)));
        exception = assertThrows(MerchantCategoryNotFoundException.class, () -> simpleAuthorizer.execute(authorizerForm));
        assertEquals("Merchant category not found", exception.getMessage());
    }

    private void whenExecuteSimpleAuthorizerShouldThrowExceptionForAccountNotFound() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());
        exception = assertThrows(AccountNotFoundException.class, () -> simpleAuthorizer.execute(authorizerForm));
        assertEquals("Account not found", exception.getMessage());
    }

    private void thenShouldAddTheNewTransaction() {
        assertEquals(1, account.getTransactions().size());
    }

    private void thenShouldSaveTheAccount() {
        verify(accountRepository).save(account);
    }

    private void whenExecuteSimpleAuthorizer() {
        simpleAuthorizer.execute(authorizerForm);
    }

}