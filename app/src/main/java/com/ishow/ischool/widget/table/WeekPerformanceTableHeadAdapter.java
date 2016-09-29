package com.ishow.ischool.widget.table;

import android.content.Context;
import android.view.View;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.campusperformance.WeekTableHead;

import java.util.List;

/**
 * Created by mini on 16/9/28.
 */

public class WeekPerformanceTableHeadAdapter extends MyLinearLayoutBaseAdapter<WeekTableHead> {

    public WeekPerformanceTableHeadAdapter(Context context, List<WeekTableHead> list) {
        super(context, list);
    }

    @Override
    View getView(int position) {
        View convertView = getLayoutInflater().inflate(R.layout.item_month_performance_table_head, null);
        MultiTextLabelHead tv = (MultiTextLabelHead) convertView.findViewById(R.id.tv);
//        tv.setTopTextStr(list.get(position).title + "(" + list.get(position).date + ")");
        tv.setTopTextStr(list.get(position).title);
        tv.setBottomTextStr1(list.get(position).subtitle.get(0));
        tv.setBottomTextStr2(list.get(position).subtitle.get(1));
        tv.setBottomTextStr3(list.get(position).subtitle.get(2));
        return convertView;
    }
}
