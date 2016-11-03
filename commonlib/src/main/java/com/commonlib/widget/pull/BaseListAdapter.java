package com.commonlib.widget.pull;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.commonlib.R;


/**
 * Created by wqf on 16/4/29.
 */
public abstract class BaseListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    protected static final int VIEW_TYPE_LOAD_MORE_LOADING = 100;       // 加载中视图type
    protected static final int VIEW_TYPE_LOAD_MORE_END = 101;           // 已显示全部内容视图type
    protected int loadMoreFooterState = ACTION_LOADMORE_HIDE;
    public static final int ACTION_LOADMORE_HIDE = 0;
    public static final int ACTION_LOADMORE_SHOW = 1;
    public static final int ACTION_LOADMORE_END = 2;

    public BaseListAdapter() {
        setHasStableIds(true);      //我们要重写getItemId方法给他一个不同位置的唯一标识，并且hasStableIds返回true的时候应该返回相同的数据集；
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOAD_MORE_LOADING || viewType == VIEW_TYPE_LOAD_MORE_END) {
            return onCreateLoadMoreFooterViewHolder(parent);
        }
        return onCreateNormalViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (loadMoreFooterState == ACTION_LOADMORE_SHOW && position == getItemCount() - 1) {
            //在grid和stagger模式下，footer要占满一行而不是一个span。所以grid需要SpanSizeLookup来动态改footer所占的spanCount。而stagger，需要将viewholder中的itemView的LayoutParams中isFullSpan设置为true。
            if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                params.setFullSpan(true);
            }
        }
        holder.onBindViewHolder(position);
    }

    @Override
    public int getItemCount() {
        return getDataCount() + ((loadMoreFooterState == ACTION_LOADMORE_SHOW || loadMoreFooterState == ACTION_LOADMORE_END) ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (loadMoreFooterState == ACTION_LOADMORE_SHOW && position == getItemCount() - 1) {
            return VIEW_TYPE_LOAD_MORE_LOADING;
        } else if (loadMoreFooterState == ACTION_LOADMORE_END && position == getItemCount() - 1) {
            return VIEW_TYPE_LOAD_MORE_END;
        }
        return getDataViewType(position);
    }

    protected abstract int getDataCount();

    protected abstract BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType);

    protected int getDataViewType(int position) {
        return 0;
    }

    public void onLoadMoreStateChanged(int loadMoreFooterState) {
        this.loadMoreFooterState = loadMoreFooterState;
        if (loadMoreFooterState == ACTION_LOADMORE_SHOW || loadMoreFooterState == ACTION_LOADMORE_END) {
            notifyItemInserted(getItemCount());
        } else {            //  else if (loadMoreFooterState == ACTION_LOADMORE_HIDE)
            notifyItemRemoved(getItemCount());
        }
    }

    /**
     * 用于Gridview
     * @param position
     * @return
     */
    public boolean isLoadMoreFooter(int position) {
        return loadMoreFooterState == ACTION_LOADMORE_SHOW && position == getItemCount() - 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public boolean isSectionHeader(int position) {
        return false;
    }


    //  -----------  RecyclerView底部Footer（加载中和"已显示全部内容"）  ------------  //
    protected BaseViewHolder onCreateLoadMoreFooterViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_pull_to_refresh_footer, parent, false);
        return new LoadMoreFooterViewHolder(view);
    }

    private class LoadMoreFooterViewHolder extends BaseViewHolder {

        public LoadMoreFooterViewHolder(View view) {
            super(view);
            LinearLayout loadMoreEndLayout = (LinearLayout) view.findViewById(R.id.end_layout);
            LinearLayout loadMoreLoadingLayout = (LinearLayout) view.findViewById(R.id.loading_layout);
            if (loadMoreFooterState == ACTION_LOADMORE_SHOW) {
                loadMoreLoadingLayout.setVisibility(View.VISIBLE);
                loadMoreEndLayout.setVisibility(View.GONE);
            } else if (loadMoreFooterState == ACTION_LOADMORE_END) {
                loadMoreEndLayout.setVisibility(View.VISIBLE);
                loadMoreLoadingLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onBindViewHolder(int position) {
            this.setIsRecyclable(false);    // 强制关闭复用，刷新动画
            // 设置自定义加载中和到底了效果
        }

        @Override
        public void onItemClick(View view, int position) {
            // 设置点击效果，比如加载失败，点击重试
        }
    }
}
