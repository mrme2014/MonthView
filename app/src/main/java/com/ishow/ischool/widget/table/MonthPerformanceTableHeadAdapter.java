package com.ishow.ischool.widget.table;

import android.content.Context;
import android.view.View;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.campusperformance.MonthTableHead;

import java.util.List;

import static com.ishow.ischool.R.id.tv;

/**
 * Created by mini on 16/9/28.
 */

public class MonthPerformanceTableHeadAdapter extends MyLinearLayoutBaseAdapter<MonthTableHead> {

    public MonthPerformanceTableHeadAdapter(Context context, List<MonthTableHead> list) {
        super(context, list);
    }

    @Override
    View getView(int position, View convertView) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.item_month_performance_table_head, null);
            viewHolder = new ViewHolder();
            viewHolder.tv = (MultiTextLabelHead) convertView.findViewById(tv);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (list.get(position).subtitle != null && list.get(position).subtitle.size() > 0) {
            viewHolder.tv.setTopTextStr(list.get(position).title);
            viewHolder.tv.setBottomTextStr1(list.get(position).subtitle.get(0));
            viewHolder.tv.setBottomTextStr2(list.get(position).subtitle.get(1));
            viewHolder.tv.setBottomTextStr3(list.get(position).subtitle.get(2));
        } else {
            viewHolder.tv.setCenterTextStr(list.get(position).title);
        }
        return convertView;
    }

    class ViewHolder {
        MultiTextLabelHead tv;
    }
}
