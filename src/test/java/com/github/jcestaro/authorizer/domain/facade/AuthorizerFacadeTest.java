package com.github.jcestaro.authorizer.domain.facade;

import com.github.jcestaro.authorizer.configuration.exception.business.BusinessException;
import com.github.jcestaro.authorizer.domain.service.strategy.AuthorizerStrategy;
import com.github.jcestaro.authorizer.domain.service.strategy.impl.FallbackAuthorizer;
import com.github.jcestaro.authorizer.domain.service.strategy.impl.SimpleAuthorizer;
import com.github.jcestaro.authorizer.domain.web.form.AuthorizerForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizerFacadeTest {

    private AuthorizerFacade authorizerFacade;

    @Mock
    private SimpleAuthorizer simpleAuthorizer;

    @Mock
    private FallbackAuthorizer fallbackAuthorizer;

    @BeforeEach
    void setUp() {
        List<AuthorizerStrategy> strategies = Mockito.spy(List.of(simpleAuthorizer, fallbackAuthorizer));
        authorizerFacade = new AuthorizerFacade(strategies);
    }

    @Test
    @DisplayName("Given valid authorizer form, when authorize then validator and simple authorizer should be called")
    public void test01() {
        AuthorizerForm form = new AuthorizerForm("123", BigDecimal.TEN, "5411", "validMerchant");
        when(simpleAuthorizer.isSatisfiedBy(form)).thenReturn(true);
        authorizerFacade.authorize(form);
        verify(simpleAuthorizer).execute(form);
    }

    @Test
    @DisplayName("Given invalid authorizer form, when authorize then validator throws exception")
    public void test02() {
        AuthorizerForm form = new AuthorizerForm("", BigDecimal.ZERO, "", "");
        assertThrows(BusinessException.class, () -> authorizerFacade.authorize(form));
        verify(simpleAuthorizer, never()).execute(any());
    }

    @Test
    @DisplayName("Given invalid authorizer form which cant find strategies, when authorize then throws exception")
    public void test03() {
        AuthorizerForm form = new AuthorizerForm("123", BigDecimal.TEN, "5411", "validMerchant");
        assertThrows(BusinessException.class, () -> authorizerFacade.authorize(form));
        verify(simpleAuthorizer, never()).execute(any());
    }

    @Test
    @DisplayName("Given valid authorizer form, when authorize then validator and fallback authorizer should be called")
    public void test04() {
        AuthorizerForm form = new AuthorizerForm("123", BigDecimal.TEN, "5411", "validMerchant");
        when(fallbackAuthorizer.isSatisfiedBy(form)).thenReturn(true);
        authorizerFacade.authorize(form);
        verify(simpleAuthorizer, never()).execute(form);
        verify(fallbackAuthorizer).execute(form);
    }

    @Test
    @DisplayName("Given valid authorizer form, when authorize then validator and fallback authorizer should be called")
    public void test05() {
        AuthorizerForm form = new AuthorizerForm("123", BigDecimal.TEN, "5411", "validMerchant");
        when(fallbackAuthorizer.isSatisfiedBy(form)).thenReturn(true);
        authorizerFacade.authorize(form);
        verify(simpleAuthorizer, never()).execute(form);
        verify(fallbackAuthorizer).execute(form);
    }

}