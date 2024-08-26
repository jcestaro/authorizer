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
 * Segunda estratégia a ser executada, o Autorizador simples:
 * - Recebe a transação
 * - Usa apenas a MCC para mapear a transação para uma categoria de benefícios (MerchantCategory)
 * - Aprova ou rejeita a transação
 * - Caso a transação seja aprovada, o saldo da categoria mapeada é diminuido do saldo de Account.
 */
@Order(2)
@Service
public class SimpleAuthorizer extends AuthorizerTemplate implements AuthorizerStrategy {

    @Autowired
    public SimpleAuthorizer(AccountRepository accountRepository) {
        super(accountRepository);
    }

    @Override
    public void execute(AuthorizerForm authorizerForm) {
        super.authorize(authorizerForm);
    }

    @Override
    public boolean isSatisfiedBy(AuthorizerForm authorizerForm) {
        return authorizerForm != null && MerchantCategory.getByCode(authorizerForm.mcc()).isPresent();
    }

    @Override
    protected Merchant getMerchant(AuthorizerForm authorizerForm) {
        MerchantCategory merchantCategory = getMerchantCategoryByCode(authorizerForm);
        return new Merchant(authorizerForm.merchant(), merchantCategory);
    }

    @Override
    protected Runnable addTransactionToAccount(Account account, TransactionPayload transactionPayload) {
        return () -> account.addSimpleTransaction(transactionPayload);
    }

    private MerchantCategory getMerchantCategoryByCode(AuthorizerForm authorizerForm) {
        return MerchantCategory.getByCode(authorizerForm.mcc())
                .orElseThrow(MerchantCategoryNotFoundException::new);
    }

}
