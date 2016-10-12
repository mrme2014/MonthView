package com.commonlib.widget.pullrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.commonlib.R;
import com.commonlib.util.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by abel on 16/10/10.
 */

public abstract class PullRecyclerViewAdapter<T> extends RecyclerView.Adapter<PullViewHolder> {

    protected static final int VIEW_TYPE_LOAD_MORE_LOADING = 100;       // 加载中视图type
    protected static final int VIEW_TYPE_LOAD_MORE_END = 101;           // 已显示全部内容视图type

    public static final int ACTION_LOADMORE_HIDE = 0;
    public static final int ACTION_LOADMORE_LOADING = 1;
    public static final int ACTION_LOADMORE_END = 2;

    //获取从Activity中传递过来每个item的数据集合
    public List<T> mDatas;
    private int mState = ACTION_LOADMORE_HIDE;

    @Override
    public PullViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOAD_MORE_LOADING || viewType == VIEW_TYPE_LOAD_MORE_END) {
            return onCreateLoadMoreFooterViewHolder(parent);
        }
        return onCreateItemViewHolder(parent, viewType);
    }

    public abstract PullViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);

    @Override
    public int getItemCount() {
        return getDataCount() + ((mState == ACTION_LOADMORE_LOADING || mState == ACTION_LOADMORE_END) ? 1 : 0);
    }

    private int getDataCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * RecyclerView底部Footer（加载中和"已显示全部内容")
     *
     * @param parent
     * @return
     */
    protected PullViewHolder onCreateLoadMoreFooterViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_pull_to_refresh_footer, parent, false);
        return new LoadMoreFooterViewHolder(view);
    }

    public void setData(ArrayList<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<T> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mState == ACTION_LOADMORE_LOADING && position == getItemCount() - 1) {
            return VIEW_TYPE_LOAD_MORE_LOADING;
        } else if (mState == ACTION_LOADMORE_END && position == getItemCount() - 1) {
            return VIEW_TYPE_LOAD_MORE_END;
        }
        return getDataViewType(position);
    }

    public int getDataViewType(int position) {
        return 0;
    }

    public void onLoadMoreStateChanged(int actionLoadmoreShow) {
        mState = actionLoadmoreShow;
    }

    public void onLoadMoreLoading() {
        mState = ACTION_LOADMORE_LOADING;
    }

    public void onLoadMoreCompleted() {
        mState = ACTION_LOADMORE_HIDE;
    }

    public void onLoadNoMoreData() {
        mState = ACTION_LOADMORE_END;
        notifyItemChanged(getItemCount() - 1);
        LogUtil.d("onLoadNoMoreData");
    }

    private class LoadMoreFooterViewHolder extends PullViewHolder {

        public LoadMoreFooterViewHolder(View view) {
            super(view);
            LinearLayout loadMoreEndLayout = (LinearLayout) view.findViewById(R.id.end_layout);
            LinearLayout loadMoreLoadingLayout = (LinearLayout) view.findViewById(R.id.loading_layout);
            if (mState == ACTION_LOADMORE_LOADING) {
                loadMoreLoadingLayout.setVisibility(View.VISIBLE);
                loadMoreEndLayout.setVisibility(View.GONE);
            } else if (mState == ACTION_LOADMORE_END) {
                loadMoreEndLayout.setVisibility(View.VISIBLE);
                loadMoreLoadingLayout.setVisibility(View.GONE);
            }
        }
    }
}
