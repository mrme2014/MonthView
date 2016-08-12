package com.ishow.ischool.activity;

import android.os.Handler;
import android.os.Message;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.presenter.impl.BasePresenter;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.common.manager.TokenManager;
public class SplashActivity extends BaseCompactActivity {

    public static final int WHAT_MAIN = 1;
    public static final int WHAT_LOGIN = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_MAIN:
                    JumpManager.jumpActivity(SplashActivity.this, MainActivity.class);
                    finish();
                    break;
                case WHAT_LOGIN:
                    JumpManager.jumpActivity(SplashActivity.this, LoginActivity.class);
                    finish();
                    break;
            }

        }
    };


    private void gotoMainActivity() {
       startActivity(LoginActivity.class);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_splash,MODE_NONE,MODE_NONE,MODE_NONE);
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

    @Override
    public BasePresenter bindPresenter() {
        return null;
    }
}
