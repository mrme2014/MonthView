package com.ishow.ischool.business.statistic.other;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.statistics.OtherStatistics;
import com.ishow.ischool.bean.statistics.OtherStatisticsTable;
import com.ishow.ischool.common.base.BaseFragment4Crm;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by abel on 16/9/22.
 */

public class PieChartFragment extends BaseFragment4Crm {


    @BindView(R.id.pie_chart)
    PieChart mPieChart;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pie_chart;
    }

    @Override
    public void init() {
        initPieChar();
    }

    public void initPieChar() {
        //mPieChart = (PieChart) findViewById(R.id.pie_chart);
        mPieChart.setUsePercentValues(true);
        mPieChart.setDescription("");
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);

//        mPieChart.setCenterTextTypeface(mTfLight);
//        mPieChart.setCenterText(generateCenterSpannableText());

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);

        // mBarChart.setUnit(" €");
        // mBarChart.setDrawUnitsInChart(true);

        // add a selection listener
//        mPieChart.setOnChartValueSelectedListener(this);

//        setPieData(mTableData);

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mBarChart.spin(2000, 0, 360);

//        mSeekBarX.setOnSeekBarChangeListener(this);
//        mSeekBarY.setOnSeekBarChangeListener(this);

        Legend l = mPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setEntryLabelTextSize(12f);
    }

    public void setPieData(OtherStatisticsTable table) {
        if (table == null) {
            return;
        }

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        ArrayList<OtherStatistics> others = table.data;

        int value = 0;
        for (int i = 0; i < others.size(); i++) {
            value += others.get(i).value;
        }

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < others.size(); i++) {
            entries.add(new PieEntry(((float) others.get(i).value) / value, others.get(i).name));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(mTfLight);
        mPieChart.setData(data);

        // undo all highlights
        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }
}
