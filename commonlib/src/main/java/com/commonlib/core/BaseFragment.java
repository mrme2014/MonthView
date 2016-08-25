package com.commonlib.core;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


/**
 * Created by wqf on 16/8/25.
 */
public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    public abstract void init();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    /**
     * 得到可靠地Activity,避免NullPointerException
     *
     * @return
     */
    public Activity getMyActivity() {
        return mActivity;
    }

    protected boolean isActivityUnavaiable() {
        Activity activity = getActivity();
        //Activity被回收导致fragment的getActivity为null的解决办法
        if (Build.VERSION.SDK_INT >= 17) {
            return activity == null || activity.isFinishing() || activity.isDestroyed();
        } else {
            return activity == null || activity.isFinishing();
        }
    }

    public boolean isActivityFinished() {
        Activity activity = getActivity();
        //Activity被回收导致fragment的getActivity为null的解决办法
        if (Build.VERSION.SDK_INT >= 17) {
            return activity == null || activity.isFinishing() || activity.isDestroyed();
        } else {
            return activity == null || activity.isFinishing();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mActivity != null) {
            mActivity = null;
        }
    }
}
