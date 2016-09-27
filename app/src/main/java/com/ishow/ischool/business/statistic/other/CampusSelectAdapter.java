package com.ishow.ischool.business.statistic.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.user.CampusInfo;

import java.util.ArrayList;

/**
 * Created by wqf on 16/9/12.
 */
public class CampusSelectAdapter extends RecyclerView.Adapter<CampusSelectAdapter.ListItemViewHolder> {
    private ArrayList<CampusInfo> mList = new ArrayList<>();
    private boolean mIsSelectable = false;
    private Context mContext;
    private int curPosition;

    public CampusSelectAdapter(Context ctx, ArrayList<CampusInfo> list) {
        this.mContext = ctx;
        if (list == null) {
            throw new IllegalArgumentException("model Data must not be null");
        }
        mList = list;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_campus_item_other, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    //根据位置判断条目是否选中
    private boolean isItemChecked(int position) {
        return curPosition == position;
    }

    //根据位置判断条目是否可选
    private boolean isSelectable() {
        return mIsSelectable;
    }

    //设置给定位置条目的可选与否的状态
    private void setSelectable(boolean selectable) {
        mIsSelectable = selectable;
    }

    //绑定界面，设置监听
    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, final int position) {
        //设置条目状态
        holder.mainTitle.setText(mList.get(position).name);
        holder.checkBox.setChecked(isItemChecked(position));

        //条目view的监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curPosition = position;
                notifyDataSetChanged();
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curPosition = position;
                notifyDataSetChanged();
            }
        });
    }

    public int getCurPosition() {
        return curPosition;
    }

    public CampusInfo getCurCampusInfo() {
        return mList.get(curPosition);
    }

    public void setCurCampusInfo(int id) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).id == id) {
                curPosition = i;
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {
        //ViewHolder
        TextView mainTitle;
        RadioButton checkBox;

        ListItemViewHolder(View view) {
            super(view);
            this.mainTitle = (TextView) view.findViewById(R.id.campus_name);
            this.checkBox = (RadioButton) view.findViewById(R.id.select_checkbox);
        }
    }
}
