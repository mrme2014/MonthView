package com.ishow.ischool.util;

import com.commonlib.util.DateUtil;

/**
 * Created by abel on 16/11/28.
 */

public class TextUtil {
    public static String format4Table(String text) {

        if (text == null || "".equals(text)) {
            return "-";
        }
        return text;
    }

    public static String format4Table(int text) {

        if (text == 0) {
            return "-";
        }
        return DateUtil.parseSecond2Str((long) text);
    }
}
