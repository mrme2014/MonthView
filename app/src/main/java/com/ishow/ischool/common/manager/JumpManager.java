package com.ishow.ischool.common.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.ishow.ischool.R;
import com.ishow.ischool.application.Resourse;
import com.zaaach.citypicker.utils.ToastUtils;

import java.util.List;

/**
 * Created by abel on 16/8/12.
 */
public class JumpManager {

    private List<Integer> myResources;

    public static void jumpActivity(Context from, Class to, int permission) {
        if (!checkUserPermision(permission))
        {
            ToastUtils.showToast(from, R.string.no_permission);
            return;
        }

        Intent intent = new Intent(from, to);
        from.startActivity(intent);
    }

    public static void jumpActivity(Context from, Intent intent, int permission) {
        if (!checkUserPermision(permission)) {
            return;
        }
        from.startActivity(intent);
    }

    private static boolean checkUserPermision(int permission) {
        if (permission== Resourse.NO_NEED_CHECK)
            return true;
        List<Integer> resurces = UserManager.getInstance().getResurces();
        if (resurces==null)
            return false;
        for (int i = 0; i <resurces.size() ; i++) {
            if (resurces.get(i)==permission)
                return true;
        }
        return false;
    }


    public static void jumpActivityForResult(Activity from, Class to, int requestCode, int permission) {
        if (!checkUserPermision(permission)) {
            return;
        }
        Intent intent = new Intent(from, to);
        from.startActivityForResult(intent, requestCode);
    }

    public static void jumpActivityForResult(Activity from, Intent intent, int requestCode, int permission) {
        if (!checkUserPermision(permission)) {
            return;
        }
        from.startActivityForResult(intent, requestCode);
    }


    public static void jumpActivityForResult(Fragment from, Intent intent, int requestCode, int permission) {
        if (!checkUserPermision(permission)) {
            return;
        }
        from.startActivityForResult(intent, requestCode);
    }

}
