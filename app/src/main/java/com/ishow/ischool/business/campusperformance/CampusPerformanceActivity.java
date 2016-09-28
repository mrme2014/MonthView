package com.ishow.ischool.business.campusperformance;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commonlib.widget.pull.BaseItemDecor;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.CampusSelectAdapter;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.CampusManager;
import com.ishow.ischool.fragment.BarChartFragment;
import com.ishow.ischool.fragment.LineChartFragment;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;


/**
 * Created by wqf on 16/9/8.
 */
public class CampusPerformanceActivity extends BaseActivity4Crm<CampusPerformancePresenter, CampusPerformanceModel> implements CampusPerformanceContract.View {

    private final int TYPE_START_TIME = 1;
    private final int TYPE_END_TIME = 2;
    private boolean startDateFinished = false;
    private boolean endDateFinished = false;
    DatePicker startDatePicker, endDatePicker;

    @BindView(R.id.filter_layout)
    LinearLayout filertLayout;
    @BindView(R.id.filter_type)
    TextView filertType;
    @BindView(R.id.filter_campus)
    TextView filertCampus;
    @BindView(R.id.filter_date)
    TextView filertDate;

    LineChartFragment lineChartFragment;
    BarChartFragment barChartFragment;
    Fragment curFragment;

    private RelativeLayout inverseLayout;
    private TextView inverseTv;
    private CheckBox inverseCb;
    private boolean isAllSelected = true;
    private TextView startDateTv, endDateTv;
    private String mFilterStartTime, mFilterEndTime;
    private String mLastStartTime, mLastEndTime;
    private ArrayList<CampusInfo> mLastSelectedItem;
    private Calendar calendar;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_campusperformance, R.string.campus_performance, -1, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        calendar = Calendar.getInstance();//初始化日历类
        mLastSelectedItem = new ArrayList<>();
        mLastSelectedItem.addAll(CampusManager.getInstance().get());
        mFilterStartTime = mLastStartTime = "201607";
        mFilterEndTime = mLastEndTime = "201609";
    }

    @Override
    protected void setUpData() {
        lineChartFragment = new LineChartFragment();
        barChartFragment = new BarChartFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content, lineChartFragment).add(R.id.content, barChartFragment).hide(barChartFragment);
        ft.commit();
        curFragment = lineChartFragment;
    }

    @OnClick({R.id.filter_type, R.id.filter_campus, R.id.filter_date})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter_type:
                if (mTypePopup != null && mTypePopup.isShowing()) {
                    mTypePopup.dismiss();
                } else {
                    closePop();
                    showTypePopup();
                }
                break;
            case R.id.filter_campus:
                if (mCampusPopup != null && mCampusPopup.isShowing()) {
                    mCampusPopup.dismiss();
                } else {
                    closePop();
                    showCampusPopup();
                }
                break;
            case R.id.filter_date:
                if (mDatePopup != null && mDatePopup.isShowing()) {
                    mDatePopup.dismiss();
                } else {
                    closePop();
                    showDatePopup();
                }
                break;
        }
    }

    void closePop() {
        if (mTypePopup != null && mTypePopup.isShowing()) {
            mTypePopup.dismiss();
        } else if (mCampusPopup != null && mCampusPopup.isShowing()) {
            mCampusPopup.dismiss();
        } else if (mDatePopup != null && mDatePopup.isShowing()) {
            mDatePopup.dismiss();
        }
    }


    private PopupWindow mTypePopup, mCampusPopup, mDatePopup;
    private ArrayList<CampusInfo> mList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    CampusSelectAdapter mAdapter;

    void showCampusPopup() {
        if (mCampusPopup == null) {
            View contentView = LayoutInflater.from(CampusPerformanceActivity.this).inflate(R.layout.filter_campus_layout, null);
            mCampusPopup = new PopupWindow(contentView);
            mCampusPopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mCampusPopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            //外部是否可以点击
//            mCampusPopup.setBackgroundDrawable(new BitmapDrawable());
//            mCampusPopup.setOutsideTouchable(true);

            inverseLayout = (RelativeLayout) contentView.findViewById(R.id.inverse_layout);
            inverseTv = (TextView) contentView.findViewById(R.id.inverse_tv);
            inverseCb = (CheckBox) contentView.findViewById(R.id.inverse_checkbox);
            RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerview);
            TextView resetTv = (TextView) contentView.findViewById(R.id.campus_reset);
            TextView okTv = (TextView) contentView.findViewById(R.id.campus_ok);
            inverseLayout.setOnClickListener(onClickListener);
            inverseCb.setOnClickListener(onClickListener);
            resetTv.setOnClickListener(onClickListener);
            okTv.setOnClickListener(onClickListener);

            mList.addAll(CampusManager.getInstance().get());
            layoutManager = new LinearLayoutManager(CampusPerformanceActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new CampusSelectAdapter(CampusPerformanceActivity.this, mList);
            recyclerView.addItemDecoration(new BaseItemDecor(this, 10));
            recyclerView.setAdapter(mAdapter);
            mAdapter.selectAllItems();
        }
        mCampusPopup.showAsDropDown(filertLayout);
    }

    void showTypePopup() {
        if (mTypePopup == null) {
            View contentView = LayoutInflater.from(CampusPerformanceActivity.this).inflate(R.layout.filter_type_layout, null);
            mTypePopup = new PopupWindow(contentView);
            mTypePopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mTypePopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

            TextView performanceTv = (TextView) contentView.findViewById(R.id.performance_tv);
            TextView numberTv = (TextView) contentView.findViewById(R.id.number_tv);
            View blankView = contentView.findViewById(R.id.blank_view_type);
            performanceTv.setOnClickListener(onClickListener);
            numberTv.setOnClickListener(onClickListener);
            blankView.setOnClickListener(onClickListener);
        }
        mTypePopup.showAsDropDown(filertLayout);
    }

    void showDatePopup() {
        if (mDatePopup == null) {
            View contentView = LayoutInflater.from(CampusPerformanceActivity.this).inflate(R.layout.filter_date_layout, null);
            mDatePopup = new PopupWindow(contentView);
            mDatePopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mDatePopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

            startDateTv = (TextView) contentView.findViewById(R.id.start_date);
            endDateTv = (TextView) contentView.findViewById(R.id.end_date);
            TextView resetTv = (TextView) contentView.findViewById(R.id.date_reset);
            TextView okTv = (TextView) contentView.findViewById(R.id.date_ok);
            View blankView = contentView.findViewById(R.id.blank_view_date);
            startDateTv.setText(getString(R.string.item_start_time) + " :        2016-07");
            endDateTv.setText(getString(R.string.item_end_time) + " :        2016-09");
            startDateTv.setOnClickListener(onClickListener);
            endDateTv.setOnClickListener(onClickListener);
            resetTv.setOnClickListener(onClickListener);
            okTv.setOnClickListener(onClickListener);
            blankView.setOnClickListener(onClickListener);
        }
        mDatePopup.showAsDropDown(filertLayout);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            switch (view.getId()) {
                case R.id.inverse_layout:
                    if (isAllSelected) {
                        inverseCb.setChecked(false);
                        mAdapter.clearAllItems();
                    } else {
                        inverseCb.setChecked(true);
                        mAdapter.selectAllItems();
                    }
                    isAllSelected = !isAllSelected;
                    break;
                case R.id.inverse_checkbox:
                    if (isAllSelected) {
                        mAdapter.clearAllItems();
                    } else {
                        mAdapter.selectAllItems();
                    }
                    isAllSelected = !isAllSelected;
                    break;
                case R.id.campus_reset:
                    mCampusPopup.dismiss();
                    break;
                case R.id.campus_ok:
                    int j = 0;
                    if (mLastSelectedItem.size() == mAdapter.getSelectedItem().size()) {        // 判断数据是否变化,否则就不用刷新
                        for (int i = 0; i < mLastSelectedItem.size(); i++) {
                            if (mLastSelectedItem.get(i).id == mAdapter.getSelectedItem().get(i).id) {
                                j = i;
                            } else {
                                break;
                            }
                        }
                    }
                    if (j == mLastSelectedItem.size() - 1) {
                        mCampusPopup.dismiss();
                        break;
                    }
                    mLastSelectedItem.clear();
                    mLastSelectedItem.addAll(mAdapter.getSelectedItem());

                    ArrayList<CampusInfo> selectedItem = mAdapter.getSelectedItem();
                    if (selectedItem.size() == 0) {
                        showToast("校区选项必须选择一个");
                    } else {
                        lineChartFragment.setLineChartData(selectedItem);
                        lineChartFragment.setPieChartData(selectedItem);

                        barChartFragment.setBarChartData(selectedItem, barChartFragment.mChartAmount, barChartFragment.mBarDataAmount);
                        barChartFragment.setBarChartData(selectedItem, barChartFragment.mChartPercentage, barChartFragment.mBarDataPercentage);
                    }
                    mCampusPopup.dismiss();
                    break;
                case R.id.performance_tv:
                    filertType.setText("业绩对比");
                    if (curFragment != lineChartFragment) {
                        ft.hide(curFragment);
                        if (lineChartFragment == null) {
                            lineChartFragment = new LineChartFragment();
                            ft.add(R.id.content, lineChartFragment);
                        }
                        curFragment = lineChartFragment;
                    }
                    ft.show(curFragment).commitAllowingStateLoss();
                    mTypePopup.dismiss();
                    break;
                case R.id.number_tv:
                    filertType.setText("人数对比");
                    if (curFragment != barChartFragment) {
                        ft.hide(curFragment);
                        if (barChartFragment == null) {
                            barChartFragment = new BarChartFragment();
                            ft.add(R.id.content, barChartFragment);
                        }
                        curFragment = barChartFragment;
                    }
                    ft.show(curFragment).commitAllowingStateLoss();
                    mTypePopup.dismiss();
                    break;
                case R.id.blank_view_type:
                    mTypePopup.dismiss();
                    break;
                case R.id.start_date:
                    startDatePicker = new DatePicker(CampusPerformanceActivity.this, DatePicker.YEAR_MONTH);
                    startDatePicker.setRangeStart(1970, 1);         //开始范围
                    startDatePicker.setRangeEnd(2099, 12);          //结束范围
                    startDatePicker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);  //得到月，因为从0开始的，所以要加1
                    startDatePicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                        @Override
                        public void onDatePicked(String year, String month) {
                            if (endDateFinished) {
                                String endDateYear = endDatePicker.getSelectedYear();
                                String endDateMonth = endDatePicker.getSelectedMonth();
                                if (!year.equals(endDateYear)) {
                                    year = endDateYear;
                                }
                                if (Integer.parseInt(endDateMonth) <= 6) {      //当end<=6(上半年)
                                    if (Integer.parseInt(month) > Integer.parseInt(endDateMonth)) { //若start>end,则start=end(不合理情况)
                                        month = endDateMonth;
                                    }
                                } else {                                        //当end>6(下半年)
                                    if (Integer.parseInt(month) > Integer.parseInt(endDateMonth)) { //若start>end,则start=end(不合理情况)
                                        month = endDateMonth;
                                    } else if (Integer.parseInt(month) <= 6) {                      //若start<=6,则start=7
                                        month = "07";
                                    }
                                }
                            }
                            startDateTv.setText(getString(R.string.item_start_time) + " :        " + year + "-" + month);
                            mFilterStartTime = year + month;
                            startDateFinished = true;
                        }
                    });
                    startDatePicker.show();
                    break;
                case R.id.end_date:
                    endDatePicker = new DatePicker(CampusPerformanceActivity.this, DatePicker.YEAR_MONTH);
                    endDatePicker.setRangeStart(1970, 1);           //开始范围
                    endDatePicker.setRangeEnd(2099, 12);            //结束范围
                    endDatePicker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);  //得到月，因为从0开始的，所以要加1
                    endDatePicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                        @Override
                        public void onDatePicked(String year, String month) {
                            if (startDateFinished) {
                                String startDateYear = startDatePicker.getSelectedYear();
                                String startDateMonth = startDatePicker.getSelectedMonth();
                                if (!year.equals(startDateYear)) {
                                    year = startDateYear;
                                }
                                if (Integer.parseInt(startDateMonth) <= 6) {    //当start<=6(上半年)
                                    if (Integer.parseInt(month) > Integer.parseInt(startDateMonth)) {   //若end>start
                                        if (Integer.parseInt(month) > 6) {
                                            month = "06";                              //若end>6,则end=6
                                        }
                                    } else {                                                            //若end<start(不合理情况)
                                        month = startDateMonth;
                                    }
                                } else {                                        //当start>6(下半年)
                                    if (Integer.parseInt(month) < Integer.parseInt(startDateMonth)) {   //若end<start
                                        month = startDateMonth;
                                    }
                                }
                            }
                            endDateTv.setText(getString(R.string.item_end_time) + " :        " + year + "-" + month);
                            mFilterEndTime = year + month;
                            endDateFinished = true;
                        }
                    });
                    endDatePicker.show();
                    break;
                case R.id.date_reset:
                    startDateFinished = false;
                    endDateFinished = false;
                    mFilterStartTime = "";
                    mFilterEndTime = "";
                    startDateTv.setText(getString(R.string.item_start_time) + " :        ");
                    endDateTv.setText(getString(R.string.item_end_time) + " :        ");
                    break;
                case R.id.date_ok:
                    if (mLastStartTime.equals(mFilterStartTime) && mLastEndTime.equals(mFilterEndTime)) {
                        mDatePopup.dismiss();
                        break;
                    }

                    lineChartFragment.pullData(lineChartFragment.mParamCampus, mFilterStartTime != null ? Integer.parseInt(mFilterStartTime) : -1,
                                mFilterEndTime != null ? Integer.parseInt(mFilterEndTime) : -1, lineChartFragment.mParamDataType);
                    barChartFragment.pullData(barChartFragment.lastShowCampus, mFilterStartTime != null ? Integer.parseInt(mFilterStartTime) : -1,
                                mFilterEndTime != null ? Integer.parseInt(mFilterEndTime) : -1);
                    mLastStartTime = mFilterStartTime;
                    mLastEndTime = mFilterEndTime;
                    mDatePopup.dismiss();
                    break;
                case R.id.blank_view_date:
                    mDatePopup.dismiss();
                    break;
            }
        }
    };

}
