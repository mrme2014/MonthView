package monthview.ishow.com.monthview.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import monthview.ishow.com.monthview.R;
import monthview.ishow.com.monthview.pull.BaseListAdapter;
import monthview.ishow.com.monthview.pull.BaseViewHolder;
import monthview.ishow.com.monthview.pull.DividerItemDecoration;
import monthview.ishow.com.monthview.pull.PullRecycler;
import monthview.ishow.com.monthview.pull.layoutmanager.ILayoutManager;
import monthview.ishow.com.monthview.pull.layoutmanager.MyLinearLayoutManager;

/**
 * Created by MrS on 2016/12/14.
 */

public abstract class BaseListDilaogFragment<T> extends BaseDialogFragment implements PullRecycler.OnRecyclerRefreshListener {
    protected PullRecycler recycler;
    protected ListAdapter mAdapter;
    protected ArrayList<T> mDataList;
    protected int mCurrentPage = 1;

    @Override
    protected int getDialogTheme() {
        return Theme.SLIDE_FROM_TO_BOTTOM_THEME;
    }

    @Override
    protected int getDialogLayoutId() {
        return R.layout.fragment_base_list;
    }

    @Override
    protected void onDialogCreate() {
        recycler = (PullRecycler) rootView.findViewById(R.id.pullRecycler);
        recycler.setOnRefreshListener(this);
        recycler.setLayoutManager(getLayoutManager());
        recycler.addItemDecoration(getItemDecoration());
        setLoadMoreEnable(false);
        setRefreshEnable(false);
        setUpAdapter();
        recycler.setAdapter(mAdapter);
    }

    protected void setLoadMoreEnable(boolean enable) {
        recycler.enableLoadMore(enable);
    }

    protected void setRefreshEnable(boolean enable) {
        recycler.enablePullToRefresh(enable);
    }

    protected void setUpAdapter() {
        mAdapter = new ListAdapter();
    }

    protected ILayoutManager getLayoutManager() {
        return new MyLinearLayoutManager(getContext());
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getContext(), R.drawable.widget_list_divider);
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
            return isBaseSectionHeader(position);
        }
    }

    protected boolean isBaseSectionHeader(int position) {
        return false;
    }

    protected int getItemType(int position) {
        return 0;
    }

    protected int getDataCounts() {
        return mDataList != null ? mDataList.size() : 0;
    }

    protected abstract BaseViewHolder getViewHolder(ViewGroup parent, int viewType);


    public void loadSuccess(ArrayList<T> resultList) {
        if (mDataList == null) mDataList = new ArrayList<>();

        if (mCurrentPage == 1) {        // 不支持分页（mCurrentPage没有使用时，就是没有作分页处理，即mCurrentPage一直等于1）
            recycler.mPageEnable = false;
        }

        recycler.resetView();


        if (recycler.mCurrentState == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }

        if (resultList == null || resultList.size() == 0) {
            recycler.enableLoadMore(false);         // 数据为空时，屏蔽加载更多功能
            if (recycler.mPageEnable && mCurrentPage > 2) {     // 非第一页并且支持分页时
                recycler.setLoadState(PullRecycler.ACTION_LOAD_MORE_END);       // 加载到底（没数据了）
            } else {                                            // 当不支持分页或者第一页时（即mCurrentPage=2，因为mCurrentPage++）。
                recycler.showEmptyView();
            }
        } else {
            if (recycler.mPageEnable) {     // 支持分页
                if (resultList.size() < 20) {     // 已经是最后一页了
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

    public void loadFailed() {
        if (mCurrentPage == 1) {        // 不支持分页（mCurrentPage没有使用时，就是没有作分页处理，即mCurrentPage一直等于1）
            recycler.mPageEnable = false;
        }

        if (mCurrentPage > 1) {
            mCurrentPage--;
        }

        if (mCurrentPage == 1) {            // 没有数据，清楚数据（列表上次有数据的情况）并显示空白占位
            if (mDataList != null) mDataList.clear();
            mAdapter.notifyDataSetChanged();
            recycler.enableLoadMore(false);
            recycler.setLoadState(PullRecycler.ACTION_IDLE);
            recycler.showEmptyView();
        }
        recycler.onRefreshCompleted();
    }

    protected void setRefreshing() {
        if (recycler != null) recycler.setRefreshingMainThread();
    }

    @Override
    public void onRefresh(int action) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mCurrentPage = 1;
        } else {
            mCurrentPage++;
        }
        onListRefresh(action);
    }

    protected abstract void onListRefresh(int action);
}
