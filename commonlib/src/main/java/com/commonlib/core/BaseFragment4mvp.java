package com.commonlib.core;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.commonlib.core.util.GenericUtil;

/**
 * Created by wqf on 16/4/28.
 */
public abstract class BaseFragment4mvp<P extends BasePresenter, M extends BaseModel> extends Fragment {

    public P mPresenter;
    public M mModel;

    protected Activity mActivity;
    protected View rootView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }


    public abstract void init();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (this instanceof BaseView) {
            mPresenter = GenericUtil.getType(this, 0);
            mModel = GenericUtil.getType(this, 1);
            if (mPresenter != null && mModel != null && this instanceof BaseView) {
                mPresenter.setMV(mModel, this);
            }
        }
        /*if (this instanceof BaseView) {
            mPresenter.setMV(mModel, this);
        }*/
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
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }
}
