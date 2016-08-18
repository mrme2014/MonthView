package com.ishow.ischool.business.statisticslist;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.student.StudentStatistics;
import com.ishow.ischool.bean.student.StudentStatisticsList;
import com.ishow.ischool.business.addstudent.AddStudentActivity;
import com.ishow.ischool.business.student.detail.StudentDetailActivity;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.util.UserUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wqf on 16/8/14.
 */
public class StatisticsListActivity extends BaseListActivity4Crm<StatisticsListPresenter, StatisticsListModel, StudentStatistics> implements StatisticsListContract.View {

    @BindView(R.id.fab)
    FloatingActionButton addFab;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_statistics_list, R.string.student_statistics, R.menu.menu_statisticslist, MODE_BACK);
    }

    @Override
    public void onRefresh(int action) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mCurrentPage = 1;
        }
        mPresenter.getList4StudentStatistics(mUser.getUserInfo().campus_id);
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
        @BindView(R.id.university)
        TextView university;
        @BindView(R.id.phone)
        ImageView phone;

        public StatisticsListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            StudentStatistics data = mDataList.get(position);
            final String nameStr = data.studentInfo.name;
            final String phoneNumber = data.studentInfo.mobile;
            if (data != null) {
//                PicUtils.loadUserHeader(StatisticsListActivity.this, data.StudentInfo., avatar);
                name.setText(data.studentInfo.name);
//                university.setText(data.StudentInfo.);
                state.setText(UserUtil.getUserPayState(data.applyInfo.status));
            }
            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActionSheet.createBuilder(StatisticsListActivity.this, StatisticsListActivity.this.getSupportFragmentManager())
                            .setCancelButtonTitle(R.string.str_cancel)
                            .setOtherButtonTitles(nameStr + "\n" + phoneNumber, getString(R.string.call), getString(R.string.phone_contacts))
                            .setCancelableOnTouchOutside(true)
                            .setTag("TAG")
                            .setListener(new ActionSheet.ActionSheetListener() {
                                @Override
                                public void onDismiss(ActionSheet actionSheet, boolean b) {

                                }

                                @Override
                                public void onOtherButtonClick(ActionSheet actionSheet, int i) {
                                    switch (i) {
                                        case 1:
                                            // TODO: 16/8/17  打电话
                                            break;
                                        case 2:
                                            // TODO: 16/8/17  保存至通讯录
                                            break;
                                    }
                                }
                            }).show();
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            StudentStatistics data = mDataList.get(position);
            Intent intent = new Intent(StatisticsListActivity.this, StudentDetailActivity.class);
            intent.putExtra(StudentDetailActivity.P_STUDENT, data);
            JumpManager.jumpActivity(StatisticsListActivity.this, intent);
        }
    }

    @Override
    public void getListSuccess(StudentStatisticsList studentStatisticsList) {
        loadSuccess(studentStatisticsList.lists);
    }

    @Override
    public void getListFail(String msg) {
        loadFailed();
    }

    @OnClick(R.id.fab)
    void add() {
        JumpManager.jumpActivity(this, AddStudentActivity.class);
    }
}
