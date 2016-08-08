package com.ishow.ischool.common.api;

import rx.Observer;

/**
 * Created by abel on 16/7/29.
 */
public abstract class ApiObserver<T> implements Observer<T> {

    @Override
    public void onError(Throwable e) {
        onError(e.getMessage());
        onCompleted();
    }

    public abstract void onError(String msg);
}
