package com.github.jcestaro.authorizer.domain.service.strategy;

import com.github.jcestaro.authorizer.domain.web.form.AuthorizerForm;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Strategy utilizada para encontrar a melhor opção para autorizar os pagamentos.
 * Poderia ter feito a strategy junto com o template method? sim poderia, mas optei por deixar
 * separado os patterns diferentes pois são utilizados em contextos diferentes
 */
public interface AuthorizerStrategy {

    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Exception.class)
    void execute(AuthorizerForm authorizerForm);

    boolean isSatisfiedBy(AuthorizerForm authorizerForm);

}
