package com.michaelsanchez.utils;

public class StringUtils {
    public static String ucFirst(String str) {
        if(isEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }

        if (str.isEmpty()) {
            return true;
        }

        if(str.trim().isEmpty()) {
            return true;
        }

        return false;
    }
}
