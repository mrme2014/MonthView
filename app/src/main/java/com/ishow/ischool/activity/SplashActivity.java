package com.ishow.ischool.activity;

import android.os.Handler;
import android.os.Message;

import com.ishow.ischool.R;
import com.ishow.ischool.application.Resourse;
import com.ishow.ischool.business.login.LoginActivity;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.common.manager.TokenManager;

public class SplashActivity extends BaseActivity4Crm {

    public static final int WHAT_MAIN = 1;
    public static final int WHAT_LOGIN = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_MAIN:
                    JumpManager.jumpActivity(SplashActivity.this, MainActivity.class, Resourse.NO_NEED_CHECK);
                    finish();
                    break;
                case WHAT_LOGIN:
                    JumpManager.jumpActivity(SplashActivity.this, LoginActivity.class,Resourse.NO_NEED_CHECK);
                    finish();
                    break;
            }

        }
    };


    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_splash, MODE_NONE, MODE_NONE, MODE_NONE);

    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {
        if (TokenManager.isAvailable()) {
            mHandler.sendEmptyMessageDelayed(WHAT_MAIN, 1000);
        } else {
            mHandler.sendEmptyMessageDelayed(WHAT_LOGIN, 1000);
        }

    }
}
