package com.ishow.ischool.business.statistic.other;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ishow.ischool.bean.statistics.OtherStatisticsTable;

/**
 * Created by abel on 16/9/22.
 */

public class FragmentUtil {

    private Fragment mCurrentFragment;
    private AppCompatActivity mActivity;
    private BarChartFragment mBarchartFragment;
    private PieChartFragment mPiechartFragment;
    private int mContainerId;
    private FragmentTransaction ft;
    private OtherStatisticsTable table;

    public void init(AppCompatActivity activity, int containerId, OtherStatisticsTable table) {
        mActivity = activity;
        mContainerId = containerId;
        ft = mActivity.getSupportFragmentManager().beginTransaction();
        this.table = table;
    }

    public void showBarChartFragment() {
        if (mBarchartFragment == null) {
            mBarchartFragment = new BarChartFragment();
            mBarchartFragment.setBarData(table);
        }

        if (mCurrentFragment == null) {
            mCurrentFragment = mBarchartFragment;
            ft.add(mContainerId, mBarchartFragment);
            ft.commitAllowingStateLoss();
        } else {
            ft.hide(mPiechartFragment);
            ft.show(mBarchartFragment);
        }
    }

    public void showPieChartFragment() {
        if (mPiechartFragment == null) {
            mPiechartFragment = new PieChartFragment();
            mPiechartFragment.setPieData(table);
        }

        if (mCurrentFragment == null) {
            mCurrentFragment = mPiechartFragment;
            ft.add(mContainerId, mPiechartFragment);
            ft.commitAllowingStateLoss();
        } else {
            ft.hide(mBarchartFragment);
            ft.show(mPiechartFragment);
        }
    }

    public boolean isBarChart() {
        return mCurrentFragment instanceof BarChartFragment;
    }

    public void refresh(OtherStatisticsTable table) {
        if (mCurrentFragment instanceof BarChartFragment) {
            ((BarChartFragment) mCurrentFragment).setBarData(table);
        } else if (mCurrentFragment instanceof PieChartFragment) {
            ((PieChartFragment) mCurrentFragment).setPieData(table);
        }
    }
}
