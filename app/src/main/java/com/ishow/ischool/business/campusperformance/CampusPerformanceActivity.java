package com.ishow.ischool.business.campusperformance;

import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.adpter.CampusSelectAdapter;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.CampusManager;
import com.ishow.ischool.fragment.BarChartFragment;
import com.ishow.ischool.fragment.LineChartFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by wqf on 16/9/8.
 */
public class CampusPerformanceActivity extends BaseActivity4Crm<CampusPerformancePresenter, CampusPerformanceModel> implements CampusPerformanceContract.View {

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

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_campusperformance, R.string.campus_performance, -1, MODE_BACK);
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {
        lineChartFragment = LineChartFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content, lineChartFragment);
        ft.commit();
        curFragment = lineChartFragment;
    }

    @OnClick({R.id.filter_type, R.id.filter_campus, R.id.filter_date, R.id.detail_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter_type:
                if (mTypePopup != null && isTypeShow) {
                    mTypePopup.dismiss();
                } else {
                    showTypePopup();
                }
                break;
            case R.id.filter_campus:
                if (mCampusPopup != null && isCampusShow) {
                    mCampusPopup.dismiss();
                } else {
                    showCampusPopup();
                }
                break;
            case R.id.filter_date:
                if (mDatePopup != null && isDateShow) {
                    mDatePopup.dismiss();
                } else {
                    showDatePopup();
                }
                break;
            case R.id.detail_layout:

                break;
        }
    }


    private PopupWindow mTypePopup, mCampusPopup, mDatePopup;
    private boolean isTypeShow, isCampusShow, isDateShow;
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
            mCampusPopup.setBackgroundDrawable(new BitmapDrawable());
            mCampusPopup.setOutsideTouchable(true);

            RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerview);
            TextView resetTv = (TextView) contentView.findViewById(R.id.filter_reset);
            TextView okTv = (TextView) contentView.findViewById(R.id.filter_ok);
            View blankView = contentView.findViewById(R.id.blank_view_campus);
            resetTv.setOnClickListener(onClickListener);
            okTv.setOnClickListener(onClickListener);
            blankView.setOnClickListener(onClickListener);

            mList.addAll(CampusManager.getInstance().getCampusNames());
            layoutManager = new LinearLayoutManager(CampusPerformanceActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new CampusSelectAdapter(CampusPerformanceActivity.this, mList);
            recyclerView.setAdapter(mAdapter);
        }
        mCampusPopup.showAsDropDown(filertLayout);
        isCampusShow = true;
        isTypeShow = false;
        isDateShow = false;

        mCampusPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isCampusShow = false;
            }
        });
    }

    void showTypePopup() {
        if (mTypePopup == null) {
            View contentView = LayoutInflater.from(CampusPerformanceActivity.this).inflate(R.layout.filter_type_layout, null);
            mTypePopup = new PopupWindow(contentView);
            mTypePopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mTypePopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            //外部是否可以点击
            mTypePopup.setBackgroundDrawable(new BitmapDrawable());
            mTypePopup.setOutsideTouchable(true);

            TextView performanceTv = (TextView) contentView.findViewById(R.id.performance_tv);
            TextView numberTv = (TextView) contentView.findViewById(R.id.number_tv);
            View blankView = contentView.findViewById(R.id.blank_view_type);
            performanceTv.setOnClickListener(onClickListener);
            numberTv.setOnClickListener(onClickListener);
            blankView.setOnClickListener(onClickListener);
        }
        mTypePopup.showAsDropDown(filertLayout);
        isTypeShow = true;
        isCampusShow = false;
        isDateShow = false;

        mTypePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isTypeShow = false;
            }
        });
    }

    void showDatePopup() {
        if (mDatePopup == null) {
            View contentView = LayoutInflater.from(CampusPerformanceActivity.this).inflate(R.layout.filter_date_layout, null);
            mDatePopup = new PopupWindow(contentView);
            mDatePopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mDatePopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            //外部是否可以点击
            mDatePopup.setBackgroundDrawable(new BitmapDrawable());
            mDatePopup.setOutsideTouchable(true);

            TextView startDateTv = (TextView) contentView.findViewById(R.id.start_date);
            TextView endDateTv = (TextView) contentView.findViewById(R.id.end_date);
            View blankView = contentView.findViewById(R.id.blank_view_date);
            startDateTv.setOnClickListener(onClickListener);
            endDateTv.setOnClickListener(onClickListener);
            blankView.setOnClickListener(onClickListener);
        }
        mDatePopup.showAsDropDown(filertLayout);
        isDateShow = true;
        isCampusShow = false;
        isTypeShow = false;

        mDatePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isDateShow = false;
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            switch (view.getId()) {
                case R.id.filter_reset:
                    mCampusPopup.dismiss();
                    break;
                case R.id.filter_ok:
//                    mAdapter.updateDataSet(mAdapter.getSelectedItem());
//                    mAdapter.notifyDataSetChanged();
                    mCampusPopup.dismiss();
                    break;
                case R.id.blank_view_campus:
                    mCampusPopup.dismiss();
                    break;
                case R.id.performance_tv:
                    filertType.setText("业绩对比");
                    if (curFragment != lineChartFragment) {
                        ft.hide(curFragment);
                        if (lineChartFragment == null) {
                            lineChartFragment = LineChartFragment.newInstance();
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
                            barChartFragment = BarChartFragment.newInstance();
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
                    mDatePopup.dismiss();
                    break;
                case R.id.end_date:
                    mDatePopup.dismiss();
                    break;
                case R.id.blank_view_date:
                    mDatePopup.dismiss();
                    break;
            }
        }
    };
}
