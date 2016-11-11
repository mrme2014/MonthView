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
import java.util.List;

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
        recycler.setRefreshing();       // 进入即加载，无需操作关于recycler的刷新与停止刷新操作，只关注于数据
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
     * 自定义分隔线
     *
     * @return
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
            return getDataCounts();
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

    protected int getDataCounts() {
        return mDataList != null ? mDataList.size() : 0;
    }

    protected int getItemType(int position) {
        return 0;
    }

    protected boolean isSectionHeader(int position) {
        return false;
    }

    protected abstract BaseViewHolder getViewHolder(ViewGroup parent, int viewType);


    /**
     * 数据获取成功时，对recycleview状态、mCurrentState以及adapter的实时刷新
     *
     * @param resultList
     */
    protected void loadSuccess(List<T> resultList) {
        if (mCurrentPage == 1) {        // 不支持分页（mCurrentPage没有使用时，就是没有作分页处理，即mCurrentPage一直等于1）
            recycler.mPageEnable = false;
        }

        recycler.resetView();
//        if (!recycler.mPageEnable) {            // 如果不支持分页，第一次加载之后就关闭下拉刷新
//            recycler.enablePullToRefresh(false);
//        }

        if (recycler.mCurrentState == PullRecycler.ACTION_PULL_TO_REFRESH) {      // 下拉刷新，重置数据
            mDataList.clear();
            recycler.smoothToTop();
        }

        if (resultList == null || resultList.size() == 0) {
            recycler.enableLoadMore(false);         // 数据为空时，屏蔽加载更多功能
            if (recycler.mPageEnable && mCurrentPage > 2) {     // 非第一页并且支持分页时
                recycler.setLoadState(PullRecycler.ACTION_LOAD_MORE_END);       // 加载到底（没数据了）
            } else {                                            // 当不支持分页或者第一页时（即mCurrentPage=2，因为mCurrentPage++）。
                recycler.showEmptyView();
            }
        } else {
            if (recycler.mPageEnable) {         // 如果支持分页
                if (resultList.size() < Conf.DEFAULT_PAGESIZE_LISTVIEW) {     // 已经是最后一页了
                    recycler.enableLoadMore(false);
                    recycler.setLoadState(PullRecycler.ACTION_LOAD_MORE_END);
                } else {
                    recycler.enableLoadMore(true);
                }
            } else {
                recycler.setLoadState(PullRecycler.ACTION_LOAD_MORE_END);
            }
            mDataList.addAll(resultList);
            mAdapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    /**
     * 数据获取失败时，对recycleview状态、mCurrentState以及adapter的实时刷新
     */
    protected void loadFailed() {
        if (mCurrentPage == 1) {        // 不支持分页（mCurrentPage没有使用时，就是没有作分页处理，即mCurrentPage一直等于1）
            recycler.mPageEnable = false;
        }

        if (mCurrentPage > 1) {             // 数据请求失败，mCurrentPage减1
            mCurrentPage--;
        }

        if (mCurrentPage == 1) {            // 没有数据，清除数据（列表上次有数据的情况）并显示空白占位
            mDataList.clear();
            mAdapter.notifyDataSetChanged();
            recycler.enableLoadMore(false);
            recycler.setLoadState(PullRecycler.ACTION_IDLE);
            recycler.showEmptyView();
        }
        recycler.onRefreshCompleted();
    }
}
