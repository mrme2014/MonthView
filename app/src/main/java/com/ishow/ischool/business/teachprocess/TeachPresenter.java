package com.ishow.ischool.business.teachprocess;

import android.content.res.Resources;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.teachprocess.TeachProcess;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.util.AppUtil;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by MrS on 2016/10/9.
 */

public class TeachPresenter extends TeachProcessConact.Presenter implements OnChartValueSelectedListener {
    @Override
    public void getTeachProcessData(TreeMap<String, Integer> map) {
        mModel.getTeachProcessData(map).subscribe(new ApiObserver<TeachProcess>() {
            @Override
            public void onSuccess(TeachProcess process) {
                mView.getListSucess(process);
            }

            @Override
            public void onError(String msg) {
                mView.getListFaild(msg);
            }

            @Override
            protected boolean isAlive() {
                return mView != null && !mView.isActivityFinished();
            }
        });
    }


    public void initChart(HorizontalBarChart mChart) {
        mChart.setOnChartValueSelectedListener(this);
        // mChart.setHighlightEnabled(false);

        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
       // mChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        mChart.setDrawGridBackground(false);

        XAxis xl = mChart.getXAxis();
        xl.setEnabled(false);
     /*   xl.setPosition(XAxis.XAxisPosition.TOP);
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);
        xl.setLabelCount(5);
        xl.setXOffset(24);*/


        YAxis yl = mChart.getAxisLeft();
        yl.setSpaceBottom(0);
        yl.setAxisMinimum(0f);
        yl.setEnabled(false);

        YAxis yr = mChart.getAxisRight();
        yr.setAxisMinimum(0f);
        yr.setEnabled(false);

        mChart.setFitBars(true);
        mChart.animateY(2000);


        Legend l = mChart.getLegend();
        l.setEnabled(false);

    }

    public void setData(HorizontalBarChart mChart,ArrayList<BarEntry> yVals1) {

        float barWidth = 10f;
        BarDataSet set1;
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "DataSet 1");
            Resources resources = mChart.getContext().getResources();
            set1.setColors(resources.getColor(R.color.random_color14),
                    resources.getColor(R.color.random_color9),
                    resources.getColor(R.color.random_color12),
                    resources.getColor(R.color.chart_blue));
            set1.setDrawValues(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(barWidth);

            mChart.setData(data);
        }
    }


    public ArrayList<String> getSpinnerData() {
      return  AppUtil.getSpinnerData();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
