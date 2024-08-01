package com.github.jcestaro.authorizer.domain.facade.validator;

import com.github.jcestaro.authorizer.configuration.exception.business.payload.InvalidAccountException;
import com.github.jcestaro.authorizer.configuration.exception.business.payload.InvalidAmountException;
import com.github.jcestaro.authorizer.configuration.exception.business.payload.InvalidMerchantException;
import com.github.jcestaro.authorizer.domain.web.form.AuthorizerForm;
import com.github.jcestaro.authorizer.util.NumberUtil;
import com.github.jcestaro.authorizer.util.StringUtil;

/**
 * Optei por utilizar um validador básico no java mesmo e chama-lo no facade
 * por conta de validar apenas os dados da request, como a intenção era devolver um código
 * padrão caso falhar a requisição, acredito que seja melhor do que usar um bean validation nessa situação,
 * apesar do bean validation ser bem mais completo e fácil de usar.
 * */
public class AuthorizerFormValidator {

    public static void validate(AuthorizerForm authorizerForm) {
        validateAccount(authorizerForm);
        validateTotalAmount(authorizerForm);
        validateMerchant(authorizerForm);
    }

    private static void validateAccount(AuthorizerForm authorizerForm) {
        if (StringUtil.isEmpty(authorizerForm.account())) {
            throw new InvalidAccountException();
        }
    }

    private static void validateTotalAmount(AuthorizerForm authorizerForm) {
        if (NumberUtil.isNullOrZero(authorizerForm.totalAmount()) || NumberUtil.isNegative(authorizerForm.totalAmount())) {
            throw new InvalidAmountException();
        }
    }

    private static void validateMerchant(AuthorizerForm authorizerForm) {
        if (StringUtil.isEmpty(authorizerForm.merchant())) {
            throw new InvalidMerchantException();
        }
    }

}
