package com.ishow.ischool.activity;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.presenter.impl.BasePresenter;

/**
 * Created by MrS on 2016/8/12.
 */
public class EditPwdActivity extends BaseCompactActivity {
    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_editpwd,R.string.edit_pwd,-1,0);
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
