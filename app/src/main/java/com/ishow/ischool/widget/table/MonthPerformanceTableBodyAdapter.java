package com.ishow.ischool.widget.table;

import android.content.Context;
import android.view.View;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.campusperformance.MonthTableBodyItem;

import java.util.List;

/**
 * Created by mini on 16/9/28.
 */

public class MonthPerformanceTableBodyAdapter extends MyLinearLayoutBaseAdapter<MonthTableBodyItem> {

    public MonthPerformanceTableBodyAdapter(Context context, List<MonthTableBodyItem> list) {
        super(context, list);
    }

    @Override
    View getView(int position) {
        View convertView = getLayoutInflater().inflate(R.layout.item_month_performance_table_body, null);
        MultiTextLabelBody tv = (MultiTextLabelBody) convertView.findViewById(R.id.tv);
        if (list.get(position).size() > 1) {
            tv.setLeftextStr(list.get(position).get(0));
            tv.setCenterTextStr(list.get(position).get(1));
            tv.setRightTextStr(list.get(position).get(2));
        } else {
            tv.setCenterTextStr(list.get(position).get(0));
        }
        return convertView;
    }
}
