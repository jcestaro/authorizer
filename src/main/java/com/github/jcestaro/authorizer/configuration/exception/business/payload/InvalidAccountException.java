package com.github.jcestaro.authorizer.configuration.exception.business.payload;

import com.github.jcestaro.authorizer.configuration.exception.business.BusinessException;

public class InvalidAccountException extends BusinessException {

    public InvalidAccountException() {
        super("The account must be informed");
    }

}
