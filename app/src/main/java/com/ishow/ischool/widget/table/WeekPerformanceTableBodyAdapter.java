package com.ishow.ischool.widget.table;

import android.content.Context;
import android.view.View;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.campusperformance.WeekTableBodyItem;

import java.util.ArrayList;
import java.util.List;

import static com.ishow.ischool.R.id.tv;

/**
 * Created by mini on 16/9/28.
 */

public class WeekPerformanceTableBodyAdapter extends MyLinearLayoutBaseAdapter<WeekTableBodyItem> {

    public WeekPerformanceTableBodyAdapter(Context context, List<WeekTableBodyItem> list) {
        super(context, list);
    }

    @Override
    View getView(int position, View convertView) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.item_month_performance_table_body, null);
            viewHolder = new ViewHolder();
            viewHolder.tv = (MultiTextLabelBody) convertView.findViewById(tv);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ArrayList<String> item = (ArrayList<String>) list.get(position);
        viewHolder.tv.setLeftextStr(item.get(0));
        viewHolder.tv.setCenterTextStr(item.get(1));
        viewHolder.tv.setRightTextStr(item.get(2));
        return convertView;
    }

    class ViewHolder {
        MultiTextLabelBody tv;
    }
}
