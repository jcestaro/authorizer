package com.github.jcestaro.authorizer.configuration.exception.business.authorizer;

import com.github.jcestaro.authorizer.configuration.exception.business.BusinessException;

public class TransactionLockedException extends BusinessException {

    public TransactionLockedException() {
        super("There is another transaction running, please wait a few moments and try again");
    }

}
