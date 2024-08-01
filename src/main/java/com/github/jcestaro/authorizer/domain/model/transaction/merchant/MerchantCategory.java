package com.github.jcestaro.authorizer.domain.model.transaction.merchant;

import com.github.jcestaro.authorizer.domain.model.account.balance.BalanceType;
import com.github.jcestaro.authorizer.util.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Representa os MCCs (Merchant Category Codes), por não ter muitos valores diferentes,
 * achei a abordagem do enum mais prática, porém já percebi que esse aqui vai escalar no futuro com
 * o nome dos estabelecimentos e os códigos, com certeza é melhor refatorar para buscar do banco de dados.
 * */
public enum MerchantCategory {

    FOOD(List.of("5411", "5412"), List.of("UBER EATS", "IFOOD"), BalanceType.FOOD),
    MEAL(List.of("5811", "5812"), Collections.emptyList(), BalanceType.MEAL),
    CASH(Collections.emptyList(), Collections.emptyList(), BalanceType.CASH);

    private final List<String> codes;
    private final List<String> merchantNames;
    private final BalanceType balanceType;

    MerchantCategory(List<String> codes, List<String> merchantNames, BalanceType balanceType) {
        this.codes = codes;
        this.merchantNames = merchantNames;
        this.balanceType = balanceType;
    }

    public BalanceType getBalanceType() {
        return balanceType;
    }

    private boolean containsCode(String code) {
        return getCodes().contains(code);
    }

    private boolean containsName(String name) {
        return getMerchantNames()
                .stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .anyMatch(merchantName -> merchantName.equals(name));
    }

    private List<String> getCodes() {
        return codes;
    }

    private List<String> getMerchantNames() {
        return merchantNames;
    }

    public static Optional<MerchantCategory> getByCode(String code) {
        return Stream.of(values())
                .filter(merchantCategory -> merchantCategory.containsCode(code))
                .findFirst();
    }

    public static Optional<MerchantCategory> getByName(String name) {
        String treatedName = StringUtil.toEmptyIfNull(name)
                .trim()
                .toLowerCase();

        return Stream.of(values())
                .filter(merchantCategory -> merchantCategory.containsName(treatedName))
                .findFirst();
    }

}
