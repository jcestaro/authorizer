package com.github.jcestaro.authorizer.domain.model.account;

import com.github.jcestaro.authorizer.configuration.exception.business.account.balance.InvalidBalanceTypeException;
import com.github.jcestaro.authorizer.domain.model.account.balance.AccountBalance;
import com.github.jcestaro.authorizer.domain.model.account.balance.BalanceType;
import com.github.jcestaro.authorizer.domain.model.transaction.TransactionPayload;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Entidade que representa a conta de um usuário
 * */
@Entity
@Table(name = "account")
public class Account {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "account_seq", sequenceName = "account_seq")
    private Long id;

    @Column(name = "active")
    private boolean active;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<TransactionPayload> transactions = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<AccountBalance> accountBalances = new ArrayList<>();

    protected Account() {
    }

    public void addSimpleTransaction(TransactionPayload transactionPayload) {
        transactions.add(transactionPayload);
        calculateSimpleNewBalance(transactionPayload);
    }

    public void addFallbackTransaction(TransactionPayload transactionPayload) {
        transactions.add(transactionPayload);
        calculateFallbackNewBalance(transactionPayload);
    }

    public List<AccountBalance> getAccountBalances() {
        return accountBalances;
    }

    public List<TransactionPayload> getTransactions() {
        return transactions;
    }

    public Long getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private void calculateSimpleNewBalance(TransactionPayload transactionPayload) {
        BalanceType balanceType = transactionPayload.getMerchantBalanceType();
        AccountBalance accountBalance = simpleMapToAccountBalance().apply(balanceType);
        accountBalance.subtractBalance(transactionPayload.getAmount());
    }

    private void calculateFallbackNewBalance(TransactionPayload transactionPayload) {
        BalanceType balanceType = transactionPayload.getMerchantBalanceType();
        AccountBalance accountBalance = fallbackMapToAccountBalance().apply(balanceType, transactionPayload);
        accountBalance.subtractBalance(transactionPayload.getAmount());
    }

    private Function<BalanceType, AccountBalance> simpleMapToAccountBalance() {
        return balanceType -> getAccountBalanceByType(balanceType)
                .orElseThrow(() -> new InvalidBalanceTypeException(balanceType));
    }

    private BiFunction<BalanceType, TransactionPayload, AccountBalance> fallbackMapToAccountBalance() {
        return (balanceType, transactionPayload) -> getAccountBalanceByType(balanceType)
                .filter(accBalance -> accBalance.hasEnoughBalance(transactionPayload.getAmount()))
                .orElse(getCashAccountBalance(balanceType));
    }

    private Optional<AccountBalance> getAccountBalanceByType(BalanceType balanceType) {
        return getAccountBalances().stream()
                .filter(balance -> balance.getBalanceType() == balanceType)
                .findFirst();
    }

    private AccountBalance getCashAccountBalance(BalanceType balanceType) {
        return getAccountBalances().stream()
                .filter(AccountBalance::isCash)
                .findFirst()
                .orElseThrow(() -> new InvalidBalanceTypeException(balanceType, getId()));
    }

}
