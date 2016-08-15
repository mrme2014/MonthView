package com.ishow.ischool.business.statisticslist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonlib.widget.pull.BaseViewHolder;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.student.StudentStatistics;
import com.ishow.ischool.common.base.BaseListActivity4Crm;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wqf on 16/8/14.
 */
public class StatisticsListActivity extends BaseListActivity4Crm<StatisticsListPresenter, StatisticsListModel, StudentStatistics> implements StatisticsListContract.View {

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_search_student, -1, R.menu.menu_statisticslist, MODE_BACK);
    }

    @Override
    protected void setUpView() {
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void onRefresh(int action) {

    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_statistics, parent, false);
        return new StatisticsListViewHolder(view);
    }

    class StatisticsListViewHolder extends BaseViewHolder {
        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.state)
        TextView state;
        @BindView(R.id.school)
        TextView school;
        @BindView(R.id.phone)
        ImageView phone;

        public StatisticsListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            StudentStatistics data = mDataList.get(position);
            if (data != null) {

            }
        }

        @Override
        public void onItemClick(View view, int position) {
            StudentStatistics data = mDataList.get(position);

        }

    }
}
