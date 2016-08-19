package com.ishow.ischool.business.statisticslist;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.student.StudentStatistics;
import com.ishow.ischool.bean.student.StudentStatisticsList;
import com.ishow.ischool.bean.university.UniversityInfo;
import com.ishow.ischool.business.addstudent.AddStudentActivity;
import com.ishow.ischool.business.student.detail.StudentDetailActivity;
import com.ishow.ischool.business.universitypick.UniversityPickActivity;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.util.UserUtil;
import com.ishow.ischool.widget.custom.InputLinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wqf on 16/8/14.
 */
public class StatisticsListActivity extends BaseListActivity4Crm<StatisticsListPresenter, StatisticsListModel, StudentStatistics> implements StatisticsListContract.View,
        InputLinearLayout.EidttextClick, View.OnTouchListener {

    @BindView(R.id.fab)
    FloatingActionButton addFab;

    @BindView(R.id.root_ll)
    LinearLayout popup_layout;
    @BindView(R.id.item_campus)
    InputLinearLayout campusIL;
    @BindView(R.id.start_time)
    EditText startTimeEt;
    @BindView(R.id.end_time)
    EditText endTimeEt;
    @BindView(R.id.item_pay_state)
    InputLinearLayout payStateIL;
    @BindView(R.id.item_university)
    InputLinearLayout universityIL;
    @BindView(R.id.item_referrer)
    InputLinearLayout referrerIL;

    OptionsPickerView pvOptions;
    TimePickerView pvTime;
    private Boolean startTimeFlag = true;
    private GestureDetector mGestureDetector;
    private SimpleDateFormat sdf;

    private UniversityInfo mUniversityInfo;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_statistics_list, R.string.student_statistics, R.menu.menu_statisticslist, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        campusIL.setOnEidttextClick(this);
        payStateIL.setOnEidttextClick(this);
        universityIL.setOnEidttextClick(this);
        referrerIL.setOnEidttextClick(this);
        initPickView();
        mGestureDetector = new GestureDetector(new Gesturelistener());
        startTimeEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startTimeFlag = true;
                return mGestureDetector.onTouchEvent(motionEvent);
            }
        });
        endTimeEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startTimeFlag = false;
                return mGestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                if (popup_layout.getVisibility() == View.VISIBLE) {
                    popup_layout.setVisibility(View.GONE);
                }
                break;
            case R.id.action_filter:
                if (popup_layout.getVisibility() != View.VISIBLE) {
                    popup_layout.setVisibility(View.VISIBLE);
                } else {
                    popup_layout.setVisibility(View.GONE);
                }
                break;
        }
        return true;
    }

    @OnClick({R.id.filter_reset, R.id.filter_ok})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter_reset:
                resetFilter();
                break;
            case R.id.filter_ok:
                popup_layout.setVisibility(View.GONE);
                break;
        }
    }

    void resetFilter() {
        campusIL.setContent("");
        startTimeEt.setText("");
        endTimeEt.setText("");
        payStateIL.setContent("");
        universityIL.setContent("");
        referrerIL.setContent("");
    }

    void initPickView() {
        pvOptions = new OptionsPickerView(this);
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public void onEdittextClick(View view) {
        switch (view.getId()) {
            case R.id.item_campus:
                mPresenter.showCampusPick(pvOptions);
                break;
            case R.id.item_pay_state:
                mPresenter.showPayStatePick(pvOptions);
                break;
            case R.id.item_university:
                startActivityForResult(new Intent(StatisticsListActivity.this, UniversityPickActivity.class), UniversityPickActivity.REQUEST_CODE_PICK_UNIVERSITY);
                break;
            case R.id.item_referrer:

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UniversityPickActivity.REQUEST_CODE_PICK_UNIVERSITY:
                    mUniversityInfo = data.getParcelableExtra(UniversityPickActivity.KEY_PICKED_UNIVERSITY);
                    universityIL.setContent(mUniversityInfo.name);
//                    college_id = mUniversityInfo.id;
//                    province_id = mUniversityInfo.prov_id;
//                    city_id = mUniversityInfo.city_id;
                    break;
            }
        }
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
            intent.putExtra(StudentDetailActivity.P_STUDENT, data.studentInfo);
            JumpManager.jumpActivity(StatisticsListActivity.this, intent);
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    private class Gesturelistener implements GestureDetector.OnGestureListener {

        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub
        }

        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            if (startTimeFlag) {
                mPresenter.showStartTimePick(pvTime);
            } else {
                mPresenter.showEndTimePick(pvTime);
            }
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            // TODO Auto-generated method stub
            return false;
        }

        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub
            return false;
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

    @Override
    public void onCampusPicked(String picked) {
        campusIL.setContent(picked);
    }

    @Override
    public void onStartTimePicked(Date picked) {
        startTimeEt.setText(picked.toString());
    }

    @Override
    public void onEndTimePicked(Date picked) {
        endTimeEt.setText(sdf.format(picked));
    }

    @Override
    public void onPayStatePicked(String picked) {
        payStateIL.setContent(sdf.format(picked));
    }

    @OnClick(R.id.fab)
    void add() {
        JumpManager.jumpActivity(this, AddStudentActivity.class);
    }
}
