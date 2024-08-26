package com.github.jcestaro.authorizer.domain.model.transaction;


import com.github.jcestaro.authorizer.domain.model.account.Account;
import com.github.jcestaro.authorizer.domain.model.account.balance.BalanceType;
import com.github.jcestaro.authorizer.domain.model.transaction.merchant.Merchant;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Representa a transação de uma venda
 * */
@Entity
@Table(name = "transaction_payload")
public class TransactionPayload {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "transaction_payload_seq", sequenceName = "transaction_payload_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "amount")
    private BigDecimal amount;

    @Embedded
    private Merchant merchant;

    protected TransactionPayload() {
    }

    private TransactionPayload(Builder builder) {
        account = builder.account;
        amount = builder.amount;
        merchant = builder.merchant;
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public BalanceType getMerchantBalanceType() {
        return merchant.getBalanceType();
    }

    public static final class Builder {

        private Account account;
        private BigDecimal amount;
        private Merchant merchant;

        public Builder() {
        }

        public Builder account(Account val) {
            account = val;
            return this;
        }

        public Builder amount(BigDecimal val) {
            amount = val;
            return this;
        }

        public Builder merchant(Merchant val) {
            merchant = val;
            return this;
        }

        public TransactionPayload build() {
            return new TransactionPayload(this);
        }

    }

}
