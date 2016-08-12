package com.ishow.ischool.activity;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.presenter.impl.BasePresenter;

/**
 * Created by MrS on 2016/8/12.
 */
public class ForgetPwdActivity1 extends BaseCompactActivity {
    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_editpwd_getcode,R.string.eidt_pwd_title_getcode,-1,0);
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public BasePresenter bindPresenter() {
        return null;
    }
}
