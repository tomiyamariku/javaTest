package com.todo.app.utility;

public class CheckUtility {
    
    //数値チェック
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
