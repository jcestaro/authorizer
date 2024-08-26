package com.github.jcestaro.authorizer.domain.facade.validator;

import com.github.jcestaro.authorizer.configuration.exception.business.payload.InvalidAccountException;
import com.github.jcestaro.authorizer.configuration.exception.business.payload.InvalidAmountException;
import com.github.jcestaro.authorizer.configuration.exception.business.payload.InvalidMerchantException;
import com.github.jcestaro.authorizer.domain.web.form.AuthorizerForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthorizerFormValidatorTest {

    @Test
    @DisplayName("Given empty account, when validated then throws invalid account exception")
    public void test01() {
        AuthorizerForm form = new AuthorizerForm("", BigDecimal.ZERO, "", "");
        assertThrows(InvalidAccountException.class, () -> AuthorizerFormValidator.validate(form));
    }

    @Test
    @DisplayName("Given null total amount, when validated then throws invalid amount exception")
    public void test02() {
        AuthorizerForm form = new AuthorizerForm("validAccount", null, "", "");
        assertThrows(InvalidAmountException.class, () -> AuthorizerFormValidator.validate(form));
    }

    @Test
    @DisplayName("Given negative total amount, when validated then throws invalid amount exception")
    public void test03() {
        AuthorizerForm form = new AuthorizerForm("validAccount", BigDecimal.TEN.negate(), "", "");
        assertThrows(InvalidAmountException.class, () -> AuthorizerFormValidator.validate(form));
    }

    @Test
    @DisplayName("Given empty merchant, when validated then throws invalid merchant exception")
    public void test04() {
        AuthorizerForm form = new AuthorizerForm("validAccount", BigDecimal.TEN, "", "");
        assertThrows(InvalidMerchantException.class, () -> AuthorizerFormValidator.validate(form));
    }

    @Test
    @DisplayName("Given valid form, when validated then no exception should be thrown")
    public void test05() {
        AuthorizerForm form = new AuthorizerForm("validAccount", BigDecimal.TEN, "", "validMerchant");
        assertDoesNotThrow(() -> AuthorizerFormValidator.validate(form));
    }

}