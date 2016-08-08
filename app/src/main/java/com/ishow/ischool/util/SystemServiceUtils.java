package com.ishow.ischool.util;


import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.PowerManager;
import android.view.WindowManager;

public final class SystemServiceUtils {

    public static WindowManager getWindowManager(Context context) {

        try {
            return (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
        } catch (SecurityException e) {
        } catch (ClassCastException e) {
        }

        return null;

    }

    public static PowerManager getPowerManager(Context context) {

        try {
            return (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        } catch (SecurityException e) {
        } catch (ClassCastException e) {
        }

        return null;

    }

    public static ActivityManager getActivityManager(Context context) {

        try {
            return (ActivityManager) context.getSystemService(Service.ACTIVITY_SERVICE);
        } catch (SecurityException e) {
        } catch (ClassCastException e) {
        }

        return null;

    }

    public static ConnectivityManager getConnectivityManager(Context context) {

        try {
            return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        } catch (SecurityException e) {
        } catch (ClassCastException e) {
        }

        return null;
    }

}
