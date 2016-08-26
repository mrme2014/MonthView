package com.ishow.ischool.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;

import com.commonlib.util.LogUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.business.tabbusiness.TabBusinessFragment;
import com.ishow.ischool.business.tabfragmentme.MeFragment;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.zaaach.citypicker.utils.LocManager;

import org.lzh.framework.updatepluginlib.UpdateBuilder;

public class MainActivity extends BaseActivity4Crm implements android.widget.RadioGroup.OnCheckedChangeListener {


    TabBusinessFragment businessFragment;
    MeFragment meFragment;


    android.widget.RadioGroup RadioGroup;


    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_main, -1, -1, MODE_NONE);
    }

    @Override
    protected void setUpView() {
        RadioGroup = (android.widget.RadioGroup) findViewById(R.id.RadioGroup);
        showFragment(R.id.tab_business);
        RadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void setUpData() {
        // 进入应用获取城市名然后保存起来
        LocManager.getInstance().startLocation();
        checkUpdate();
    }

    @Override
    public void onCheckedChanged(android.widget.RadioGroup group, int checkedId) {
        showFragment(checkedId);
    }

    private void showFragment(int checkedId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (checkedId) {
            case R.id.tab_business:
                if (meFragment != null) {
                    ft.hide(meFragment);
                }
                if (businessFragment == null) {
                    businessFragment = new TabBusinessFragment();
                    ft.add(R.id.tabcontent, businessFragment).show(businessFragment);
                } else {
                    ft.show(businessFragment);
                }
                break;
            case R.id.tab_me:
                LogUtil.e(System.currentTimeMillis()+"");
                if (businessFragment != null) {
                    ft.hide(businessFragment);
                }
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    ft.add(R.id.tabcontent, meFragment).show(meFragment);
                } else {
                    ft.show(meFragment);
                }
                break;
        }

        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (meFragment!=null)
            meFragment.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);

    }


    void checkUpdate() {
        UpdateBuilder.create().check(MainActivity.this);
    }
}


