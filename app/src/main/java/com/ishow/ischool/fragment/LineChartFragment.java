package com.ishow.ischool.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonlib.core.BaseFragment;
import com.commonlib.util.LogUtil;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ishow.ischool.R;
import com.ishow.ischool.common.manager.CampusManager;

import java.util.ArrayList;

/**
 * Created by wqf on 16/9/13.
 */
public class LineChartFragment extends BaseFragment {

    private CombinedChart mCombinedChart;
    private PieChart mPieChart;
    private TextView titleTv, subtitleTv, switcTv;
    private boolean curPieMode = false;
    private ArrayList<String> mCampusDatas;
    private int mCount = 0;

    protected String[] mParties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

//    public static LineChartFragment newInstance(String campus_id, String source) {
    public static LineChartFragment newInstance() {
        LineChartFragment fragment = new LineChartFragment();
        Bundle args = new Bundle();
//        args.putString("campus_id", campus_id);
//        args.putString("source", source);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
//            mCampusId = bundle.getString("campus_id", "");
//            mSource = bundle.getString("source", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_linechart, null);
        mCombinedChart = (CombinedChart) view.findViewById(R.id.combinedChart);
        mPieChart = (PieChart) view.findViewById(R.id.pieChart);
        titleTv = (TextView) view.findViewById(R.id.chart_title);
        subtitleTv = (TextView) view.findViewById(R.id.subtitle);
        switcTv = (TextView) view.findViewById(R.id.chart_switch);
        switcTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curPieMode) {
                    curPieMode = false;
                    mCombinedChart.setVisibility(View.VISIBLE);
                    mPieChart.setVisibility(View.GONE);
                    switcTv.setText("饼图");
                } else {
                    curPieMode = true;
                    mPieChart.setVisibility(View.VISIBLE);
                    mCombinedChart.setVisibility(View.GONE);
                    switcTv.setText("折线图");
                }
            }
        });
        return view;
    }

    @Override
    public void init() {
        setData();
        initCombinedChart();
        initPieChart();
    }

    public void setData() {
        mCampusDatas = new ArrayList<>();
        mCampusDatas.add("");
        mCampusDatas.addAll(CampusManager.getInstance().getCampusNames());
        mCount = mCampusDatas.size();
    }

    void initCombinedChart() {
        mCombinedChart.setDescription("");
        mCombinedChart.setBackgroundColor(Color.WHITE);
//        mCombinedChart.setDrawGridBackground(false);     //设置图表内格子背景是否显示，默认是false
//        mCombinedChart.setDrawBarShadow(false);         //是否显示阴影。启动它会降低约40%的性能，默认false

        // draw bars behind lines       调整实体显示顺序
        mCombinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        //设置y轴的样式
        YAxis leftAxis = mCombinedChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);       //是否绘制坐标轴的线，即含有坐标的那条线，默认是true
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);     默认
//        leftAxis.setDrawGridLines(true);     //是否显示Y坐标轴上的刻度横线，默认是true
        leftAxis.setXOffset(10f);
        leftAxis.setAxisMinValue(0f);
        mCombinedChart.getAxisRight().setEnabled(false);    // 隐藏右边的坐标轴

        //设置x轴的样式
        XAxis xAxis = mCombinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);      //是否显示X坐标轴上的刻度竖线，默认是true
        xAxis.setGranularity(0f);           // 设置轴最小间隔
        xAxis.setAxisMinValue(0f);

//        xAxis.setLabelCount(mCount, false);
//        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setLabelRotationAngle(-80);       //设置x轴字体显示角度

        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                LogUtil.d("LineChartFragment value = " + value);
                return mCampusDatas.get((int) value % mCampusDatas.size());
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        CombinedData data = new CombinedData();
        data.setData(generateLineData());
        data.setData(generateBarData());
        mCombinedChart.setData(data);

        mCombinedChart.setVisibleXRangeMaximum(8);//设置屏幕显示条数
        mCombinedChart.invalidate();

        Legend legend = mCombinedChart.getLegend();      // 设置坐标线描述的样式
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setWordWrapEnabled(true);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
    }

    private void initPieChart() {
        mPieChart.setUsePercentValues(true);
        mPieChart.setDescription("");
//        mPieChart.setExtraOffsets(5, 10, 5, 5);     //设置图表外，布局内显示的偏移量

        setData(4, 100);

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setEntryLabelTextSize(12f);
    }

    private LineData generateLineData() {
        LineData lineData = new LineData();
        for (int z = 0; z < 2; z++) {
            ArrayList<Entry> entries = new ArrayList<Entry>();

            for (int index = 0; index < mCount - 1; index++) {
                entries.add(new Entry(index + 1f, getRandom(10, 20)));
            }

            int color;
            if (z == 0) {
                color = getResources().getColor(R.color.chart_red);
            } else {
                color = getResources().getColor(R.color.chart_green);
            }

            LineDataSet set = new LineDataSet(entries, "Line DataSet" + (z + 1));
            set.setColor(color);
            set.setLineWidth(1f);
            set.setCircleColor(color);
            set.setCircleRadius(3f);
            set.setFillColor(color);
            set.setMode(LineDataSet.Mode.LINEAR);
            set.setValueTextSize(10f);
            set.setValueTextColor(color);
            set.setAxisDependency(YAxis.AxisDependency.LEFT);     //以左边坐标轴为准 还是以右边坐标轴
            set.setDrawValues(false);
            set.removeLast();

            lineData.addDataSet(set);
        }

        return lineData;
    }

    private BarData generateBarData() {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int index = 0; index < mCount; index++) {
            entries.add(new BarEntry(index + 1f, getRandom(15, 25)));
        }

        BarDataSet set = new BarDataSet(entries, "Bar 1");
        set.setColor(getResources().getColor(R.color.chart_blue));
//        set.setValueTextColor(R.color.chart_blue);
//        set.setValueTextSize(10f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);      //以左边坐标轴为准 还是以右边坐标轴为基准
        set.setDrawValues(false);
        set.removeLast();

        BarData d = new BarData(set);
        d.setBarWidth(0.3f);

        return d;
    }

    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }


    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5), mParties[i % mParties.length]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
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
        mPieChart.setData(data);

        // undo all highlights
        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }

}
