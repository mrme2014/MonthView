package com.ishow.ischool.common.base;

import com.commonlib.core.BaseLoadActivity;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.common.manager.UserManager;

import butterknife.ButterKnife;

/**
 * Created by mini on 16/11/14.
 */

public class BaseActivity4Loading extends BaseLoadActivity {

    protected User mUser;

    @Override
    protected void initEnv() {
        mUser = UserManager.getInstance().get();
    }

    @Override
    protected void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpContentView() {

    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }
}
