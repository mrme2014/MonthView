package com.ishow.ischool.business.tabdata;

import android.content.res.Resources;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.teachprocess.TeachProcess;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by abel on 16/10/9.
 */

public class DataTeachPresenter extends DataTeachContract.Presenter implements OnChartValueSelectedListener {
    @Override
    void getTeachingProcess(TreeMap<String, Integer> params) {
        mModel.getTeachingProcess(params).subscribe(new ApiObserver<TeachProcess>() {
            @Override
            public void onSuccess(TeachProcess process) {
                mView.getTeachingProcessSucess(process);
            }

            @Override
            public void onError(String msg) {
                mView.getTeachingProcessFaild(msg);
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
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(1f);
        xl.setAxisMinimum(0.5f);

        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);
        yl.setDrawLabels(false);

        YAxis yr = mChart.getAxisRight();
        yr.setDrawAxisLine(false);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);
        yr.setDrawLabels(false);


        mChart.setFitBars(true);
        mChart.animateY(2000);


        Legend l = mChart.getLegend();
        l.setEnabled(false);

    }

    public void setData(HorizontalBarChart mChart, TeachProcess process) {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        List<String> heads = process.selfChartData.head;
        List<String> bodys = process.selfChartData.body.get(0);

        for (int i = 0; i < heads.size() - 2; i++) {
            yVals1.add(new BarEntry(i + 1, Integer.parseInt(bodys.get(i))));
        }
        if (yVals1.isEmpty()) {
            mChart.clear();
            return;
        }

        XAxis xl = mChart.getXAxis();
        xl.setValueFormatter(new ProcessIAxisValueFormatter(heads));
        float barWidth = 1f;
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
            set1.setDrawValues(true);
            set1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return (int) value + "人";
                }
            });
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(barWidth);

            mChart.setData(data);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
