package com.github.jcestaro.authorizer.configuration.response;

public class AuthorizerResponse {

    private final String code;

    private AuthorizerResponse(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static AuthorizerResponse approvedResponse() {
        return new AuthorizerResponse(ResponseCode.APPROVED.getCode());
    }

    public static AuthorizerResponse insufficientFundsResponse() {
        return new AuthorizerResponse(ResponseCode.INSUFFICIENT_FUNDS.getCode());
    }

    public static AuthorizerResponse rejectedResponse() {
        return new AuthorizerResponse(ResponseCode.REJECTED.getCode());
    }

}
