package com.ishow.ischool.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.presenter.LoginPresenter;
import com.ishow.ischool.view.ILoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
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
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public LoginPresenter bindPresenter() {
        return new LoginPresenter(this);
    }


    @OnClick(R.id.submit_tv)
    void onSubmit(View view) {
        String username = usernameEt.getText().toString();
        String passwd = passwdEt.getText().toString();

        handProgressbar(true);
        mPresenter.login(username, passwd);
    }

    @Override
    public void loginSuccess(User user) {
        handProgressbar(false);
    }

    @Override
    public void loginError(String msg) {
        handProgressbar(false);
    }
}
