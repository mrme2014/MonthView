package com.ishow.ischool.business.login;

import android.widget.EditText;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.activity.MainActivity;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqf on 16/8/13.
 */
public class LoginActivity extends BaseActivity4Crm<LoginPresenter, LoginModel> implements LoginContract.View {


    @BindView(R.id.username_et)
    EditText usernameEt;
    @BindView(R.id.passwd_et)
    EditText passwdEt;
    @BindView(R.id.submit_tv)
    TextView submitTv;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_login, -1, -1, MODE_NONE);
    }

    @Override
    protected void setUpView() {
        usernameEt.setText("13512341234");
        passwdEt.setText("qq123456/");
    }

    @Override
    protected void setUpData() {

    }


    @OnClick(R.id.submit_tv)
    void onLogin() {
        String username = usernameEt.getText().toString();
        String passwd = passwdEt.getText().toString();

        submitTv.setEnabled(false);
        mPresenter.login(username, passwd);
    }

    @Override
    public void loginSuccess() {
        JumpManager.jumpActivity(this, MainActivity.class);
        finish();
    }

    @Override
    public void loginError(String msg) {
        submitTv.setEnabled(true);
        showToast(msg);
    }
}
