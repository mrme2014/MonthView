package com.ishow.ischool.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TextView;

import com.commonlib.widget.base.FragmentTabHost;
import com.ishow.ischool.R;
import com.ishow.ischool.business.tabbusiness.TabBusinessFragment;
import com.ishow.ischool.business.tabdata.TabDataFragment;
import com.ishow.ischool.business.tabindex.TabIndexMarketFragment;
import com.ishow.ischool.business.tabme.TabMeFragment;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.zaaach.citypicker.utils.LocManager;

import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.strategy.UpdateStrategy;

import java.util.ArrayList;

import butterknife.BindView;

import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends BaseActivity4Crm {


    private ArrayList<Class> fragments;
    private ArrayList<String> titles;
    private ArrayList<Integer> drawables;


    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabHost;

    @Override
    protected void initEnv() {
        super.initEnv();
        if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_main, -1, -1, MODE_NONE);
    }

    @Override
    protected void setUpView() {

        fragments = new ArrayList<>(4);
        fragments.add(TabIndexMarketFragment.class);
        fragments.add(TabDataFragment.class);
        fragments.add(TabBusinessFragment.class);
        fragments.add(TabMeFragment.class);

        titles = new ArrayList<>(4);
        titles.add(getString(R.string.tab_index));
        titles.add(getString(R.string.tab_data));
        titles.add(getString(R.string.tab_business));
        titles.add(getString(R.string.tab_me));

        drawables = new ArrayList<>(4);
        drawables.add(R.drawable.tab_data);
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


