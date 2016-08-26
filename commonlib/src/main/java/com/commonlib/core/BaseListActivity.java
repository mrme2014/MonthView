package com.commonlib.core;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.commonlib.Conf;
import com.commonlib.R;
import com.commonlib.widget.pull.BaseListAdapter;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.DividerItemDecoration;
import com.commonlib.widget.pull.PullRecycler;
import com.commonlib.widget.pull.layoutmanager.ILayoutManager;
import com.commonlib.widget.pull.layoutmanager.MyLinearLayoutManager;

import java.util.ArrayList;

/**
 * Created by wqf on 16/4/29.
 */
public abstract class BaseListActivity<P extends BasePresenter, M extends BaseModel, T> extends BaseActivity<P, M> implements PullRecycler.OnRecyclerRefreshListener {
    protected BaseListAdapter mAdapter;
    protected ArrayList<T> mDataList = new ArrayList<>();
    protected PullRecycler recycler;
    protected int mCurrentPage = 1;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_base_list, -1);
    }

    @Override
    protected void setUpView() {
        recycler = (PullRecycler) findViewById(R.id.pullRecycler);
        recycler.mPageEnable = setPageEnable();
    }

    /**
     * 设置该页面不支持分页加载,默认支持分页
     *
     * @return
     */
    protected boolean setPageEnable() {
        return true;
    }

    @Override
    protected void setUpData() {
        setUpAdapter();
        recycler.setOnRefreshListener(this);
        recycler.setLayoutManager(getLayoutManager());
        recycler.addItemDecoration(getItemDecoration());
        recycler.setAdapter(mAdapter);
        recycler.setRefreshing();
    }

    protected void setRefreshing() {
        recycler.setRefreshingMainThread();
    }

    protected void setUpAdapter() {
        mAdapter = new ListAdapter();
    }

    protected ILayoutManager getLayoutManager() {
        return new MyLinearLayoutManager(getApplicationContext());
    }


    /**
     * @return 分隔线样式
     */
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getApplicationContext(), R.drawable.widget_list_divider);
    }

    public class ListAdapter extends BaseListAdapter {

        @Override
        protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
            return getViewHolder(parent, viewType);
        }

        @Override
        protected int getDataCount() {
            return mDataList != null ? mDataList.size() : 0;
        }

        @Override
        protected int getDataViewType(int position) {
            return getItemType(position);
        }

        @Override
        public boolean isSectionHeader(int position) {
            return BaseListActivity.this.isSectionHeader(position);
        }
    }

    protected boolean isSectionHeader(int position) {
        return false;
    }

    protected int getItemType(int position) {
        return 0;
    }

    protected abstract BaseViewHolder getViewHolder(ViewGroup parent, int viewType);


    protected void loadSuccess(ArrayList<T> resultList) {
        recycler.resetView();
        if (!recycler.mPageEnable) {            // 如果不支持分页，第一次加载之后就关闭下拉刷新
            recycler.enablePullToRefresh(false);
        }

        if (recycler.mCurrentState == PullRecycler.ACTION_PULL_TO_REFRESH || mCurrentPage <= 1) {
            mDataList.clear();
        }

        if (resultList == null || resultList.size() == 0) {
            if (recycler.mPageEnable) {     // 支持分页
                if (mCurrentPage > 2) {      // 非第一页
                    recycler.enableLoadMore(false);
                    recycler.setOnLoadMoreEnd();
                } else {        // 当curPage=2时，其实是第一页数据。showNoDataView
                    recycler.showEmptyView();
                }
            } else {
                recycler.showEmptyView();
            }
        } else {
            if (recycler.mPageEnable) {     // 支持分页
                if (resultList.size() < Conf.DEFAULT_PAGESIZE_LISTVIEW) {     // 已经是最后一页了
                    recycler.enableLoadMore(false);
                    recycler.setOnLoadMoreEnd();
                } else {
                    recycler.enableLoadMore(true);
                }
            }
            mDataList.addAll(resultList);
            mAdapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    protected void loadFailed() {
        if (!recycler.mPageEnable) {        // 如果不支持分页，第一次加载之后就关闭下拉刷新
            recycler.enablePullToRefresh(false);
        }

        recycler.onRefreshCompleted();
        if (mCurrentPage > 1) {
            mCurrentPage--;
        }
        if (mCurrentPage == 1) {
            recycler.showEmptyView();
        }
    }
}
