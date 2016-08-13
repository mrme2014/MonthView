package com.commonlib.core;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.commonlib.core.util.GenericUtil;

/**
 * Created by wqf on 16/4/28.
 */
public class BaseFragment<P extends BasePresenter, M extends BaseModel> extends Fragment {

    public P mPresenter;
    public M mModel;

    protected Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = GenericUtil.getType(this, 0);
        mModel = GenericUtil.getType(this, 1);
        if (this instanceof BaseView) {
            mPresenter.setVM(this, mModel);
        }
    }

    /**
     * 得到可靠地Activity,避免NullPointerException
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







}
