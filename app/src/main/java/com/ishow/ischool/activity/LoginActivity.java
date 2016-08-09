package com.ishow.ischool.activity;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.view.impl.BaseCompactActivity;
import com.ishow.ischool.presenter.LoginPresenter;
import com.ishow.ischool.view.ILoginView;

public class LoginActivity extends BaseCompactActivity<LoginPresenter> implements ILoginView {


    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public LoginPresenter bindPresenter() {
        return new LoginPresenter(this);
    }


}
