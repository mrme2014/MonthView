package com.commonlib.util;

/**
 * Created by MrS on 2016/8/26.
 */
public class TxtUtil {
    public static boolean isLetterDigit(String str){
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for(int i=0 ; i<str.length();i++){
        if(Character.isDigit(str.charAt(i))){   //用char包装类中的判断数字的方法判断每一个字符
            isDigit = true;
        }
        if(Character.isLetter(str.charAt(i))){  //用char包装类中的判断字母的方法判断每一个字符
            isLetter = true;
        }
    }
    String regex = "^[a-zA-Z0-9]+$";
    boolean isRight = isDigit && isLetter&&str.matches(regex);
    return isRight;

}

}
