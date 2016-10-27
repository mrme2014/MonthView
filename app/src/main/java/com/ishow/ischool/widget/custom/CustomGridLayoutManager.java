package com.ishow.ischool.widget.custom;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * 禁止recycleview的滑动
 * Created by mini on 16/10/26.
 */

public class CustomGridLayoutManager extends GridLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
