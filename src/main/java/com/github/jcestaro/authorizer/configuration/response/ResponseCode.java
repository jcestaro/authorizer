package com.github.jcestaro.authorizer.configuration.response;

public enum ResponseCode {

    APPROVED("00"),
    INSUFFICIENT_FUNDS("51"),
    REJECTED("07");

    private final String code;

    ResponseCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
