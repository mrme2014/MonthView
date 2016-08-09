package com.commonlib.application;

import android.app.Activity;
import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.Stack;

/**
 * Created by wqf on 16/8/9.
 */
public class BaseApplication extends Application {

    public static RefWatcher _refWatcher;

    private static BaseApplication instance;

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

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static void addStack(Activity activity) {
        stack.addElement(activity);
    }

    public static void removeStack(Activity activity) {
        stack.removeElement(activity);
    }

    public static void clearStack(Class claz) {
        for (int i = 0; i < stack.size(); i++) {
            Activity activity1 = stack.get(i);
            if (activity1.getClass() != claz)
                activity1.finish();
        }
    }
}
