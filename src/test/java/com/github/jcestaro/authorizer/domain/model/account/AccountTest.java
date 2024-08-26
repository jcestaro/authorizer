package com.github.jcestaro.authorizer.domain.model.account;

import com.github.jcestaro.authorizer.configuration.exception.business.account.balance.InvalidBalanceTypeException;
import com.github.jcestaro.authorizer.domain.model.account.balance.AccountBalance;
import com.github.jcestaro.authorizer.domain.model.account.balance.BalanceType;
import com.github.jcestaro.authorizer.domain.model.transaction.TransactionPayload;
import com.github.jcestaro.authorizer.domain.model.transaction.merchant.Merchant;
import com.github.jcestaro.authorizer.domain.model.transaction.merchant.MerchantCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AccountTest {

    AccountBalance accountBalance;
    Account account;
    TransactionPayload transactionPayload;
    InvalidBalanceTypeException invalidBalanceTypeException;
    List<AccountBalance> balances = new ArrayList<>();

    @Test
    @DisplayName("Adding a transaction for simple authorization should add the transaction and calculate the new balance of the account")
    void test01() {
        givenFoodAccountBalance();
        givenAccountWithBalance();
        givenTransactionPayloadWithSmallAmount();
        whenAddSimpleTransactionToTheAccount();
        thenTheTransactionMustBeAdded();
        thenTheAccountBalanceMustBeReducedByTheTransactionAmount();
    }

    @Test
    @DisplayName("Adding a transaction for simple authorization without a existing mcc should throw a exception")
    void test02() {
        givenFoodAccountBalance();
        givenAccountWithBalance();
        givenTransactionPayloadWithoutMerchantCategory();
        whenAddSimpleTransactionThenShouldThrowInvalidBalanceTypeException();
        thenTheExceptionMustHaveClearMessage();
    }

    @Test
    @DisplayName("Adding a transaction for fallback authorization with enough balance in MEAL should add the transaction and calculate the new balance of the account")
    void test03() {
        givenManyAccountBalances();
        givenAccountWithManyBalances();
        givenTransactionPayloadWithSmallAmount();
        whenAddFallbackTransactionToTheAccount();
        thenTheTransactionMustBeAdded();
        thenTheFoodAccountBalanceMustBeReducedByTheTransactionAmount();
    }

    @Test
    @DisplayName("Adding a transaction for fallback authorization with only enough balance in CASH should add the transaction and calculate the new balance of the account")
    void test04() {
        givenManyAccountBalances();
        givenAccountWithManyBalances();
        givenTransactionPayloadWithLargeAmount();
        whenAddFallbackTransactionToTheAccount();
        thenTheTransactionMustBeAdded();
        thenTheCashAccountBalanceMustBeReducedByTheTransactionAmount();
    }

    @Test
    @DisplayName("Adding a transaction for fallback authorization without a existing mcc should throw a exception")
    void test05() {
        givenFoodAccountBalance();
        givenAccountWithBalance();
        givenTransactionPayloadWithoutMerchantCategory();
        whenAddFallbackTransactionThenShouldThrowInvalidBalanceTypeException();
        thenTheExceptionMustHaveClearMessageAndAccountId();
    }

    private void givenTransactionPayloadWithSmallAmount() {
        transactionPayload = new TransactionPayload.Builder()
                .merchant(new Merchant("merchant", MerchantCategory.FOOD))
                .amount(BigDecimal.TEN)
                .build();
    }

    private void givenTransactionPayloadWithLargeAmount() {
        transactionPayload = new TransactionPayload.Builder()
                .merchant(new Merchant("merchant", MerchantCategory.FOOD))
                .amount(BigDecimal.valueOf(110))
                .build();
    }

    private void givenTransactionPayloadWithoutMerchantCategory() {
        transactionPayload = new TransactionPayload.Builder()
                .merchant(new Merchant("merchant", null))
                .amount(BigDecimal.TEN)
                .build();
    }

    private void givenAccountWithBalance() {
        account = Mockito.spy(Account.class);
        account.setId(1L);
        when(account.getAccountBalances()).thenReturn(List.of(accountBalance));
    }

    private void givenAccountWithManyBalances() {
        account = Mockito.spy(Account.class);
        when(account.getAccountBalances()).thenReturn(balances);
    }

    private void givenFoodAccountBalance() {
        accountBalance = Mockito.spy(AccountBalance.class);
        accountBalance.setBalanceType(BalanceType.FOOD);
        accountBalance.setBalance(BigDecimal.valueOf(100));
    }

    private void givenManyAccountBalances() {
        AccountBalance foodAccountBalance = Mockito.spy(AccountBalance.class);
        foodAccountBalance.setBalanceType(BalanceType.FOOD);
        foodAccountBalance.setBalance(BigDecimal.valueOf(100));
        balances.add(foodAccountBalance);

        AccountBalance cashAccountBalance = Mockito.spy(AccountBalance.class);
        cashAccountBalance.setBalanceType(BalanceType.CASH);
        cashAccountBalance.setBalance(BigDecimal.valueOf(150));
        balances.add(cashAccountBalance);
    }

    private void whenAddSimpleTransactionToTheAccount() {
        account.addSimpleTransaction(transactionPayload);
    }

    private void whenAddFallbackTransactionToTheAccount() {
        account.addFallbackTransaction(transactionPayload);
    }

    private void whenAddSimpleTransactionThenShouldThrowInvalidBalanceTypeException() {
        invalidBalanceTypeException = assertThrows(InvalidBalanceTypeException.class, () -> account.addSimpleTransaction(transactionPayload));
    }

    private void whenAddFallbackTransactionThenShouldThrowInvalidBalanceTypeException() {
        invalidBalanceTypeException = assertThrows(InvalidBalanceTypeException.class, () -> account.addFallbackTransaction(transactionPayload));
    }

    private void thenTheAccountBalanceMustBeReducedByTheTransactionAmount() {
        assertEquals(BigDecimal.valueOf(90), accountBalance.getBalance());
    }

    private void thenTheFoodAccountBalanceMustBeReducedByTheTransactionAmount() {
        AccountBalance foodAccountBalance = balances.get(0);
        assertEquals(BigDecimal.valueOf(90), foodAccountBalance.getBalance());

        AccountBalance cashAccountBalance = balances.get(1);
        assertEquals(BigDecimal.valueOf(150), cashAccountBalance.getBalance());
    }

    private void thenTheCashAccountBalanceMustBeReducedByTheTransactionAmount() {
        AccountBalance foodAccountBalance = balances.get(0);
        assertEquals(BigDecimal.valueOf(100), foodAccountBalance.getBalance());

        AccountBalance cashAccountBalance = balances.get(1);
        assertEquals(BigDecimal.valueOf(40), cashAccountBalance.getBalance());
    }

    private void thenTheTransactionMustBeAdded() {
        assertEquals(1, account.getTransactions().size());
        assertTrue(account.getTransactions().contains(transactionPayload));
    }

    private void thenTheExceptionMustHaveClearMessage() {
        assertEquals("Balance type not supported for this operation: NONE", invalidBalanceTypeException.getMessage());
    }

    private void thenTheExceptionMustHaveClearMessageAndAccountId() {
        assertEquals("Balance type not supported: NONE and the account 1 does not have a CASH balance type", invalidBalanceTypeException.getMessage());
    }

}