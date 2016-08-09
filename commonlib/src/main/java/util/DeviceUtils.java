package util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.TimeZone;

public final class DeviceUtils {

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getDevice() {
        return Build.DEVICE;
    }

    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static int getApiVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static int getAppVersionCode(Context context) {
        int v = 1;
        try {
            v = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // Huh? Really?
        }
        return v;
    }

    public static String getCountry() {
        if (Locale.getDefault() == null)
            return null;
        return Locale.getDefault().getCountry();
    }

    public static String getLanguage() {
        if (Locale.getDefault() == null)
            return null;
        return Locale.getDefault().getLanguage();
    }

    public static String getTimeZone() {
        if (Locale.getDefault() == null)
            return null;
        return TimeZone.getDefault().getID();
    }

    public static Integer getTimeZoneOffset() {
        if (TimeZone.getDefault() == null)
            return null;
        return TimeZone.getDefault().getRawOffset() / (60 * 60 * 1000);
    }

    public static boolean connectedToWiFi(Context context) {
        try {
            ConnectivityManager connectivityManager = SystemServiceUtils.getConnectivityManager(context);
            State wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if (wifi == State.CONNECTED || wifi == State.CONNECTING)
                return true;
        } catch (Exception e) {
        }

        return false;
    }

    public static long getAvailableMemory(Context context) {
        ActivityManager activityManager = SystemServiceUtils.getActivityManager(context);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static boolean isLowMemory(Context context) {
        ActivityManager activityManager = SystemServiceUtils.getActivityManager(context);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.lowMemory;
    }

    @SuppressWarnings("deprecation")
    public static Point getDisplaySize(Context context) {
        WindowManager windowManager = SystemServiceUtils.getWindowManager(context);
        Point point = new Point();
        Display display = windowManager.getDefaultDisplay();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
            display.getSize(point);
        else {
            point.x = display.getWidth();
            point.y = display.getHeight();
        }

        return point;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            // versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            // Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    public static int getVersionCode(Context context) {
        int versioncode = 1;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versioncode = pi.versionCode;
        } catch (Exception e) {
            // Log.e("VersionInfo", "Exception", e);
        }
        return versioncode;
    }

    private static String getHandSetInfo() {
        String handSetInfo =
                "手机型号:" + Build.MODEL +
                        ",SDK版本:" + Build.VERSION.SDK +
                        ",系统版本:" + Build.VERSION.RELEASE;
        return handSetInfo;
    }

    // 获得状态栏/通知栏的高度
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 手机分辨率
     * @param context
     * @return
     */
    public static String getResolution(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int W = dm.widthPixels;
        int H = dm.heightPixels;
        return W + "*" + H;
    }

}
