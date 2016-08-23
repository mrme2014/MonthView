package com.ishow.ischool.common.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

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

    public static void jumpActivityForResult(Activity from, Class to, int requestCode) {
        Intent intent = new Intent(from, to);
        from.startActivityForResult(intent, requestCode);
    }

    public static void jumpActivityForResult(Activity from, Intent intent, int requestCode) {
        from.startActivityForResult(intent, requestCode);
    }


    public static void jumpActivityForResult(Fragment from, Intent intent, int requestCode) {
        from.startActivityForResult(intent, requestCode);
    }

}
