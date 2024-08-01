package com.github.jcestaro.authorizer.util;

public class NumberUtil {

    private static final String ONLY_NUMBERS_REGEX = "^[0-9]+$";

    private NumberUtil() {
    }

    public static boolean isNullOrZero(Number number) {
        return number == null || number.doubleValue() == 0.0;
    }

    public static boolean isNegative(Number number) {
        return number != null && number.doubleValue() < 0.0;
    }

    public static Long parseLong(String value) {
        return StringUtil.isEmpty(value) || !value.matches(ONLY_NUMBERS_REGEX)
                ? 0L
                : Long.parseLong(value.trim());
    }

}
