package com.ishow.ischool.business.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.commonlib.util.LogUtil;
import com.commonlib.util.PreferencesUtils;
import com.ishow.ischool.R;
import com.ishow.ischool.activity.MainActivity;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.business.forgetpwd.ForgetPwdActivity1;
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

    @BindView(R.id.login_layout)
    ScrollView mLoginSv;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_login, -1, -1, MODE_NONE);
    }

    @Override
    protected void setUpView() {
        Intent intent = getIntent();
        if (intent.hasExtra("invalidate_token")) {
            Toast.makeText(this, getString(R.string.login_in_other_devices), Toast.LENGTH_SHORT).show();
        }

        passwdEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mLoginSv.post(new Runnable() {
                        @Override
                        public void run() {
                            mLoginSv.fullScroll(View.FOCUS_DOWN);
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void setUpData() {
        String lastUserName = PreferencesUtils.getString(this, "last_user");
        usernameEt.setText(lastUserName);
    }

    @OnClick(R.id.forget_passwd)
    void forgetPwd() {
        JumpManager.jumpActivity(this, ForgetPwdActivity1.class, Resource.NO_NEED_CHECK);
    }

    @OnClick(R.id.submit_tv)
    void onLogin() {

        String username = usernameEt.getText().toString();
        String passwd = passwdEt.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(passwd)) {
            showToast(R.string.input_user_and_passwd);
            return;
        }
        handProgressbar(true);
        mPresenter.login(username, passwd);
        submitTv.setEnabled(false);
    }

    @Override
    public void loginSuccess(User user) {
        handProgressbar(false);
        PreferencesUtils.put(this, "last_user", user.userInfo.mobile);
        JumpManager.jumpActivity(this, MainActivity.class, Resource.NO_NEED_CHECK);
        finish();
    }

    @Override
    public void loginError(String msg) {

        handProgressbar(false);
        showToast(msg);
        LogUtil.d(msg);
        submitTv.setEnabled(true);
        showToast(msg);

    }
}
