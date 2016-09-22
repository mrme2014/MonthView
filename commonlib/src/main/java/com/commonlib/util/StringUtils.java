package com.commonlib.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kevin on 15/8/31.
 */
public class StringUtils {

    public static final String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

    //    public static final Pattern EMAIL_PATTERN = Pattern.compile("^/w+([-.]/w+)*@/w+([-]/w+)*/.(/w+([-]/w+)*/.)*[a-z]{2,3}$");
    public static final Pattern EMAIL_PATTERN = Pattern.compile(str);

    public static final boolean isEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public static boolean isEmpty(String miregId) {
        if (miregId == null || "".equals(miregId)) {
            return true;
        }
        return false;
    }

    public static String parseString(float f) {
        if (f % 1 == 0) {
            return String.valueOf((int) f);
        } else {
            return String.valueOf(f);
        }
    }

    public static boolean contains(String curProcessName, String s) {
        if (curProcessName == null || s == null) {
            return false;
        }

        if (curProcessName.indexOf(s) > 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(String flag, String saveValue) {
        if (flag == null) {
            return false;
        }
        if (flag.equals(saveValue)) {
            return true;
        }
        return false;
    }

    public static String split(List array, String split) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.size(); i++) {
            sb.append(array.get(i)).append(split);
        }
        String str = sb.toString();
        return str.substring(0, str.length() - 1);
    }

}
