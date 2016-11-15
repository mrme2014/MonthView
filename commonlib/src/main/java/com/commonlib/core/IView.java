package com.commonlib.core;

/**
 * Created by abel on 16/11/15.
 */

public interface IView {
    boolean isActivityFinished();

    void showLoading();
    void dismissLoading();
}
