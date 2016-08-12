package com.ishow.ischool.activity;

import android.widget.EditText;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.user.User;

import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.presenter.LoginPresenter;
import com.ishow.ischool.view.ILoginView;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseCompactActivity<LoginPresenter> implements ILoginView {


    @BindView(R.id.username_et)
    EditText usernameEt;

    @BindView(R.id.passwd_et)
    EditText passwdEt;

    @BindView(R.id.submit_tv)
    TextView submitTv;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_login,MODE_NONE,MODE_NONE,MODE_NONE);
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


    @OnClick(R.id.submit_tv)
    void onLogin() {
        String username = usernameEt.getText().toString();
        String passwd = passwdEt.getText().toString();

        handProgressbar(true);
        mPresenter.login(username, passwd);
    }

    @Override
    public void loginSuccess(User user) {

        handProgressbar(false);

        JumpManager.jumpActivity(this, MainActivity.class);
        finish();

    }

    @Override
    public void loginError(String msg) {

        handProgressbar(false);

        showToast(msg);

    }
}
