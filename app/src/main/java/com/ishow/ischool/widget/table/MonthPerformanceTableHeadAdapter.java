package com.ishow.ischool.widget.table;

import android.content.Context;
import android.view.View;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.campusperformance.MonthTableHead;

import java.util.List;

/**
 * Created by mini on 16/9/28.
 */

public class MonthPerformanceTableHeadAdapter extends MyLinearLayoutBaseAdapter<MonthTableHead> {

    public MonthPerformanceTableHeadAdapter(Context context, List<MonthTableHead> list) {
        super(context, list);
    }

    @Override
    View getView(int position) {
        View convertView = getLayoutInflater().inflate(R.layout.item_month_performance_table_head, null);
        MultiTextLabelHead tv = (MultiTextLabelHead) convertView.findViewById(R.id.tv);
        if (list.get(position).subtitle != null && list.get(position).subtitle.size() > 0) {
            tv.setTopTextStr(list.get(position).title);
            tv.setBottomTextStr1(list.get(position).subtitle.get(0));
            tv.setBottomTextStr2(list.get(position).subtitle.get(1));
            tv.setBottomTextStr3(list.get(position).subtitle.get(2));
        } else {
            tv.setCenterTextStr(list.get(position).title);
        }
        return convertView;
    }
}
