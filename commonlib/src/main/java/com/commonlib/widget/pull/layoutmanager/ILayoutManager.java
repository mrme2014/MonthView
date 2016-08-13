package com.commonlib.widget.pull.layoutmanager;

import android.support.v7.widget.RecyclerView;

import com.commonlib.widget.pull.BaseListAdapter;


/**
 * Created by wqf on 16/4/29.
 */
public interface ILayoutManager {
    RecyclerView.LayoutManager getLayoutManager();
    int findLastVisiblePosition();
    void setUpAdapter(BaseListAdapter adapter);
}
