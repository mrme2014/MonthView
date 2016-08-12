package com.commonlib.application;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by wqf on 16/8/9.
 */
public class BaseApplication extends Application {

    public static RefWatcher _refWatcher;

    private static BaseApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();

        _refWatcher = LeakCanary.install(this);

        instance = this;
    }

    public static RefWatcher getRefWatcher() {
        return _refWatcher;
    }

    public static BaseApplication getInstance() {
        return instance;
    }

}
