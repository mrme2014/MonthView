package com.ishow.ischool.business.statistic.other;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.statistics.OtherStatistics;
import com.ishow.ischool.bean.statistics.OtherStatisticsTable;
import com.ishow.ischool.common.base.BaseFragment4Crm;

import java.util.ArrayList;

/**
 * Created by abel on 16/9/22.
 */

public class BarChartFragment extends BaseFragment4Crm {


    //    @BindView(R.id.bar_chart)
    BarChart mBarChart;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bar_chart;
    }

    @Override
    public void init() {
        initBarChart();
    }


    public void initBarChart() {
//        mBarChart.setOnChartGestureListener(this);
//        mBarChart.setOnChartValueSelectedListener(this);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setBorderColor(Color.WHITE);

        // no description text
        mBarChart.setDescription("");
        mBarChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mBarChart.setTouchEnabled(true);

        // enable scaling and dragging
        mBarChart.setDragEnabled(true);
        mBarChart.setScaleEnabled(true);
        // mBarChart.setScaleXEnabled(true);
        // mBarChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mBarChart.setPinchZoom(true);

        // set an alternative background color
        mBarChart.setBackgroundColor(Color.WHITE);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(13);
        xAxis.setLabelRotationAngle(-45);


        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mBarChart.getAxisRight().setEnabled(false);


        // add data
//        setBarData(mTableData);

        mBarChart.animateX(2500);

        // get the legend (only possible after setting data)
        Legend l = mBarChart.getLegend();

        // modify the legend ...
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setForm(Legend.LegendForm.LINE);

        // // dont forget to refresh the drawing
        // mBarChart.invalidate();
    }

    public void setBarData(OtherStatisticsTable table) {
        if (table == null) {
            return;
        }

        ArrayList<OtherStatistics> others = table.data;

        float start = 0f;
        int count = others.size();

        mBarChart.getXAxis().setAxisMinValue(start);
        mBarChart.getXAxis().setAxisMaxValue(start + count + 1);

        CampusIAxisValueFormatter formatter = new CampusIAxisValueFormatter(others);
        mBarChart.getXAxis().setValueFormatter(formatter);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < count; i++) {
            yVals1.add(new BarEntry(i + 1, others.get(i).value));
        }

        BarDataSet set1;

        if (mBarChart.getData() != null &&
                mBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            mBarChart.setData(data);
        }
    }
}
