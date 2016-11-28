package com.ishow.ischool.business.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.commonlib.util.LogUtil;
import com.commonlib.util.PreferencesUtils;
import com.commonlib.widget.LabelCleanableEditText;
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
    LabelCleanableEditText passwdEt;
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
                updateCleanable(passwdEt.length(), hasFocus);
            }
        });

        passwdEt.setClearListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesUtils.remove(LoginActivity.this, "last_passwd");
            }
        });

        passwdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateCleanable(s.length(), passwdEt.hasFocus());
            }
        });
    }

    //当内容不为空，而且获得焦点，才显示右侧删除按钮
    public void updateCleanable(int length, boolean hasFocus) {
        if (length > 0 && hasFocus)
            passwdEt.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.ic_search_clear), null);
        else
            passwdEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    @Override
    protected void setUpData() {
        String lastUserName = PreferencesUtils.getString(this, "last_user");
        String lastPasswd = "";
        if (System.currentTimeMillis() - PreferencesUtils.getLong(this, "last_passwd_save_date") > 7 * 24 * 3600) {//7天有效期
            PreferencesUtils.remove(this, "last_passwd_save_date");
        } else {
            lastPasswd = PreferencesUtils.getString(this, "last_passwd");
        }
        usernameEt.setText(lastUserName);
        passwdEt.setText(lastPasswd);
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
        PreferencesUtils.put(this, "last_passwd", passwdEt.getText().toString());
        PreferencesUtils.put(this, "last_passwd_save_date", System.currentTimeMillis());
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
