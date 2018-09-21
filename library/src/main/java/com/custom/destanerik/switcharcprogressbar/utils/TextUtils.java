package com.custom.destanerik.switcharcprogressbar.utils;

public class TextUtils {
    public static boolean checkEmptyOrNull(String str){
        if (str == null || str.isEmpty())
            return true;

        return false;
    }
}
