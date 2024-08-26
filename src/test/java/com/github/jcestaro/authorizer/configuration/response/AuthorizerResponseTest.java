package com.github.jcestaro.authorizer.configuration.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorizerResponseTest {

    @Test
    @DisplayName("Approved response must have return code for approved response code")
    void test01() {
        assertEquals(ResponseCode.APPROVED.getCode(), AuthorizerResponse.approvedResponse().getCode());
    }

    @Test
    @DisplayName("Insufficient funds response must have return code for insufficient funds response code")
    void test02() {
        assertEquals(ResponseCode.INSUFFICIENT_FUNDS.getCode(), AuthorizerResponse.insufficientFundsResponse().getCode());
    }

    @Test
    @DisplayName("Rejected response must have return code for rejected response code")
    void test03() {
        assertEquals(ResponseCode.REJECTED.getCode(), AuthorizerResponse.rejectedResponse().getCode());
    }

}