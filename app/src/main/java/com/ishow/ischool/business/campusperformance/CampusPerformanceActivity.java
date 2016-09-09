package com.ishow.ischool.business.campusperformance;

import android.graphics.Color;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.ishow.ischool.R;
import com.ishow.ischool.common.base.BaseActivity4Crm;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * Created by wqf on 16/9/8.
 */
public class CampusPerformanceActivity extends BaseActivity4Crm<CampusPerformancePresenter, CampusPerformanceModel> implements CampusPerformanceContract.View {

    @BindView(R.id.combinedChart)
    CombinedChart mCombinedChart;
    private final int itemcount = 12;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_campusperformance, R.string.campus_performance, R.menu.menu_statisticslist, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        initChart();
    }

    @Override
    protected void setUpData() {

    }

    void initChart() {
        mCombinedChart.setDescription("");
        mCombinedChart.setBackgroundColor(Color.WHITE);
        mCombinedChart.setDrawGridBackground(false);
        mCombinedChart.setDrawBarShadow(false);
        mCombinedChart.setHighlightFullBarEnabled(false);

        // draw bars behind lines
        mCombinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        Legend l = mCombinedChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        YAxis leftAxis = mCombinedChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(10f);
        leftAxis.setDrawGridLines(true);     // 是否绘制网格轴线
//        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        mCombinedChart.getAxisRight().setEnabled(false);    // 隐藏右边的坐标轴

        XAxis xAxis = mCombinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
//        xAxis.setAxisMinValue(itemcount);
//        xAxis.setAxisMaxValue(itemcount + 2);
//        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(0f);           // 设置轴最小间隔
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mMonths[(int) value % mMonths.length];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        CombinedData data = new CombinedData();

        data.setData(generateLineData());
        data.setData(generateBarData());

//        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        mCombinedChart.setData(data);
        mCombinedChart.invalidate();
    }

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < itemcount; index++)
            entries.add(new Entry(index, getRandom(15, 5)));

        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.rgb(240, 238, 70));
        set.setLineWidth(1f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(3f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData() {
        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();
//        ArrayList<BarEntry> entries2 = new ArrayList<BarEntry>();

        for (int index = 0; index < itemcount; index++) {
            entries1.add(new BarEntry(index, getRandom(25, 25)));

            // stacked
//            entries2.add(new BarEntry(0, new float[]{getRandom(13, 12), getRandom(13, 12)}));
        }

        BarDataSet set1 = new BarDataSet(entries1, "Bar 1");
        set1.setColor(Color.rgb(60, 220, 78));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

//        BarDataSet set2 = new BarDataSet(entries2, "");
//        set2.setStackLabels(new String[]{"Stack 1", "Stack 2"});
//        set2.setColors(new int[]{Color.rgb(61, 165, 255), Color.rgb(23, 197, 255)});
//        set2.setValueTextColor(Color.rgb(61, 165, 255));
//        set2.setValueTextSize(10f);
//        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        // make this BarData object grouped
//        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        return d;
    }

    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

    //    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_filter:
//                if (dialog == null) {
//                    dialog = StatisticsFilterFragment.newInstance(filterParams, mFilterSourceName, mFilterCollegeName, mFilterReferrerName);
//                    dialog.setOnFilterCallback(CampusPerformanceActivity.this);
//                }
//                dialog.show(getSupportFragmentManager(), "dialog");
//                break;
//        }
//        return true;
//    }

    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };
}
