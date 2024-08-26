package com.github.jcestaro.authorizer.domain.service.strategy.impl.template;

import com.github.jcestaro.authorizer.configuration.exception.business.account.AccountNotFoundException;
import com.github.jcestaro.authorizer.domain.model.account.Account;
import com.github.jcestaro.authorizer.domain.model.transaction.TransactionPayload;
import com.github.jcestaro.authorizer.domain.model.transaction.merchant.Merchant;
import com.github.jcestaro.authorizer.domain.repository.AccountRepository;
import com.github.jcestaro.authorizer.domain.web.form.AuthorizerForm;
import com.github.jcestaro.authorizer.util.NumberUtil;

/**
 * Template method pattern utilizada aqui para abstrair o comportamento padrão dos autorizadores
 * e reutilizar as dependencias em comum.
 */
public abstract class AuthorizerTemplate {

    private final AccountRepository accountRepository;

    protected AuthorizerTemplate(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    protected abstract Merchant getMerchant(AuthorizerForm authorizerForm);

    protected abstract Runnable addTransactionToAccount(Account account, TransactionPayload transactionPayload);

    protected void authorize(AuthorizerForm authorizerForm) {
        Account account = findAccountById(authorizerForm);

        TransactionPayload transactionPayload = buildNewTransaction(authorizerForm, account);
        addTransactionToAccount(account, transactionPayload).run();

        accountRepository.save(account);
    }

    private Account findAccountById(AuthorizerForm authorizerForm) {
        return accountRepository.findById(NumberUtil.parseLong(authorizerForm.account()))
                .orElseThrow(AccountNotFoundException::new);
    }

    private TransactionPayload buildNewTransaction(AuthorizerForm authorizerForm, Account account) {
        return new TransactionPayload.Builder()
                .account(account)
                .amount(authorizerForm.totalAmount())
                .merchant(getMerchant(authorizerForm))
                .build();
    }

}
