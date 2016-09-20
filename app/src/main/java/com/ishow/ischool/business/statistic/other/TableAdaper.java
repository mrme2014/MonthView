package com.ishow.ischool.business.statistic.other;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonlib.widget.base.BaseListAdapter;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.statistics.OtherStatistics;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abel on 16/9/19.
 */
public class TableAdaper extends BaseListAdapter<OtherStatistics> {

    public TableAdaper(Context context) {
        super(context);
    }

    public TableAdaper(Context context, ArrayList<OtherStatistics> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = getInflater().inflate(R.layout.table_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OtherStatistics otherStatistics = getItem(position);
        holder.tableNo.setText(String.valueOf(position + 1));
        holder.tableName.setText(otherStatistics.name);
        holder.tableNum.setText(otherStatistics.value + "");

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.item_table_no)
        TextView tableNo;
        @BindView(R.id.item_table_name)
        TextView tableName;
        @BindView(R.id.item_table_num)
        TextView tableNum;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
