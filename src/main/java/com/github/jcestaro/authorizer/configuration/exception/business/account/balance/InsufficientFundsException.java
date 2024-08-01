package com.github.jcestaro.authorizer.configuration.exception.business.account.balance;

import com.github.jcestaro.authorizer.configuration.exception.business.BusinessException;
import com.github.jcestaro.authorizer.domain.model.account.balance.AccountBalance;

import java.math.BigDecimal;
import java.text.MessageFormat;

public class InsufficientFundsException extends BusinessException {

    private static final String INSUFFICIENT_FUNDS_MSG = "Insufficient funds for the account: {0}, balance type: {1}, total balance: {2}, amount to subtract: {3}";

    public InsufficientFundsException(AccountBalance accountBalance, BigDecimal amount) {
        super(MessageFormat.format(INSUFFICIENT_FUNDS_MSG,
                accountBalance.getAccountId(),
                accountBalance.getBalanceType(),
                accountBalance.getBalance(),
                amount));
    }

}
