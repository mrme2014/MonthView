package com.ishow.ischool.common.base.presenter.impl;

import android.content.Context;

import com.ishow.ischool.common.base.presenter.IPresenter;
import com.ishow.ischool.common.base.view.IView;

/**
 * 绑定 P V层 以及 解绑
 * Created by MrS on 2016/7/12.
 */
public class BasePresenter<V extends IView> implements IPresenter<V> {

    private V view;

    public Context context;

    public BasePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void dettachView() {
        this.view = null;
    }

    public V getView() {
        return view;
    }

}
