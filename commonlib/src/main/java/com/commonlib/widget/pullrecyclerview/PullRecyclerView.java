package com.commonlib.widget.pullrecyclerview;


import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonlib.R;
import com.commonlib.util.LogUtil;


/**
 * Created by abel on 16/10/10.
 */

public class PullRecyclerView extends SwipeRefreshLayout {

    //    public static final int ACTION_PULL_TO_REFRESH = 1;
    public static final int ACTION_LOAD_MORE_LOADING = 2;
    //    public static final int ACTION_LOAD_MORE_END = 3;
    public static final int ACTION_IDLE = 0;
    private static final String TAG = "PullRecyclerView";

    RecyclerView mRecyclerView;
    private LinearLayout mEmptyLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private int mCurrentState;
    private PullRecyclerViewAdapter mAdapter;

    private boolean isLoadMoreEnabled = true;

    private OnRefreshListener1 mOnRefreshListener;

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
                LogUtil.d("onScrollStateChanged newState=" + newState + " Thread=" + Thread.currentThread().getId());
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LogUtil.d("onScrolled " + dx + " " + dy + " mCurrentState=" + mCurrentState + " Thread=" + Thread.currentThread().getId());
                if (mCurrentState == ACTION_IDLE && isLoadMoreEnabled && checkIfNeedLoadMore()) {

                    mCurrentState = ACTION_LOAD_MORE_LOADING;
                    mAdapter.onLoadMoreLoading();

                    setEnabled(false);
                    LogUtil.d("onScrolled mCurrentState=" + mCurrentState);
                    mOnRefreshListener.onRefreshMore();
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

    public void setLoadMoreEnabled(boolean loadMoreEnabled) {
        isLoadMoreEnabled = loadMoreEnabled;
    }

    public void setNoMoreData() {
        mCurrentState = ACTION_IDLE;
        isLoadMoreEnabled = false;
        mAdapter.onLoadNoMoreData();
        setEnabled(true);
    }

    public interface OnRefreshListener1 extends SwipeRefreshLayout.OnRefreshListener {

        void onRefreshMore();
    }

    public void setRefreshCompleted() {
        setEnabled(true);
        if (mCurrentState == ACTION_LOAD_MORE_LOADING) {
            mCurrentState = ACTION_IDLE;
            mAdapter.onLoadMoreCompleted();
        } else {
            setRefreshing(false);
        }
    }
}
