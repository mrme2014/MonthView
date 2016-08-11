package com.commonlib.core;

import android.content.Context;

/**
 * Created by wqf on 16/8/11.
 */
public abstract class BasePresenter<M, T> {
    public Context context;
    public M mModel;
    public T mView;

    public void setVM(T v, M m) {
        this.mView = v;
        this.mModel = m;
        this.onStart();
    }

    public abstract void onStart();

    public void onDestroy() {
        mView = null;
    }
}
