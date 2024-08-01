package com.github.jcestaro.authorizer.domain.service.strategy.impl;

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
 * terceira estratégia a ser executada, o Autorizador com Fallback:
 * Para despesas não relacionadas a benefícios existe a categoria CASH.
 * <p>
 * O autorizador com fallback funciona como o autorizador simples, com a seguinte diferença:
 * - Se a MCC não puder ser mapeado para uma categoria de benefícios ou se o saldo da categoria
 * fornecida não for suficiente para pagar a transação inteira, verifica o saldo de CASH e, se for suficiente,
 * debita esse saldo.
 */
@Order(3)
@Service
public class FallbackAuthorizer extends AuthorizerTemplate implements AuthorizerStrategy {

    @Autowired
    public FallbackAuthorizer(AccountRepository accountRepository) {
        super(accountRepository);
    }

    @Override
    public void execute(AuthorizerForm authorizerForm) {
        super.authorize(authorizerForm);
    }

    @Override
    public boolean isSatisfiedBy(AuthorizerForm authorizerForm) {
        return authorizerForm != null;
    }

    @Override
    protected Merchant getMerchant(AuthorizerForm authorizerForm) {
        return MerchantCategory.getByCode(authorizerForm.mcc())
                .map(merchantCategory -> new Merchant(authorizerForm.merchant(), merchantCategory))
                .orElse(new Merchant(authorizerForm.merchant()));
    }

    @Override
    protected Runnable addTransactionToAccount(Account account, TransactionPayload transactionPayload) {
        return () -> account.addFallbackTransaction(transactionPayload);
    }

}
