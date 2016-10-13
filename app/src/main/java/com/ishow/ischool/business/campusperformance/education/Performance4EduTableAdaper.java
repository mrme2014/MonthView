package com.ishow.ischool.business.campusperformance.education;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.campusperformance.EducationMonth;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mini on 16/10/11.
 */
public class Performance4EduTableAdaper extends BaseAdapter {

    private ArrayList<String> names;
    private ArrayList<EducationMonth> datas;
    private LayoutInflater inflater;

    public Performance4EduTableAdaper(Context context, ArrayList<String> names, ArrayList<EducationMonth> datas) {
        this.names = names;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas == null ? null : datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_performance_education_table, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        EducationMonth educationMonth = (EducationMonth)getItem(position);
        holder.itemName.setText(names.get(position));
        holder.itemChallenge.setText(educationMonth.full_challenge + "%");
        holder.itemBase.setText(educationMonth.full_base + "%");
        holder.itemReal.setText(educationMonth.permonth_real + "%");

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.item_name)
        TextView itemName;
        @BindView(R.id.item_challenge)
        TextView itemChallenge;
        @BindView(R.id.item_base)
        TextView itemBase;
        @BindView(R.id.item_real)
        TextView itemReal;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
