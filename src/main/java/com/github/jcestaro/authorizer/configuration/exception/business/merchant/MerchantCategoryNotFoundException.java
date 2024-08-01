package com.github.jcestaro.authorizer.configuration.exception.business.merchant;

import com.github.jcestaro.authorizer.configuration.exception.business.BusinessException;

public class MerchantCategoryNotFoundException extends BusinessException {

    public MerchantCategoryNotFoundException() {
        super("Merchant category not found");
    }

}
