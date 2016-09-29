package com.ishow.ischool.widget.table;

import android.content.Context;
import android.view.View;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.campusperformance.WeekTableBodyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mini on 16/9/28.
 */

public class WeekPerformanceTableBodyAdapter extends MyLinearLayoutBaseAdapter<WeekTableBodyItem> {

    public WeekPerformanceTableBodyAdapter(Context context, List<WeekTableBodyItem> list) {
        super(context, list);
    }

    @Override
    View getView(int position) {
        View convertView = getLayoutInflater().inflate(R.layout.item_month_performance_table_body, null);
        MultiTextLabelBody tv = (MultiTextLabelBody) convertView.findViewById(R.id.tv);
        ArrayList<String> item = (ArrayList<String>) list.get(position);
        tv.setLeftextStr(item.get(0));
        tv.setCenterTextStr(item.get(1));
        tv.setRightTextStr(item.get(2));
        return convertView;
    }
}
