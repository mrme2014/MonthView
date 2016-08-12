package com.ishow.ischool.common.manager;

import android.content.Context;
import android.content.Intent;

/**
 * Created by abel on 16/8/12.
 */
public class JumpManager {

    public static void jumpActivity(Context from, Class to) {
        if (!checkUserPermision()) {
            return;
        }
        Intent intent = new Intent(from, to);
        from.startActivity(intent);
    }

    public static void jumpActivity(Context from, Intent intent) {
        if (!checkUserPermision()) {
            return;
        }
        from.startActivity(intent);
    }

    private static boolean checkUserPermision() {
        return true;
    }
}
