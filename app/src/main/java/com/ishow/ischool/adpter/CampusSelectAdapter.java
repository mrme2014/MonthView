package com.ishow.ischool.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.user.CampusInfo;

import java.util.ArrayList;

/**
 * Created by wqf on 16/9/12.
 */
public class CampusSelectAdapter extends RecyclerView.Adapter<CampusSelectAdapter.ListItemViewHolder> {
    private ArrayList<CampusInfo> mList = new ArrayList<>();
    private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
    private boolean mIsSelectable = false;
    private Context mContext;

    public CampusSelectAdapter(Context ctx, ArrayList<CampusInfo> list) {
        this.mContext = ctx;
        if (list == null) {
            throw new IllegalArgumentException("model Data must not be null");
        }
        mList = list;
    }

    //更新adpter的数据和选择状态
    public void updateDataSet(ArrayList<CampusInfo> list) {
        this.mList = list;
        mSelectedPositions = new SparseBooleanArray();
    }

    /**
     * 清除所有选中item的标记
     */
    public void clearAllItems() {
        mSelectedPositions.clear();
        for (int i = 0; i < getItemCount(); i++) {
            mSelectedPositions.put(i, false);
        }
        notifyDataSetChanged();
    }

    /**
     * 全选
     */
    public void selectAllItems() {
        clearAllItems();
        for (int i = 0; i < getItemCount(); i++) {
            mSelectedPositions.put(i, true);
        }
        notifyDataSetChanged();
    }

    //获得选中条目的结果(校区id)
    public ArrayList<CampusInfo> getSelectedItem() {
        ArrayList<CampusInfo> selectList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            if (isItemChecked(i)) {
                selectList.add(mList.get(i));
            }
        }
        return selectList;
    }

    //获得选中的条目
    public ArrayList<Integer> getSelectedPosition() {
        ArrayList<Integer> selectList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            if (isItemChecked(i)) {
                selectList.add(i);
            }
        }
        return selectList;
    }


    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_campus_item, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    //设置给定位置条目的选择状态
    private void setItemChecked(int position, boolean isChecked) {
        mSelectedPositions.put(position, isChecked);
    }

    //根据位置判断条目是否选中
    private boolean isItemChecked(int position) {
        return mSelectedPositions.get(position);
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
    public void onBindViewHolder(final ListItemViewHolder holder, final int i) {
        //设置条目状态
        holder.mainTitle.setText(mList.get(i).name);
        holder.checkBox.setChecked(isItemChecked(i));

        //checkBox的监听
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isItemChecked(i)) {
                    setItemChecked(i, false);
                } else {
                    setItemChecked(i, true);
                }
            }
        });

        //条目view的监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isItemChecked(i)) {
                    setItemChecked(i, false);
                } else {
                    setItemChecked(i, true);
                }
                notifyItemChanged(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {
        //ViewHolder
        TextView mainTitle;
        CheckBox checkBox;

        ListItemViewHolder(View view) {
            super(view);
            this.mainTitle = (TextView) view.findViewById(R.id.campus_name);
            this.checkBox = (CheckBox) view.findViewById(R.id.select_checkbox);
        }
    }
}
