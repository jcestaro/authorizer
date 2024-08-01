package com.github.jcestaro.authorizer.configuration.exception.business.payload;

import com.github.jcestaro.authorizer.configuration.exception.business.BusinessException;

public class InvalidAmountException extends BusinessException {

    public InvalidAmountException() {
        super("The amount of the transaction must be informed");
    }

}
