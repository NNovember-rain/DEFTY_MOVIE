package com.defty.movie.utils;

public class NumberUtil {

    public static boolean isNumber(String value) {
        try {
            Long number=Long.parseLong(value);
            return true;
        }catch(Exception e) {
            return false;
        }
    }

}
