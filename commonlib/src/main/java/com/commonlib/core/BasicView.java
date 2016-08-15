package com.commonlib.core;

import android.support.annotation.StringRes;

/**
 * Created by MrS on 2016/8/15.
 *
 * view 层 只用到 sucess  erroe 这两个回调的 应该蛮多
 */
public interface BasicView extends BaseView {
    @StringRes
    void onNetSucess(int  msg);
    @StringRes
    void onNetfaield(int msg);
}
