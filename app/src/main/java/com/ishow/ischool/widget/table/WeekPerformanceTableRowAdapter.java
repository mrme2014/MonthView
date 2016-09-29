package com.ishow.ischool.widget.table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.campusperformance.WeekTableBodyItem;
import com.ishow.ischool.bean.campusperformance.WeekTableBodyRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mini on 16/9/28.
 */

public class WeekPerformanceTableRowAdapter extends BaseAdapter {

    private Context context;
    private List<WeekTableBodyRow> datas;

    public WeekPerformanceTableRowAdapter(Context context, List<WeekTableBodyRow> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_month_performance_table_row, null);
        MyLinearLayout4ListView lv = (MyLinearLayout4ListView) convertView.findViewById(R.id.row_lv);

        ArrayList<WeekTableBodyItem> rowData = (ArrayList<WeekTableBodyItem>) datas.get(position);
        ArrayList<WeekTableBodyItem> temp = new ArrayList<>();
        temp.addAll(rowData.subList(1, rowData.size()));        // 去掉第一个"校区"(已固定存在于右边)
        WeekPerformanceTableBodyAdapter adapter = new WeekPerformanceTableBodyAdapter(context, temp);
        lv.setAdapter(adapter);

        return convertView;
    }

}
