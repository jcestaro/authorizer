package com.github.jcestaro.authorizer.util;

public class StringUtil {

    private StringUtil() {
    }

    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty() || string.isBlank();
    }

    public static String toEmptyIfNull(String string) {
        return string == null ? "" : string;
    }

}
