package com.github.jcestaro.authorizer.domain.model.account.balance;

/**
 * Tipos de saldo, pelo tamanho da aplicação um enum é o suficiente, porém
 * se começar a crescer muito é melhor guardar no banco e listar conforme a necessidade
 * */
public enum BalanceType {

    FOOD,
    MEAL,
    CASH,
    NONE

}
