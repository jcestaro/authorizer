package com.github.jcestaro.authorizer.configuration.exception.business.authorizer;

import com.github.jcestaro.authorizer.configuration.exception.business.BusinessException;

import java.text.MessageFormat;

public class ConnectionTimeoutException extends BusinessException {

    public ConnectionTimeoutException(Throwable e) {
        super(MessageFormat.format("Connection timeout: {0}", e.getMessage()), e);
    }

}
