package com.github.jcestaro.authorizer.domain.web.form;

import java.math.BigDecimal;

public record AuthorizerForm(
        String account,
        BigDecimal totalAmount,
        String mcc,
        String merchant
) {

}
