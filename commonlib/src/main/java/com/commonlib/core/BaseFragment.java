package com.commonlib.core;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;

/**
 * Created by wqf on 16/4/28.
 */
public class BaseFragment extends Fragment {

    protected Activity mActivity;

    protected boolean isActivityUnavaiable() {
        Activity activity = getActivity();
        //Activity被回收导致fragment的getActivity为null的解决办法
        if (Build.VERSION.SDK_INT >= 17) {
            return activity == null || activity.isFinishing() || activity.isDestroyed();
        } else {
            return activity == null || activity.isFinishing();
        }
    }

    public void finishActivity() {
        if (!isActivityUnavaiable()) {
            getActivity().finish();
        }
    }

    //  得到可靠地Activity
    public Activity getMyActivity() {
        return mActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

}
