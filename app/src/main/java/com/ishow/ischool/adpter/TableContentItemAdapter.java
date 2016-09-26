package com.ishow.ischool.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ishow.ischool.R;

/**
 * Created by wqf on 16/9/23.
 */

public class TableContentItemAdapter extends BaseAdapter {
    private Context context;

    public TableContentItemAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 16;
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_campus_table_content, null);
            holder.tv_column1 = (TextView) convertView.findViewById(R.id.column1);
            holder.tv_column2 = (TextView) convertView.findViewById(R.id.column2);
            holder.tv_column3 = (TextView) convertView.findViewById(R.id.column3);
            holder.tv_column4 = (TextView) convertView.findViewById(R.id.column4);
            holder.tv_column5 = (TextView) convertView.findViewById(R.id.column5);
            holder.tv_column6 = (TextView) convertView.findViewById(R.id.column6);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_column1.setText("column1测试" + position);
        holder.tv_column2.setText("column2测试" + position);
        holder.tv_column3.setText("column3测试" + position);
        holder.tv_column4.setText("column4测试" + position);
        holder.tv_column5.setText("column5测试" + position);
        holder.tv_column6.setText("column6测试" + position);

        return convertView;
    }


    class ViewHolder {
        TextView tv_column1;
        TextView tv_column2;
        TextView tv_column3;
        TextView tv_column4;
        TextView tv_column5;
        TextView tv_column6;
    }
}
