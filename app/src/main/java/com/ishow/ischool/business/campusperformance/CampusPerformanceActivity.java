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
import android.widget.Toast;

import com.commonlib.widget.pull.BaseItemDecor;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.CampusSelectAdapter;
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
    private String mFilterStartTime;
    private String mFilterEndTime;
    private Calendar calendar;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_campusperformance, R.string.campus_performance, -1, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        calendar = Calendar.getInstance();//使用日历类
    }

    @Override
    protected void setUpData() {
        lineChartFragment = new LineChartFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content, lineChartFragment);
        ft.commit();
        curFragment = lineChartFragment;
    }

    @OnClick({R.id.filter_type, R.id.filter_campus, R.id.filter_date, R.id.detail_layout})
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
            case R.id.detail_layout:

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
    private ArrayList<String> mList = new ArrayList<>();
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

            mList.addAll(CampusManager.getInstance().getCampusNames());
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
                    ArrayList<Integer> i = mAdapter.getSelectedItem();
                    if (curFragment == lineChartFragment) {
                        if (!lineChartFragment.curPieMode) {
                            lineChartFragment.setLineChartData(mAdapter.getSelectedItem());
                            mCampusPopup.dismiss();
                        } else {
                            mCampusPopup.dismiss();
                        }
                    } else {
                        if (barChartFragment.curAmountMode) {
                            barChartFragment.setAmountData(mAdapter.getSelectedItem());
                            mCampusPopup.dismiss();
                        } else {
                            mCampusPopup.dismiss();
                        }
                    }
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
                    DatePicker startPicker = new DatePicker(CampusPerformanceActivity.this, DatePicker.YEAR_MONTH);
                    startPicker.setRangeStart(1970, 1, 1);       //开始范围
                    startPicker.setRangeEnd(2099, 12, 31);       //结束范围
                    startPicker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);  //得到月，因为从0开始的，所以要加1
                    startPicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                        @Override
                        public void onDatePicked(String year, String month) {
                            startDateTv.setText(year + "-" + month);
                        }
                    });
                    startPicker.show();
                    break;
                case R.id.end_date:
                    DatePicker endPicker = new DatePicker(CampusPerformanceActivity.this, DatePicker.YEAR_MONTH);
                    endPicker.setRangeStart(1970, 1, 1);       //开始范围
                    endPicker.setRangeEnd(2099, 12, 31);       //结束范围
                    endPicker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);  //得到月，因为从0开始的，所以要加1
                    endPicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                        @Override
                        public void onDatePicked(String year, String month) {
                            endDateTv.setText(year + "-" + month);
                        }
                    });
                    endPicker.show();
                    break;
                case R.id.date_reset:
                    mDatePopup.dismiss();
                    break;
                case R.id.date_ok:
                    mDatePopup.dismiss();
                    break;
                case R.id.blank_view_date:
                    mDatePopup.dismiss();
                    break;
            }
        }
    };
    
}
