package com.ishow.ischool.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.classes.ClassTimeSlot;

import java.util.List;

/**
 * Created by wqf on 16/10/24.
 */
public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private Context mContext;
    private List<ClassTimeSlot> datas;

    public TimeSlotAdapter(Context context, List<ClassTimeSlot> datas) {
        this.mContext = context;
        this.datas = datas;
    }

    @Override
    public TimeSlotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_time_slot, parent, false);
        return new TimeSlotAdapter.TimeSlotViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TimeSlotViewHolder holder, int position) {
        holder.itemTv.setText(mContext.getString(R.string.time_slot, parseToWeekday(datas.get(position).week),
                datas.get(position).start_time, datas.get(position).end_time));
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        TextView itemTv;
        View itemView;

        public TimeSlotViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemTv = (TextView) itemView.findViewById(R.id.class_time);
        }
    }


    public String parseToWeekday(int i) {
        switch (i) {
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            case 7:
                return "日";
        }
        return "";
    }
}
