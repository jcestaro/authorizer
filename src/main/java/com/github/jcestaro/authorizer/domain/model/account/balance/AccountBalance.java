package com.github.jcestaro.authorizer.domain.model.account.balance;

import com.github.jcestaro.authorizer.configuration.exception.business.account.balance.InsufficientFundsException;
import com.github.jcestaro.authorizer.domain.model.account.Account;
import com.github.jcestaro.authorizer.util.NumberUtil;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Entidade que representa o saldo da conta, onde podem haver mais de um tipo de saldo diferente
 * */
@Entity
@Table(name = "account_balance")
public class AccountBalance {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "account_balance_seq", sequenceName = "account_balance_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "balance_type")
    private BalanceType balanceType;

    private BigDecimal balance = BigDecimal.ZERO;

    protected AccountBalance() {
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Long getAccountId() {
        return account.getId();
    }

    public BalanceType getBalanceType() {
        return balanceType;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setBalanceType(BalanceType balanceType) {
        this.balanceType = balanceType;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCash() {
        return BalanceType.CASH.equals(this.balanceType);
    }

    public void subtractBalance(BigDecimal amount) {
        validateSubtractOperation(amount);
        balance = balance.subtract(amount);
    }

    private void validateSubtractOperation(BigDecimal amount) {
        if (NumberUtil.isNegative(balance.subtract(amount))) {
            throw new InsufficientFundsException(this, amount);
        }
    }

    public boolean hasEnoughBalance(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }

}
