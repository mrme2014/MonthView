package com.ishow.ischool.business.forgetpwd;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.BaseCompactActivity;
import com.ishow.ischool.common.base.presenter.impl.BasePresenter;

/**
 * Created by MrS on 2016/8/12.
 */
public class ForgetPwdActivity2 extends BaseCompactActivity {
    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_editpwd_newpwd,R.string.eidt_pwd_title_newpwd,-1,0);
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
