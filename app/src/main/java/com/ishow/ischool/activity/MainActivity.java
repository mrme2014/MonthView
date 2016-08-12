package com.ishow.ischool.activity;

import android.support.v4.app.FragmentTransaction;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.presenter.impl.BasePresenter;
import com.ishow.ischool.fragment.BusinessFragment;
import com.ishow.ischool.fragment.MeFragment;

public class MainActivity extends BaseCompactActivity implements android.widget.RadioGroup.OnCheckedChangeListener {


    BusinessFragment businessFragment;
    MeFragment meFragment;


    android.widget.RadioGroup RadioGroup;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_main, -1, -1, MODE_NONE);

        RadioGroup = (android.widget.RadioGroup) findViewById(R.id.RadioGroup);
        showFragment(R.id.tab_business);
        RadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void setUpView() {
        showFragment(R.id.tab_business);
        RadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public BasePresenter bindPresenter() {
        return null;
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
                    businessFragment = new BusinessFragment();
                    ft.add(R.id.tabcontent, businessFragment).show(businessFragment);
                } else {
                    ft.show(businessFragment);
                }
                break;
            case R.id.tab_me:
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

}
