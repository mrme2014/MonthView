package com.commonlib.widget.pull;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.commonlib.R;
import com.commonlib.widget.pull.layoutmanager.ILayoutManager;


/**
 * Created by wqf on 16/4/29.
 */
public class PullRecycler extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayout emptyLayout;
    public static final int ACTION_PULL_TO_REFRESH = 1;
    public static final int ACTION_LOAD_MORE_LOADING = 2;
    public static final int ACTION_LOAD_MORE_END = 3;
    public static final int ACTION_IDLE = 0;
    private OnRecyclerRefreshListener listener;
    private int mCurrentState = ACTION_IDLE;
    private boolean isLoadMoreEnabled = false;
    private boolean isPullToRefreshEnabled = true;
    private ILayoutManager mLayoutManager;
    private BaseListAdapter adapter;

    public PullRecycler(Context context) {
        super(context);
        setUpView();
    }

    public PullRecycler(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpView();
    }

    public PullRecycler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpView();
    }

    private void setUpView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_pull_to_refresh, this, true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                R.color.colorAccent, R.color.colorAccent, R.color.colorAccent);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        mRecyclerView.setItemAnimator(new MyItemAnimator());
        emptyLayout = (LinearLayout) findViewById(R.id.empty_layout);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mCurrentState == ACTION_IDLE && isLoadMoreEnabled && checkIfNeedLoadMore()) {
                    mCurrentState = ACTION_LOAD_MORE_LOADING;
                    adapter.onLoadMoreStateChanged(BaseListAdapter.ACTION_LOADMORE_SHOW);
                    mSwipeRefreshLayout.setEnabled(false);
                    listener.onRefresh(ACTION_LOAD_MORE_LOADING);
                }
            }
        });
    }

    /**
     * 距离还剩1个时刷新
     */
    private boolean checkIfNeedLoadMore() {
        int lastVisibleItemPosition = mLayoutManager.findLastVisiblePosition();
        int totalCount = mLayoutManager.getLayoutManager().getItemCount();
        return totalCount - lastVisibleItemPosition < 2;
    }

    public void enableLoadMore(boolean enable) {
        isLoadMoreEnabled = enable;
        if (emptyLayout.getVisibility() != GONE) {       // 避免上一次加载为空而本次加载有数据的时候，及时隐藏emptyLayout
            emptyLayout.setVisibility(GONE);
        }
    }

    public void enablePullToRefresh(boolean enable) {
        isPullToRefreshEnabled = enable;
        mSwipeRefreshLayout.setEnabled(enable);
    }

    public void setLayoutManager(ILayoutManager manager) {
        this.mLayoutManager = manager;
        mRecyclerView.setLayoutManager(manager.getLayoutManager());
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decoration) {
        if (decoration != null) {
            mRecyclerView.addItemDecoration(decoration);
        }
    }

    public void setAdapter(BaseListAdapter adapter) {
        this.adapter = adapter;
        mRecyclerView.setAdapter(adapter);
        mLayoutManager.setUpAdapter(adapter);
    }

    public void setRefreshing() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    public void setOnRefreshListener(OnRecyclerRefreshListener listener) {
        this.listener = listener;
    }

    @Override
    public void onRefresh() {
        mCurrentState = ACTION_PULL_TO_REFRESH;
        listener.onRefresh(ACTION_PULL_TO_REFRESH);
    }

    public void onRefreshCompleted() {
        switch (mCurrentState) {
            case ACTION_PULL_TO_REFRESH:
                mSwipeRefreshLayout.setRefreshing(false);
                mCurrentState = ACTION_IDLE;
                break;
            case ACTION_LOAD_MORE_LOADING:
                adapter.onLoadMoreStateChanged(BaseListAdapter.ACTION_LOADMORE_HIDE);
//                if (isPullToRefreshEnabled) {
                    mSwipeRefreshLayout.setEnabled(true);
//                }
                mCurrentState = ACTION_IDLE;
                break;
            case ACTION_LOAD_MORE_END:
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                adapter.onLoadMoreStateChanged(BaseListAdapter.ACTION_LOADMORE_END);
                mSwipeRefreshLayout.setEnabled(true);
                break;
        }
    }

    public void setOnLoadMoreEnd() {
        mCurrentState = ACTION_LOAD_MORE_END;
    }

    public void setSelection(int position) {
        mRecyclerView.scrollToPosition(position);
    }

    public interface OnRecyclerRefreshListener {
        void onRefresh(int action);
    }

    public void setEmptyView4Icon(int resId) {
        ImageView emptyIcon = (ImageView) emptyLayout.findViewById(R.id.empty_iv);
        emptyIcon.setImageResource(resId);
    }

    public void showEmptyView() {
        emptyLayout.setVisibility(VISIBLE);
    }
}
