package com.github.jcestaro.authorizer.configuration.exception.business.payload;

import com.github.jcestaro.authorizer.configuration.exception.business.BusinessException;

public class InvalidMerchantException extends BusinessException {

    public InvalidMerchantException() {
        super("The merchant must be informed");
    }

}
