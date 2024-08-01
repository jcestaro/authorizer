package com.github.jcestaro.authorizer.configuration.exception.business.account.balance;

import com.github.jcestaro.authorizer.configuration.exception.business.BusinessException;
import com.github.jcestaro.authorizer.domain.model.account.balance.BalanceType;

import java.text.MessageFormat;

public class InvalidBalanceTypeException extends BusinessException {

    public InvalidBalanceTypeException(BalanceType balanceType) {
        super(MessageFormat.format("Balance type not supported for this operation: {0}", balanceType));
    }

    public InvalidBalanceTypeException(BalanceType balanceType, Long accountId) {
        super(MessageFormat.format(
                "Balance type not supported: {0} and the account {1} does not have a CASH balance type",
                balanceType,
                accountId
        ));
    }

}
