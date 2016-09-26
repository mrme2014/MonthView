package com.ishow.ischool.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.campusperformance.SignAmount;

import java.util.ArrayList;

/**
 * Created by wqf on 16/9/23.
 */

public class TableContentItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SignAmount> datas;

    public TableContentItemAdapter(Context context, ArrayList<SignAmount> datas) {
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

        SignAmount data = datas.get(position);
        holder.tv_column1.setText(data.scene + "");
        holder.tv_column2.setText(data.sign + "");
        holder.tv_column3.setText(data.fullPay + "");
        holder.tv_column4.setText(data.signRate + "");
        holder.tv_column5.setText(data.signRate + "");
        holder.tv_column6.setText(data.fullSignRate + "");

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
