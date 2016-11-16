package com.ishow.ischool.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.commonlib.util.StatusBarCompat;
import com.commonlib.widget.base.FragmentTabHost;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Constants;
import com.ishow.ischool.business.tabbusiness.TabBusinessFragment;
import com.ishow.ischool.business.tabdata.TabDataFragment;
import com.ishow.ischool.business.tabindex.TabIndexFragment;
import com.ishow.ischool.business.tabme.TabMeFragment;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.CampusManager;
import com.zaaach.citypicker.utils.LocManager;

import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.strategy.UpdateStrategy;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity4Crm {


    private ArrayList<Class> fragments;
    private ArrayList<String> titles;
    private ArrayList<Integer> drawables;


    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabHost;

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarCompat.compat(this);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_main, -1, -1, MODE_NONE);
    }

    @Override
    protected void setUpView() {

        fragments = new ArrayList<>();
        drawables = new ArrayList<>();
        titles = new ArrayList<>();

        if (isHeadquarters()) {
            fragments.add(TabIndexFragment.class);
            titles.add(getString(R.string.tab_index));
            drawables.add(R.drawable.tab_index);
        }

        fragments.add(TabDataFragment.class);
        fragments.add(TabBusinessFragment.class);
        fragments.add(TabMeFragment.class);

        titles.add(getString(R.string.tab_data));
        titles.add(getString(R.string.tab_business));
        titles.add(getString(R.string.tab_me));

        drawables.add(R.drawable.tab_data);
        drawables.add(R.drawable.tab_business);
        drawables.add(R.drawable.tab_me);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabcontent);

        for (int i = 0; i < fragments.size(); i++) {
            // Tab按钮添加文字和图片
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(titles.get(i)).setIndicator(getIndicatorView(i));
            // 添加Fragment
            mTabHost.addTab(tabSpec, fragments.get(i), null);
            // 设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.white);
        }
        mTabHost.getTabWidget().setDividerDrawable(R.color.transparent);
    }

    private boolean isHeadquarters() {
        return mUser.positionInfo.campusId == Constants.CAMPUS_HEADQUARTERS;
    }

    private View getIndicatorView(int i) {
        View view = getLayoutInflater().inflate(R.layout.item_main_tab, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_text);
        textView.setText(titles.get(i));
        textView.setCompoundDrawablesWithIntrinsicBounds(0, drawables.get(i), 0, 0);
        return view;
    }

    @Override
    protected void setUpData() {
        // 进入应用获取城市名然后保存起来
        LocManager.getInstance().startLocation();
        checkUpdate();
        CampusManager.getInstance().getFromServer(getApplicationContext());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment meFragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.tab_me));
        if (meFragment != null) {
            meFragment.onActivityResult(requestCode, resultCode, data);
        }
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


