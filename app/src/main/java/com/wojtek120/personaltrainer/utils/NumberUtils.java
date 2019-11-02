package com.wojtek120.personaltrainer.utils;

public class NumberUtils {

    private NumberUtils() {
    }

    /**
     * Checks if string is number
     * @param strNum - string to check
     * @return true if string is numeric, otherwise false
     */
    public static boolean isNumeric(String strNum) {
        return strNum.matches("\\d+(\\.\\d+)?");
    }
}
