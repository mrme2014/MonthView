package com.zaaach.citypicker.utils;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.commonlib.util.LogUtil;
import com.commonlib.util.SpUtil;

/**
 * Created by wqf on 16/8/16.
 */
public class LocManager {
    public static final String LOC_KEY = "loc_key";

    private AMapLocationClient mLocationClient;
    private String curCityName;
    private static LocManager locManager;
    private Context mContext;

    public static LocManager getInstance() {
        if (locManager == null) {
            locManager = new LocManager();
        }
        return locManager;
    }

    public void init(Context ctx) {
        this.mContext = ctx;
    }

    /**
     * 获取城市名
     */
    public void startLocation() {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(mContext);
            AMapLocationClientOption option = new AMapLocationClientOption();
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);       //高精度模式
            option.setOnceLocation(true);
            mLocationClient.setLocationOption(option);
            mLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if (aMapLocation != null) {
                        if (aMapLocation.getErrorCode() == 0) {
                            String city = aMapLocation.getCity();
                            String district = aMapLocation.getDistrict();
                            LogUtil.d("onLocationChanged", "city: " + city);
                            LogUtil.d("onLocationChanged", "district: " + district);
                            curCityName = StringUtils.extractLocation(city, district);
                            SpUtil.getInstance(mContext).setValue(LOC_KEY, curCityName);
                        } else {
                            //定位失败
                        }
                    }
                }
            });
        }
        mLocationClient.startLocation();
    }

    public String getCurCityName() {
        if (TextUtils.isEmpty(curCityName)) {
            return SpUtil.getInstance(mContext).getStringValue(LOC_KEY);
        }
        return curCityName;
    }
}
