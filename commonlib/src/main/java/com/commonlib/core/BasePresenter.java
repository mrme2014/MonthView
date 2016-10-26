package com.commonlib.core;

/**
 * Created by wqf on 16/8/11.
 */
public abstract class BasePresenter<M, V> {

    public M mModel;
    public V mView;

    public void setMV(M m, V v) {
        this.mModel = m;
        this.mView = v;
    }

    public void onDestroy() {
        mView = null;
    }

}
