package com.ishow.ischool.activity;

import android.os.Handler;
import android.os.Message;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.presenter.impl.BasePresenter;

public class SplashActivity extends BaseCompactActivity {

    public static final int WHAT_MAIN = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_MAIN:
                    gotoMainActivity();
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
        mHandler.sendEmptyMessageDelayed(WHAT_MAIN, 1000);
    }

    @Override
    public BasePresenter bindPresenter() {
        return null;
    }
}
