package com.ishow.ischool.common.base.presenter.impl;

import com.ishow.ischool.common.base.presenter.IPresenter;
import com.ishow.ischool.common.base.view.IView;

/**
 * 绑定 P V层 以及 解绑
 * Created by MrS on 2016/7/12.
 */
public class BasePresenter<V extends IView> implements IPresenter<V> {

    public V view;
    public BasePresenter(V view) {
        this.view = view;
    }

    public V getView() {
        return view;
    }

    @Override
    public void destroy() {
        view = null;
    }

}
