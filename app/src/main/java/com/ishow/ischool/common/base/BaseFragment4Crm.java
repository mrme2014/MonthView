package com.ishow.ischool.common.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.commonlib.core.BaseFragment;
import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mini on 16/8/14.
 */
public abstract class BaseFragment4Crm<P extends BasePresenter, M extends BaseModel> extends BaseFragment<P, M> {

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), null);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    public abstract int getLayoutId();
}
