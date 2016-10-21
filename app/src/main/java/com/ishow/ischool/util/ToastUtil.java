package com.ishow.ischool.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by abel on 16/10/9.
 */

public class ToastUtil {
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
