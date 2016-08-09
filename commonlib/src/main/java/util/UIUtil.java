package util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class UIUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float getScale(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int[] getDisplaySize(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int[] screen = new int[] { dm.widthPixels, dm.heightPixels };
        return screen;
    }

    public static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }

    /**
     * 获取屏幕宽度
     * @param mContext
     * @return px
     */
    public static int getScreenWidthPixels(Context mContext) {

        WindowManager wm =(WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm =new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     * @param mContext
     * @return  px
     */
    public static int getScreenHeightPixels(Context mContext) {

        WindowManager wm =(WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm =new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
    /**
     * 在activity 查找View
     * 
     * @param context
     * @param id
     * @return
     */

    @SuppressWarnings("unchecked")
    public static <T extends View> T find(Activity context, int id) {
        return (T) context.findViewById(id);
    }

    /**
     * 在View中查找View
     * 
     * @param view
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T find(View view, int id) {
        return (T) view.findViewById(id);
    }



}
