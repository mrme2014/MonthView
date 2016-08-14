package com.ishow.ischool.common.manager;


import com.ishow.ischool.bean.user.Token;

/**
 * Created by kevin on 15/8/7.
 */
public class TokenManager {

    private static Token tokenInfo;

    public static void init(Token tokenInfo) {
        TokenManager.tokenInfo = tokenInfo;
    }

    public static void clear() {
        tokenInfo = null;
    }

    public static Token getToken() {
        return tokenInfo;
    }

    public static boolean isAvailable() {
        return tokenInfo != null && tokenInfo.over_time > System.currentTimeMillis() / 1000;
    }
}
