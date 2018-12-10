package com.michaelsanchez.utils;

public class StringUtils {
    public static String ucFirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
