package monthview.ishow.com.monthview.pull;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import monthview.ishow.com.monthview.R;
import monthview.ishow.com.monthview.pull.layoutmanager.ILayoutManager;


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
    public int mCurrentState = ACTION_IDLE;
    private boolean isLoadMoreEnabled = false;
    private boolean isPullToRefreshEnabled = true;
    private ILayoutManager mLayoutManager;
    private BaseListAdapter adapter;
    public boolean mPageEnable = true;      // 是否支持分页

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
        mSwipeRefreshLayout.setColorSchemeResources(R.color.comm_blue,
                R.color.comm_blue, R.color.comm_blue, R.color.comm_blue);
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
                if (mPageEnable) {      // 判断是否支持分页，当不支持时，直接过滤加载更多
                    if (mCurrentState == ACTION_IDLE && isLoadMoreEnabled && checkIfNeedLoadMore()) {
                        mCurrentState = ACTION_LOAD_MORE_LOADING;
                        adapter.onLoadMoreStateChanged(BaseListAdapter.ACTION_LOADMORE_SHOW);
                        mSwipeRefreshLayout.setEnabled(false);              // 加载更多时，关闭下拉刷新
                        listener.onRefresh(ACTION_LOAD_MORE_LOADING);
                    }
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

    public void setRefreshingMainThread() {
        mSwipeRefreshLayout.setRefreshing(true);
        onRefresh();
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
        if (!isPullToRefreshEnabled) {          // 更新状态：是否支持下拉刷新
            mSwipeRefreshLayout.setEnabled(false);
        } else {
            mSwipeRefreshLayout.setEnabled(true);
        }
        if (!mPageEnable) {
//            enablePullToRefresh(false);         // 如果不支持分页，禁止下拉刷新
            enableLoadMore(false);               // 如果不支持分页，禁止自动加载更多
        }
        switch (mCurrentState) {
            case ACTION_PULL_TO_REFRESH:
                mSwipeRefreshLayout.setRefreshing(false);
                mCurrentState = ACTION_IDLE;
                break;
            case ACTION_LOAD_MORE_LOADING:
                adapter.onLoadMoreStateChanged(BaseListAdapter.ACTION_LOADMORE_HIDE);
                mCurrentState = ACTION_IDLE;
                break;
            case ACTION_LOAD_MORE_END:
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                adapter.onLoadMoreStateChanged(BaseListAdapter.ACTION_LOADMORE_END);
                break;
            case ACTION_IDLE:       // 第一次有数据，经过筛选等操作后数据为空时（loadfail()）
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                adapter.onLoadMoreStateChanged(BaseListAdapter.ACTION_LOADMORE_HIDE);
                break;
        }
    }

    /**
     * 设置当前状态
     * 0.ACTION_IDLE：               空白状态（即没有数据）
     * 1.ACTION_PULL_TO_REFRESH：    下拉刷新中
     * 2.ACTION_LOAD_MORE_LOADING    上滑加载中
     * 3.ACTION_LOAD_MORE_END        没有更多（显示）状态
     */
    public void setLoadState(int state) {
        mCurrentState = state;
    }

    public void setSelection(int position) {
        mRecyclerView.scrollToPosition(position);
    }

    public interface OnRecyclerRefreshListener {
        void onRefresh(int action);
    }

    /**
     * @param resId 设置数据为空时占位图
     */
    public void setEmptyView4Icon(int resId) {
        ImageView emptyIcon = (ImageView) emptyLayout.findViewById(R.id.empty_iv);
        emptyIcon.setImageResource(resId);
    }

    /**
     * 显示数据为空时的占位图
     */
    public void showEmptyView() {
        emptyLayout.setVisibility(VISIBLE);
    }

    public void resetView() {
        emptyLayout.setVisibility(GONE);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * recycleview滑到顶部
     */
    public void smoothToTop() {
//        mRecyclerView.scrollToPosition(0);
//        mRecyclerView.smoothScrollToPosition(0);
    }


    /**
     * 隐藏列表下方"已显示全部内容"
     */
    public void hideLoadMoreFooter() {
        adapter.onLoadMoreStateChanged(BaseListAdapter.ACTION_LOADMORE_HIDE);
    }
}
