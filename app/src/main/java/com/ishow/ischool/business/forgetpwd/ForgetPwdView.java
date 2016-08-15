package com.ishow.ischool.business.forgetpwd;

import android.support.annotation.StringRes;

import com.commonlib.core.BasicView;

/**
 * Created by MrS on 2016/8/15.
 */
public interface ForgetPwdView extends BasicView {
    @StringRes
    void onSmsSucess(int msg);
    @StringRes
    void onSmsFaild(int msg);
}
