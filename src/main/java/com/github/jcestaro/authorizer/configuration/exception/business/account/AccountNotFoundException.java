package com.github.jcestaro.authorizer.configuration.exception.business.account;

import com.github.jcestaro.authorizer.configuration.exception.business.BusinessException;

public class AccountNotFoundException extends BusinessException {

    public AccountNotFoundException() {
        super("Account not found");
    }
}
