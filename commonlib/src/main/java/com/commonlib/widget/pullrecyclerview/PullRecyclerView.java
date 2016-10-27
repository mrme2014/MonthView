package com.commonlib.widget.pullrecyclerview;


import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonlib.R;
import com.commonlib.util.LogUtil;

import java.util.ArrayList;

import static com.commonlib.widget.pullrecyclerview.PullRecyclerViewAdapter.ACTION_LOADMORE_END;


/**
 * Created by abel on 16/10/10.
 */

public class PullRecyclerView<T> extends SwipeRefreshLayout {

    public static final int ACTION_PULL_TO_REFRESH = 1;
    public static final int ACTION_LOAD_MORE_LOADING = 2;
    public static final int ACTION_LOAD_MORE_NO_MORE_DATA = 3;
    public static final int ACTION_IDLE = 0;
    private static final String TAG = "PullRecyclerView";

    public static final int MODE_PULL_DOWN = 1;
    public static final int MODE_PULL_UP = 2;
    public static final int MODE_BOTH = 3;
    public static final int MODE_NONE = 0;

    RecyclerView mRecyclerView;
    private LinearLayout mEmptyLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private int mCurrentState;
    private PullRecyclerViewAdapter mAdapter;

    private boolean isLoadMoreEnable = true;
    private boolean isNoMoreDataEnable = true;

    private OnRefreshListener1 mOnRefreshListener;
    private int mode = MODE_BOTH;
    private int pageSize = 20;

    public PullRecyclerView(Context context) {
        super(context);
        initView();
    }


    public PullRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_pull_recyclerview, this, true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mEmptyLayout = (LinearLayout) findViewById(R.id.empty_layout);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //LogUtil.d("onScrollStateChanged newState=" + newState + " Thread=" + Thread.currentThread().getId());
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LogUtil.d("onScrolled " + dx + " " + dy + " mCurrentState=" + mCurrentState);
                if (mCurrentState == ACTION_IDLE && (mode & MODE_PULL_UP) != 0 && isLoadMoreEnable && checkIfNeedLoadMore()) {

                    mCurrentState = ACTION_LOAD_MORE_LOADING;
                    mAdapter.onLoadMoreLoading();

                    setEnabled(false);
                    LogUtil.d("onScrolled mCurrentState=" + mCurrentState);
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onRefreshMore();
                    }
                }
            }
        });

        setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
    }

    //计算数据是否已经全部加载

    public void setAdapter(PullRecyclerViewAdapter adapter) {
        this.mAdapter = adapter;
        mRecyclerView.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        this.mLayoutManager = manager;
        mRecyclerView.setLayoutManager(manager);
    }

    /**
     * 距离还剩1个时刷新
     */
    private boolean checkIfNeedLoadMore() {
        if (mLayoutManager instanceof LinearLayoutManager) {
            int lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            int totalCount = mLayoutManager.getItemCount();
            return totalCount - lastVisibleItemPosition < 2;
        }
        return false;
    }

    public void setEmptyViewIcon(int resId) {
        ImageView emptyIcon = (ImageView) mEmptyLayout.findViewById(R.id.empty_iv);
        emptyIcon.setImageResource(resId);
    }

    public void setEmptyViewText(int resId) {
        TextView emptyText = (TextView) mEmptyLayout.findViewById(R.id.empty_tv);
        emptyText.setText(resId);
    }

    public void showEmptyView() {
        mEmptyLayout.setVisibility(VISIBLE);
    }

    public void resetView() {
        mEmptyLayout.setVisibility(GONE);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setOnRefreshListener1(OnRefreshListener1 listener) {
        mOnRefreshListener = listener;
        setOnRefreshListener(listener);
    }

    public void set(int mode, int pageSize) {
        setMode(mode);
        setPageSize(pageSize);
    }

    public void computerLoadMore(int pageSize, int dataSize) {
        if (dataSize == pageSize && (mode & MODE_PULL_UP) != 0) {
            isLoadMoreEnable = true;
        } else if (dataSize < pageSize && isNoMoreDataEnable) {
            mAdapter.onLoadMoreStateChanged(ACTION_LOADMORE_END);
        } else {
            isLoadMoreEnable = false;
        }
    }

    public int setPageData(ArrayList<T> datas, int page) {
        if (page <= 1) {
            if (datas == null || datas.isEmpty()) {
                showEmptyView();
            } else {
                setData(datas);
            }
        } else {
            if (datas == null || datas.isEmpty()) {//加载跟多，但是无数据
                //页数减一
                page--;
                //显示没有更多数据了
                if (isNoMoreDataEnable) {
                    mAdapter.onLoadMoreStateChanged(ACTION_LOADMORE_END);
                }
            } else {
                addData(datas);
            }
        }
        return page;
    }

    public void setData(ArrayList<T> datas) {
        computerLoadMore(pageSize, datas.size());
        mAdapter.setData(datas, false);
    }

    public void addData(ArrayList<T> datas) {
        computerLoadMore(pageSize, datas.size());
        mAdapter.addData(datas, false);
    }

    public void setMode(int mode) {
        this.mode = mode;
        switch (mode) {
            case MODE_NONE:
                setEnabled(false);
                isLoadMoreEnable = false;
                isNoMoreDataEnable = false;
                break;
            case MODE_PULL_DOWN:
                setEnabled(true);
                break;
            case MODE_PULL_UP:
                isLoadMoreEnable = true;
                break;
            case MODE_BOTH:
                setEnabled(true);
                isLoadMoreEnable = true;
                isNoMoreDataEnable = true;
                break;

        }

    }

    public void setNoMoreDataEnable(boolean noMoreDataEnable) {
        isNoMoreDataEnable = noMoreDataEnable;
    }

    public void setNoMoreData() {
        mCurrentState = ACTION_IDLE;
        isLoadMoreEnable = false;
        mAdapter.onLoadNoMoreData();
        setEnabled(true);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public interface OnRefreshListener1 extends SwipeRefreshLayout.OnRefreshListener {

        void onRefreshMore();
    }

    /**
     * 刷新状态，加载更多状态
     */
    @Override
    public void setRefreshing(boolean refreshing) {
        super.setRefreshing(refreshing);
        if (refreshing) {
            mCurrentState = ACTION_PULL_TO_REFRESH;
        } else {
            setEnabled(true);
            if (mCurrentState == ACTION_LOAD_MORE_LOADING) {
                mAdapter.onLoadMoreCompleted();
            }
            mCurrentState = ACTION_IDLE;
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener lisnter) {
        if (mAdapter != null) {
            mAdapter.setOnItemClickListener(lisnter);
        }
    }
}
