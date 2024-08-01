package com.github.jcestaro.authorizer.domain.facade;

import com.github.jcestaro.authorizer.configuration.exception.business.BusinessException;
import com.github.jcestaro.authorizer.configuration.exception.business.authorizer.ConnectionTimeoutException;
import com.github.jcestaro.authorizer.configuration.exception.business.authorizer.TransactionLockedException;
import com.github.jcestaro.authorizer.domain.facade.validator.AuthorizerFormValidator;
import com.github.jcestaro.authorizer.domain.service.strategy.AuthorizerStrategy;
import com.github.jcestaro.authorizer.domain.web.form.AuthorizerForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * O Facade é responsável por garantir que o formulário esteja válido,
 * garantir que não ocorrerá uma concorrência utilizando um lock pessimista
 * ao autorizar mais de um pagamento simultaneamente com um timeout de no máximo 100ms.
 * <p>
 * também é responsável por encontrar a estratégia adequada para autorizar o pagamento.
 */
@Component
public class AuthorizerFacade {

    private static final int TIMEOUT_CONFIG_MS = 100;
    private static final String STRATEGY_NOT_IMPLEMENTED_YET = "Strategy not implemented yet";

    private final List<AuthorizerStrategy> strategies;

    @Autowired
    public AuthorizerFacade(List<AuthorizerStrategy> strategies) {
        this.strategies = strategies;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void authorize(AuthorizerForm authorizerForm) {
        AuthorizerFormValidator.validate(authorizerForm);

        final Lock pessimistLock = new ReentrantLock();

        try {
            tryLockAndAuthorize(authorizerForm, pessimistLock);
        } catch (InterruptedException e) {
            throw new ConnectionTimeoutException(e);
        } finally {
            pessimistLock.unlock();
        }
    }

    private void tryLockAndAuthorize(AuthorizerForm authorizerForm, Lock pessimistLock) throws InterruptedException {
        if (pessimistLock.tryLock(TIMEOUT_CONFIG_MS, TimeUnit.MILLISECONDS)) {
            findAndExecuteAuthorizeStrategy(authorizerForm);
        } else {
            throw new TransactionLockedException();
        }
    }

    private void findAndExecuteAuthorizeStrategy(AuthorizerForm authorizerForm) {
        strategies.stream()
                .filter(strategy -> strategy.isSatisfiedBy(authorizerForm))
                .findFirst()
                .orElseThrow(() -> new BusinessException(STRATEGY_NOT_IMPLEMENTED_YET))
                .execute(authorizerForm);
    }

}
