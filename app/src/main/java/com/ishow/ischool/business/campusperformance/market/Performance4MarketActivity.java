package com.ishow.ischool.business.campusperformance.market;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.ishow.ischool.util.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;


/**
 * Created by wqf on 16/9/8.
 */
public class Performance4MarketActivity extends BaseActivity4Crm implements Performance4MarketContract.View {

    private boolean startDateFinished = true;
    private boolean endDateFinished = true;
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
        setContentView(R.layout.activity_market_performance, R.string.campus_performance, -1, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        mLastSelectedItem = new ArrayList<>();
        mLastSelectedItem.addAll(CampusManager.getInstance().get());
        initDefaultDate();
    }

    void initDefaultDate() {
        calendar = Calendar.getInstance();      //初始化日历类
        int curYear = calendar.get(Calendar.YEAR);
        String startMonth = calendar.get(Calendar.MONTH) + "";
        if (Integer.parseInt(startMonth) == 0) {
            curYear = curYear - 1;
            startMonth = "12";
        } else if (Integer.parseInt(startMonth) < 10) {
            startMonth = "0" + startMonth;
        }
        mFilterStartTime = curYear + startMonth;
        String endMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1); //得到月，因为从0开始的，所以要加1
        if (Integer.parseInt(endMonth) < 10) {
            endMonth = "0" + endMonth;
        }
        mFilterEndTime = curYear + endMonth;
        mFilterStartTime = mLastStartTime = curYear + startMonth;
        mFilterEndTime = mLastEndTime = curYear + endMonth;
    }

    @Override
    protected void setUpData() {
        lineChartFragment = LineChartFragment.newInstance(Integer.parseInt(mFilterStartTime), Integer.parseInt(mFilterEndTime));
        barChartFragment = BarChartFragment.newInstance(Integer.parseInt(mFilterStartTime), Integer.parseInt(mFilterEndTime));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content, lineChartFragment).add(R.id.content, barChartFragment).hide(barChartFragment);
        ft.commit();
        curFragment = lineChartFragment;
    }

    @OnClick({R.id.filter_type_layout, R.id.filter_campus_layout, R.id.filter_date_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter_type_layout:
                if (mTypePopup != null && mTypePopup.isShowing()) {
                    mTypePopup.dismiss();
                } else {
                    closePop();
                    showTypePopup();
                }
                break;
            case R.id.filter_campus_layout:
                if (mCampusPopup != null && mCampusPopup.isShowing()) {
                    mCampusPopup.dismiss();
                } else {
                    closePop();
                    showCampusPopup();
                }
                break;
            case R.id.filter_date_layout:
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
            View contentView = LayoutInflater.from(Performance4MarketActivity.this).inflate(R.layout.filter_campus_layout, null);
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
            layoutManager = new LinearLayoutManager(Performance4MarketActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new CampusSelectAdapter(Performance4MarketActivity.this, mList);
            recyclerView.addItemDecoration(new BaseItemDecor(this, 10));
            recyclerView.setAdapter(mAdapter);
            mAdapter.selectAllItems();
        }
        mCampusPopup.showAsDropDown(filertLayout);
    }

    void showTypePopup() {
        if (mTypePopup == null) {
            View contentView = LayoutInflater.from(Performance4MarketActivity.this).inflate(R.layout.filter_type_layout, null);
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
            View contentView = LayoutInflater.from(Performance4MarketActivity.this).inflate(R.layout.filter_date_layout, null);
            mDatePopup = new PopupWindow(contentView);
            mDatePopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mDatePopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

            startDateTv = (TextView) contentView.findViewById(R.id.start_date);
            endDateTv = (TextView) contentView.findViewById(R.id.end_date);
            TextView resetTv = (TextView) contentView.findViewById(R.id.date_reset);
            TextView okTv = (TextView) contentView.findViewById(R.id.date_ok);
            View blankView = contentView.findViewById(R.id.blank_view_date);
            startDateTv.setText(getString(R.string.item_start_time) + " :   " + mFilterStartTime.substring(0, 4) + "-" + mFilterStartTime.substring(4, mFilterStartTime.length()));
            endDateTv.setText(getString(R.string.item_end_time) + " :   " + mFilterEndTime.substring(0, 4) + "-" + mFilterEndTime.substring(4, mFilterEndTime.length()));
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
                        if (j == mLastSelectedItem.size()) {
                            mCampusPopup.dismiss();
                            break;
                        }
                    }

                    mLastSelectedItem.clear();
                    mLastSelectedItem.addAll(mAdapter.getSelectedItem());

                    ArrayList<CampusInfo> selectedItem = mAdapter.getSelectedItem();
                    if (selectedItem.size() == 0) {
                        showToast("校区选项必须选择一个");
                    } else {
                        if (lineChartFragment.mLastYdatas != null && lineChartFragment.mLastYdatas.size() > 0) {        //  避免网络异常等原因数据获取失败
                            lineChartFragment.setLineChartData(selectedItem);
                            lineChartFragment.setPieChartData(selectedItem);

                            barChartFragment.setBarChartData(selectedItem, barChartFragment.mChartAmount, barChartFragment.mBarDataAmount);
                            barChartFragment.setBarChartData(selectedItem, barChartFragment.mChartPercentage, barChartFragment.mBarDataPercentage);
                        }
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
                    startDatePicker = new DatePicker(Performance4MarketActivity.this, DatePicker.YEAR_MONTH);
                    if (endDateFinished) {
                        int curYear = Integer.parseInt(mFilterEndTime.substring(0, 4));
                        int endMonth = Integer.parseInt(mFilterEndTime.substring(4, mFilterEndTime.length()));
                        if (endMonth <= 6) {     // 上半年
                            startDatePicker.setRangeStart(curYear, 1);                  //开始范围
                        } else {
                            startDatePicker.setRangeStart(curYear, 7);                  //开始范围
                        }
                        startDatePicker.setRangeEnd(curYear, endMonth);             //结束范围
                        if (startDateFinished) {
                            startDatePicker.setSelectedItem(Integer.parseInt(mFilterStartTime.substring(0, 4)), Integer.parseInt(mFilterStartTime.substring(4, mFilterStartTime.length())));
                        } else {
                            startDatePicker.setSelectedItem(curYear, endMonth);
                        }
                    } else {
                        startDatePicker.setRangeStart(1970, 1);         //开始范围
                        startDatePicker.setRangeEnd(2099, 12);          //结束范围
                        startDatePicker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);  //得到月，因为从0开始的，所以要加1
                    }

                    startDatePicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                        @Override
                        public void onDatePicked(String year, String month) {
                            startDateTv.setText(getString(R.string.item_start_time) + " :   " + year + "-" + month);
                            mFilterStartTime = year + month;
                            startDateFinished = true;
                        }
                    });
                    startDatePicker.show();
                    break;
                case R.id.end_date:
                    endDatePicker = new DatePicker(Performance4MarketActivity.this, DatePicker.YEAR_MONTH);
                    if (startDateFinished) {
                        int curYear = Integer.parseInt(mFilterStartTime.substring(0, 4));
                        int endMonth = Integer.parseInt(mFilterStartTime.substring(4, mFilterStartTime.length()));
                        if (endMonth <= 6) {     // 上半年
                            endDatePicker.setRangeEnd(curYear, 6);                  //开始范围
                        } else {
                            endDatePicker.setRangeEnd(curYear, 12);                  //开始范围
                        }
                        endDatePicker.setRangeStart(curYear, endMonth);             //结束范围
                        if (endDateFinished) {
                            endDatePicker.setSelectedItem(Integer.parseInt(mFilterEndTime.substring(0, 4)), Integer.parseInt(mFilterEndTime.substring(4, mFilterEndTime.length())));
                        } else {
                            endDatePicker.setSelectedItem(curYear, endMonth);
                        }
                    } else {
                        endDatePicker.setRangeStart(1970, 1);           //开始范围
                        endDatePicker.setRangeEnd(2099, 12);            //结束范围
                        endDatePicker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);  //得到月，因为从0开始的，所以要加1
                    }
                    
                    endDatePicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                        @Override
                        public void onDatePicked(String year, String month) {
                            endDateTv.setText(getString(R.string.item_end_time) + " :   " + year + "-" + month);
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
                    startDateTv.setText(getString(R.string.item_start_time) + " :   ");
                    endDateTv.setText(getString(R.string.item_end_time) + " :   ");
                    break;
                case R.id.date_ok:
                    mDatePopup.dismiss();
                    if (TextUtils.isEmpty(mFilterStartTime) && TextUtils.isEmpty(mFilterEndTime)) {
                        ToastUtil.showToast(Performance4MarketActivity.this, "请选择时间");
                        break;
                    } else {
                        if (TextUtils.isEmpty(mFilterStartTime)) {
                            String curYear = mFilterEndTime.substring(0, 4);
                            int endMonth = Integer.parseInt(mFilterEndTime.substring(4, mFilterEndTime.length()));
                            if (endMonth <= 6) {     // 上半年
                                mFilterStartTime = curYear + "01";
                            } else {
                                mFilterStartTime = curYear + "07";
                            }
                        } else if (TextUtils.isEmpty(mFilterEndTime)) {
                            String curYear = mFilterStartTime.substring(0, 4);
                            int startMonth = Integer.parseInt(mFilterStartTime.substring(4, mFilterStartTime.length()));
                            if (startMonth <= 6) {     // 上半年
                                mFilterEndTime = curYear + "06";
                            } else {
                                mFilterEndTime = curYear + "12";
                            }
                        }
                        if (mLastStartTime.equals(mFilterStartTime) && mLastEndTime.equals(mFilterEndTime)) {
                            break;
                        }
                        mLastStartTime = mFilterStartTime;
                        mLastEndTime = mFilterEndTime;
                        if (lineChartFragment.mLastYdatas != null && lineChartFragment.mLastYdatas.size() > 0) {        //  避免网络异常等原因数据获取失败
                            lineChartFragment.pullData(lineChartFragment.mLastCampus, Integer.parseInt(mFilterStartTime),
                                    Integer.parseInt(mFilterEndTime), lineChartFragment.mParamDataType);
                            barChartFragment.pullData(barChartFragment.mLastCampus, Integer.parseInt(mFilterStartTime),
                                    Integer.parseInt(mFilterEndTime));
                        }
                    }
                    break;
                case R.id.blank_view_date:
                    mDatePopup.dismiss();
                    break;
            }
        }
    };

}
