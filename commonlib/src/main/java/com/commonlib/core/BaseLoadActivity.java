package com.commonlib.core;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.commonlib.R;


/**
 * Created by wqf on 16/4/28.
 * 适用于有页面加载失败，页面加载中，页面数据为空等显示需求的Activity
 */
public abstract class BaseLoadActivity extends BaseActivity {
    protected LoadFrameLayout mLoadFrameLayout;

    protected void setLoadContentView(int layoutResID, String titleStr, int menuId, int mode) {
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        View baseView = LayoutInflater.from(this).inflate(R.layout.widget_load_base_layout, viewGroup, true);
        mLoadFrameLayout = (LoadFrameLayout) baseView.findViewById(R.id.baseFrameLayout);
        LayoutInflater.from(this).inflate(layoutResID, mLoadFrameLayout, true);
        setUpToolbar(titleStr, menuId, mode);
        mLoadFrameLayout.showLoadingView();
    }

    protected void setLoadContentView(int layoutResID, int titleResId, int menuId, int mode) {
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        View baseView = LayoutInflater.from(this).inflate(R.layout.widget_load_base_layout, viewGroup, true);
        mLoadFrameLayout = (LoadFrameLayout) baseView.findViewById(R.id.baseFrameLayout);
        mLoadFrameLayout.setContentView(layoutResID);
        setUpToolbar(titleResId, menuId, mode);
        mLoadFrameLayout.showLoadingView();
    }

    protected void showContentView() {
        mLoadFrameLayout.showContentView();
    }

    protected void showEmptyView() {
        mLoadFrameLayout.showEmptyView();
    }

    protected void showErrorView() {
        mLoadFrameLayout.showErrorView();
    }
}
