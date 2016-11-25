package com.commonlib.core;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.commonlib.R;
import com.commonlib.core.view.LoadFrameLayout;



public abstract class BaseLoadActivity extends BaseActivity {
    protected LoadFrameLayout mLoadFrameLayout;

//    @Override
//    protected void showLoading() {
//        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
//        if (viewGroup.getChildCount() > 1) {
//            for (int i = 1; i < viewGroup.getChildCount(); i++) {
//                viewGroup.removeViewAt(i);
//            }
//        }
////        viewGroup.removeAllViews();
//        View baseView = LayoutInflater.from(this).inflate(R.layout.widget_load_base_layout, viewGroup, true);
//        mLoadFrameLayout = (LoadFrameLayout) baseView.findViewById(R.id.baseFrameLayout);
//        mLoadFrameLayout.setContentView(layoutResID);
//        LayoutInflater.from(this).inflate(this.layoutResID, mLoadFrameLayout, true);
//        mLoadFrameLayout.showLoadingView();
//    }

//    protected void setLoadContentView() {
//        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
//        viewGroup.removeAllViews();
//        View baseView = LayoutInflater.from(this).inflate(R.layout.widget_load_base_layout, viewGroup, true);
//        mLoadFrameLayout = (LoadFrameLayout) baseView.findViewById(R.id.baseFrameLayout);
//        LayoutInflater.from(this).inflate(this.layoutResID, mLoadFrameLayout, true);
////        setUpToolbar(titleStr, menuId, mode);
//        mLoadFrameLayout.showLoadingView();
//    }

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
