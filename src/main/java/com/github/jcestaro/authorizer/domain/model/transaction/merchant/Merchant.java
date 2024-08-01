package com.github.jcestaro.authorizer.domain.model.transaction.merchant;

import com.github.jcestaro.authorizer.domain.model.account.balance.BalanceType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;

import java.util.Optional;

/**
 * Representa um estabelecimento, não vi necessidade de gravar no banco por enquanto.
 * */
@Embeddable
public class Merchant {

    @Enumerated
    @Column(name = "merchant_category")
    private MerchantCategory category;

    @Column(name = "merchant_name")
    private String name;

    protected Merchant() {
    }

    public Merchant(String name) {
        this.name = name;
    }

    public Merchant(String name, MerchantCategory category) {
        this.name = name;
        this.category = category;
    }

    public Optional<MerchantCategory> getCategory() {
        return Optional.ofNullable(category);
    }

    public String getName() {
        return name;
    }

    public BalanceType getBalanceType() {
        return getCategory()
                .map(MerchantCategory::getBalanceType)
                .orElse(BalanceType.NONE);
    }

}
