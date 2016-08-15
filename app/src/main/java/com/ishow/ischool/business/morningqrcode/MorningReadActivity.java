package com.ishow.ischool.business.morningqrcode;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.BaseCompactActivity;
import com.ishow.ischool.common.base.presenter.impl.BasePresenter;

/**
 * Created by MrS on 2016/8/12.
 */
public class MorningReadActivity extends BaseCompactActivity {
    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_mornig_read_qrcode,R.string.morning_read_qrcode_title,-1,0);
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
