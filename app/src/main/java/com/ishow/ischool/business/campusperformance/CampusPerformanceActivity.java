package com.ishow.ischool.business.campusperformance;

import android.graphics.Color;
import android.view.View;

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
import butterknife.OnClick;


/**
 * Created by wqf on 16/9/8.
 */
public class CampusPerformanceActivity extends BaseActivity4Crm<CampusPerformancePresenter, CampusPerformanceModel> implements CampusPerformanceContract.View {

    @BindView(R.id.combinedChart)
    CombinedChart mCombinedChart;

    private final int itemcount = 8;

    private final int mStart = 0;
    private final int mItemCount = 12;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_campusperformance, R.string.campus_performance, -1, MODE_BACK);
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
//        mCombinedChart.setDrawGridBackground(false);     //设置图表内格子背景是否显示，默认是false
//        mCombinedChart.setDrawBarShadow(false);         //是否显示阴影。启动它会降低约40%的性能，默认false
        mCombinedChart.setHighlightFullBarEnabled(false);

        // draw bars behind lines       调整实体显示顺序
        mCombinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        Legend legend = mCombinedChart.getLegend();      // 设置坐标线描述的样式
        legend.setEnabled(true);
        legend.setFormSize(10f);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setWordWrapEnabled(true);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);


        YAxis leftAxis = mCombinedChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);       //是否绘制坐标轴的线，即含有坐标的那条线，默认是true
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);     默认
//        leftAxis.setDrawGridLines(true);     //是否显示Y坐标轴上的刻度横线，默认是true
        leftAxis.setSpaceTop(20);           //Y轴坐标距顶有多少距离，即留白
        mCombinedChart.getAxisRight().setEnabled(false);    // 隐藏右边的坐标轴

        XAxis xAxis = mCombinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
       /* xAxis.setAxisMinValue(itemcount);
        xAxis.setAxisMaxValue(itemcount + 2);*/

        xAxis.setGranularity(0f);           // 设置轴最小间隔



        CombinedData data = new CombinedData();

        data.setData(generateLineData());
        data.setData(generateBarData());
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMaxValue(data.getXMax()+0.5f);

        xAxis.setLabelCount(13);
        xAxis.setLabelRotationAngle(-80);       //设置x轴字体显示角度
        mCombinedChart.getXAxis().setAxisMinValue(mStart);
        mCombinedChart.getXAxis().setAxisMaxValue(mStart + mItemCount + 1);

        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value==-1)
                    return "null";
                return mMonths[(int) value % mMonths.length];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        
        mCombinedChart.setData(data);
        mCombinedChart.invalidate();
    }

    private LineData generateLineData() {
        LineData lineData = new LineData();

        for (int z = 0; z < 2; z++) {
            ArrayList<Entry> entries = new ArrayList<Entry>();


        //ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < itemcount; index++)
            entries.add(new Entry(index+0.25f, getRandom(15, 5)));

            for (int index = 0; index < mStart + mItemCount; index++) {
                entries.add(new Entry(index + 1f, getRandom(15, 20)));
            }

            int color;
            if (z == 0) {
                color = Color.rgb(240, 128, 128);
            } else {
                color = Color.rgb(60, 220, 78);
            }

            LineDataSet set = new LineDataSet(entries, "Line DataSet" + (z + 1));
            set.setColor(color);
            set.setLineWidth(1f);
            set.setCircleColor(color);
            set.setCircleRadius(3f);
            set.setFillColor(color);
            set.setMode(LineDataSet.Mode.LINEAR);
            set.setDrawValues(true);
            set.setValueTextSize(10f);
            set.setValueTextColor(color);

            set.setAxisDependency(YAxis.AxisDependency.LEFT);     //以左边坐标轴为准 还是以右边坐标轴
            lineData.addDataSet(set);
        }

        return lineData;
    }

    private BarData generateBarData() {
        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();

        ArrayList<BarEntry> entries2 = new ArrayList<BarEntry>();

        for (int index = 0; index < itemcount; index++) {
            entries1.add(new BarEntry(index, getRandom(25, 25)));
        }
            // stacked
           // entries2.add(new BarEntry(0, new float[]{getRandom(13, 12), getRandom(13, 12)}));



        for (int index = 0; index < mStart + mItemCount; index++) {
            entries1.add(new BarEntry(index + 1f, getRandom(25, 25)));


        }

        BarDataSet set1 = new BarDataSet(entries1, "Bar 1");
        set1.setColor(Color.rgb(135,206,250));
        set1.setValueTextColor(Color.rgb(135,206,250));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);      //以左边坐标轴为准 还是以右边坐标轴为基准


        BarDataSet set2 = new BarDataSet(entries2, "");
        set2.setStackLabels(new String[]{"Stack 1", "Stack 2"});
        set2.setColors(new int[]{Color.rgb(61, 165, 255), Color.rgb(23, 197, 255)});
        set2.setValueTextColor(Color.rgb(61, 165, 255));
        set2.setValueTextSize(10f);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset

        //        float groupSpace = 0.06f;
//        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set1,set2);
        d.setBarWidth(barWidth);

        // make this BarData object grouped
        d.groupBars(0, groupSpace, barSpace); // start at x = 0

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

            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };


    @OnClick({R.id.filter1, R.id.filter2, R.id.filter3})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter1:
                break;
            case R.id.filter2:
                break;
            case R.id.filter3:
                break;
        }
    }

}
