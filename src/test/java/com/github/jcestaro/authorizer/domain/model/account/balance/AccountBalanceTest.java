package com.github.jcestaro.authorizer.domain.model.account.balance;

import com.github.jcestaro.authorizer.configuration.exception.business.account.balance.InsufficientFundsException;
import com.github.jcestaro.authorizer.domain.model.account.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountBalanceTest {

    @Test
    @DisplayName("Given a account balance, when subtract balance with enough value then balance must be subtracted")
    void test01() {
        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setBalance(BigDecimal.valueOf(50));

        accountBalance.subtractBalance(BigDecimal.valueOf(5));

        assertEquals(BigDecimal.valueOf(45), accountBalance.getBalance());
    }

    @Test
    @DisplayName("Given a account balance, when subtract balance with too much value then should throw insufficient funds exception")
    void test02() {
        Account account = Mockito.mock(Account.class);
        BigDecimal balance = BigDecimal.valueOf(50);

        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setAccount(account);
        accountBalance.setBalance(balance);
        accountBalance.setBalanceType(BalanceType.CASH);

        assertThrows(InsufficientFundsException.class, () -> accountBalance.subtractBalance(BigDecimal.valueOf(51)));
        assertEquals(balance, accountBalance.getBalance());
    }

    @Test
    @DisplayName("Given a account balance, when check if has enough balance with greater value, should return false")
    void test03() {
        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setBalance(BigDecimal.valueOf(50));

        boolean hasEnoughBalance = accountBalance.hasEnoughBalance(BigDecimal.valueOf(51));

        assertFalse(hasEnoughBalance);
    }

    @Test
    @DisplayName("Given a account balance, when check if has enough balance with lower value, should return true")
    void test04() {
        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setBalance(BigDecimal.valueOf(50));

        boolean hasEnoughBalance = accountBalance.hasEnoughBalance(BigDecimal.valueOf(49));

        assertTrue(hasEnoughBalance);
    }

    @Test
    @DisplayName("Given a account balance, when check if has enough balance with equal value, should return true")
    void test05() {
        BigDecimal balance = BigDecimal.valueOf(50);
        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setBalance(balance);

        boolean hasEnoughBalance = accountBalance.hasEnoughBalance(balance);

        assertTrue(hasEnoughBalance);
    }

}