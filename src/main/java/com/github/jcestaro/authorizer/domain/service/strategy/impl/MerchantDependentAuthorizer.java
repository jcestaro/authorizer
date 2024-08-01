package com.github.jcestaro.authorizer.domain.service.strategy.impl;

import com.github.jcestaro.authorizer.configuration.exception.business.merchant.MerchantCategoryNotFoundException;
import com.github.jcestaro.authorizer.domain.model.account.Account;
import com.github.jcestaro.authorizer.domain.model.transaction.TransactionPayload;
import com.github.jcestaro.authorizer.domain.model.transaction.merchant.Merchant;
import com.github.jcestaro.authorizer.domain.model.transaction.merchant.MerchantCategory;
import com.github.jcestaro.authorizer.domain.repository.AccountRepository;
import com.github.jcestaro.authorizer.domain.service.strategy.AuthorizerStrategy;
import com.github.jcestaro.authorizer.domain.service.strategy.impl.template.AuthorizerTemplate;
import com.github.jcestaro.authorizer.domain.web.form.AuthorizerForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * primeira estratégia a ser executada, o Autorizador dependente do comerciante:
 * As vezes, os MCCs estão incorretos e a transação deve ser processada levando em
 * consideração o nome do comerciante e o mesmo tem maior precedência sobre as MCCs,
 * ao encontrar por nome, executa a mesma operação do autorizador simples.
 */
@Order(1)
@Service
public class MerchantDependentAuthorizer extends AuthorizerTemplate implements AuthorizerStrategy {

    @Autowired
    public MerchantDependentAuthorizer(AccountRepository accountRepository) {
        super(accountRepository);
    }

    @Override
    public void execute(AuthorizerForm authorizerForm) {
        super.authorize(authorizerForm);
    }

    @Override
    public boolean isSatisfiedBy(AuthorizerForm authorizerForm) {
        return authorizerForm != null && MerchantCategory.getByName(authorizerForm.merchant()).isPresent();
    }

    @Override
    protected Runnable addTransactionToAccount(Account account, TransactionPayload transactionPayload) {
        return () -> account.addSimpleTransaction(transactionPayload);
    }

    @Override
    protected Merchant getMerchant(AuthorizerForm authorizerForm) {
        MerchantCategory merchantCategory = getMerchantCategoryByName(authorizerForm);
        return new Merchant(authorizerForm.merchant(), merchantCategory);
    }

    private MerchantCategory getMerchantCategoryByName(AuthorizerForm authorizerForm) {
        return MerchantCategory.getByName(authorizerForm.merchant())
                .orElseThrow(MerchantCategoryNotFoundException::new);
    }

}
