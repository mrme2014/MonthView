package com.commonlib.widget.imageloader;

import android.content.Context;

import com.commonlib.util.SpUtil;

/**
 * Created by wqf on 16/8/10.
 */
public class ImageLoaderConf {
    public static final String ONLY_WIFI_LOAD_IMG = "OnlyWifiLoadImg";

    /**
     * WIFI下加载大图
     */
    public static boolean getOnlyWifiLoadImg(Context ctx) {
        return SpUtil.getInstance(ctx).getBoolValue(ONLY_WIFI_LOAD_IMG);
    }

    public static void setOnlyWifiLoadImg(Context ctx, boolean isOn) {
        SpUtil.getInstance(ctx).setValue(ONLY_WIFI_LOAD_IMG, isOn);
    }
}
