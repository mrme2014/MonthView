package com.ishow.ischool.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;

import com.commonlib.util.LogUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.business.tabbusiness.TabBusinessFragment;
import com.ishow.ischool.business.tabdata.TabDataFragment;
import com.ishow.ischool.business.tabme.MeFragment;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.zaaach.citypicker.utils.LocManager;

import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.strategy.UpdateStrategy;

import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends BaseActivity4Crm implements android.widget.RadioGroup.OnCheckedChangeListener {

    TabDataFragment dataFragment;
    TabBusinessFragment businessFragment;
    MeFragment meFragment;
    Fragment curFragment;
    private int curIndex = 0;
    private String KEY_INDEX = "key_index";

    android.widget.RadioGroup RadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {  // “内存重启”时调用,解决重叠问题
            dataFragment = (TabDataFragment) getSupportFragmentManager().findFragmentByTag(TabDataFragment.class.getName());
            businessFragment = (TabBusinessFragment) getSupportFragmentManager().findFragmentByTag(TabBusinessFragment.class.getName());
            meFragment = (MeFragment) getSupportFragmentManager().findFragmentByTag(MeFragment.class.getName());
            curIndex = savedInstanceState.getInt(KEY_INDEX);
            switch (curIndex) {
                case 0:
                    getSupportFragmentManager().beginTransaction()
                            .show(dataFragment)
                            .hide(businessFragment)
                            .hide(meFragment)
                            .commitAllowingStateLoss();
                    curFragment = dataFragment;
                    break;
                case 1:
                    getSupportFragmentManager().beginTransaction()
                            .hide(dataFragment)
                            .show(businessFragment)
                            .hide(meFragment)
                            .commitAllowingStateLoss();
                    curFragment = businessFragment;
                    break;
                case 2:
                    getSupportFragmentManager().beginTransaction()
                            .hide(dataFragment)
                            .hide(businessFragment)
                            .show(meFragment)
                            .commitAllowingStateLoss();
                    curFragment = meFragment;
                    break;
            }
        } else {                            // 正常create
            dataFragment = new TabDataFragment();
            businessFragment = new TabBusinessFragment();
            meFragment = new MeFragment();
            curFragment = dataFragment;

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.tabcontent, dataFragment, dataFragment.getClass().getName())
                    .add(R.id.tabcontent, businessFragment, businessFragment.getClass().getName())
                    .add(R.id.tabcontent, meFragment, meFragment.getClass().getName())
                    .hide(businessFragment)
                    .hide(meFragment)
                    .commitAllowingStateLoss();
        }

        if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //使得布局延伸到状态栏和导航栏区域
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            //透明状态栏/导航栏
            window.setStatusBarColor(Color.TRANSPARENT);
//            if (SDK_INT < 23) {
//                ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
//                View parentView = contentFrameLayout.getChildAt(0);
//                parentView.setFitsSystemWindows(true);
//            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存当前Fragment的下标
        outState.putInt(KEY_INDEX, curIndex);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_main, -1, -1, MODE_NONE);
    }

    @Override
    protected void setUpView() {
        RadioGroup = (android.widget.RadioGroup) findViewById(R.id.RadioGroup);
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
        ft.hide(curFragment);
        switch (checkedId) {
            case R.id.tab_data:
                curIndex = 0;
                if (dataFragment == null) {
                    dataFragment = new TabDataFragment();
                    ft.add(R.id.tabcontent, dataFragment).show(dataFragment);
                }
                curFragment = dataFragment;
                break;
            case R.id.tab_business:
                curIndex = 1;
                if (businessFragment == null) {
                    businessFragment = new TabBusinessFragment();
                    ft.add(R.id.tabcontent, businessFragment).show(businessFragment);
                }
                curFragment = businessFragment;
                break;
            case R.id.tab_me:
                curIndex = 2;
                LogUtil.e(System.currentTimeMillis() + "");
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    ft.add(R.id.tabcontent, meFragment).show(meFragment);
                }
                curFragment = meFragment;
                break;
        }
        ft.show(curFragment).commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (meFragment != null)
            meFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }


    void checkUpdate() {
        UpdateBuilder.create().strategy(new UpdateStrategy() {
            @Override
            public boolean isShowUpdateDialog(Update update) {
                // 有新更新直接展示
                return true;
            }

            @Override
            public boolean isAutoInstall() {
                return false;
            }

            @Override
            public boolean isShowDownloadDialog() {
                // 展示下载进度
                return true;
            }
        }).check(MainActivity.this);
    }
}


