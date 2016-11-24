package com.ishow.ischool.adpter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.market.SummaryWeekly;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mini on 2016/11/23.
 */

public class WeeklyAdapter extends RecyclerView.Adapter<WeeklyAdapter.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private Context mContext;
    private SummaryWeekly mSummaryWeekly;
    private View mHeaderView;
    private SparseArray<Boolean> mCheckedSparseArray;

    public WeeklyAdapter(Context context, SummaryWeekly summaryWeekly) {
        this.mContext = context;
        this.mSummaryWeekly = summaryWeekly;
        mCheckedSparseArray = new SparseArray<>();
        if (mSummaryWeekly != null && mSummaryWeekly.table != null) {
            for (int i = 0; i < mSummaryWeekly.table.body.size(); i++) {
                mCheckedSparseArray.put(i, true);
            }
        }
    }

    public SparseArray<Boolean> getmCheckedSparseArray() {
        return mCheckedSparseArray;
    }

    public int getCheckedCount() {
        int count = 0;
        for (int i = 0; i < mCheckedSparseArray.size(); i++) {
            if (mCheckedSparseArray.get(i)) {
                count++;
            }
        }
        return count;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public WeeklyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ViewHolder(mHeaderView);
        }
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_weekly_summary, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(final WeeklyAdapter.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;

        final int realPosition = getRealPosition(holder);

        List<String> keyDatas = mSummaryWeekly.table.head;
        List<List<String>> valueDatas = mSummaryWeekly.table.body;
        holder.weekly_title.setText(valueDatas.get(realPosition).get(0));
        holder.item1_key.setText(keyDatas.get(1));
        holder.item1_value.setText(valueDatas.get(realPosition).get(1));
        holder.item2_key.setText(keyDatas.get(2));
        holder.item2_value.setText(valueDatas.get(realPosition).get(2));
        holder.item3_key.setText(keyDatas.get(3));
        holder.item3_value.setText(valueDatas.get(realPosition).get(3));
        holder.item4_key.setText(keyDatas.get(4));
        holder.item4_value.setText(valueDatas.get(realPosition).get(4));
        holder.item5_key.setText(keyDatas.get(5));
        holder.item5_value.setText(valueDatas.get(realPosition).get(5));

        holder.checkBox.setChecked(mCheckedSparseArray.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(realPosition, holder);
            }
        });
    }

    public int getRealPosition(ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return mSummaryWeekly == null || mSummaryWeekly.table == null || mSummaryWeekly.table.body == null ? 0 : mSummaryWeekly.table.body.size();
        } else {
            return mSummaryWeekly == null || mSummaryWeekly.table == null || mSummaryWeekly.table.body == null ? 1 : mSummaryWeekly.table.body.size() + 1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.weekly_title)
        TextView weekly_title;
        @BindView(R.id.item1_key)
        public TextView item1_key;
        @BindView(R.id.item1_value)
        TextView item1_value;
        @BindView(R.id.item2_key)
        TextView item2_key;
        @BindView(R.id.item2_value)
        TextView item2_value;
        @BindView(R.id.item3_key)
        TextView item3_key;
        @BindView(R.id.item3_value)
        TextView item3_value;
        @BindView(R.id.item4_key)
        TextView item4_key;
        @BindView(R.id.item4_value)
        TextView item4_value;
        @BindView(R.id.item5_key)
        TextView item5_key;
        @BindView(R.id.item5_value)
        TextView item5_value;
        @BindView(R.id.checkbox)
        public AppCompatCheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.onItemClick(getAdapterPosition(), this);
//                }
//            });
        }
    }


    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, RecyclerView.ViewHolder viewHolder);
    }


}
