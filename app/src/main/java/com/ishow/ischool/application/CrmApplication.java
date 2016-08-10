package com.ishow.ischool.application;

import android.app.Activity;

import com.commonlib.application.BaseApplication;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.Stack;

/**
 * Created by MrS on 2016/7/1.
 */

public class CrmApplication extends BaseApplication {

    public static RefWatcher _refWatcher;

    private static CrmApplication instance;

    private static Stack<Activity> stack = new Stack<>();

    @Override
    public void onCreate() {
        super.onCreate();

        _refWatcher = LeakCanary.install(this);
        instance = this;
    }

    public static RefWatcher getRefWatcher() {
        return _refWatcher;
    }

    public static CrmApplication getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static void addStack(Activity activity) {
        stack.addElement(activity);
    }
}
