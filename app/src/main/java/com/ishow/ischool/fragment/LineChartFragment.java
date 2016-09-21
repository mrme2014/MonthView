package com.ishow.ischool.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonlib.core.BaseFragment;
import com.commonlib.http.ApiFactory;
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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.campusperformance.SignPerformance;
import com.ishow.ischool.bean.campusperformance.SignPerformanceResult;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.DataApi;
import com.ishow.ischool.common.manager.CampusManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/9/13.
 */
public class LineChartFragment extends BaseFragment {

    @BindView(R.id.combinedChart)
    CombinedChart mCombinedChart;
    @BindView(R.id.pieChart)
    PieChart mPieChart;
    @BindView(R.id.title)
    TextView titleTv;
    @BindView(R.id.subtitle)
    TextView subtitleTv;
    @BindView(R.id.chart_switch)
    TextView switcTv;
    @BindView(R.id.legend_layout)
    LinearLayout legendLayout;
    @BindView(R.id.legend_base_performance)
    CheckedTextView baseCtv;
    @BindView(R.id.legend_challenge_performance)
    CheckedTextView challengeCtv;

    public boolean curPieMode = false;
    private ArrayList<String> mCampusDatas;
    private ArrayList<CampusInfo> mCampusInfos;
    private ArrayList<SignPerformance> mOrignData;
    private int mCount = 0;
    private LineData lineData = new LineData();
    private LineDataSet baseLineDataSet, challengeLineDataSet;
    private BarData barData = new BarData();
    private BarDataSet barDataSet;
    private CombinedData combinedData = new CombinedData();

    protected String[] mParties = new String[]{
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
        ApiFactory.getInstance().getApi(DataApi.class).getSignPerformance(1, "2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17",
                201605, null, null, "campusTotal")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<SignPerformanceResult>() {
                    @Override
                    public void onSuccess(SignPerformanceResult result) {
                        mOrignData = result.campusTotal;
                        initCombinedChart();
                    }

                    @Override
                    public void onError(String msg) {
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_linechart, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        initPieChart();
    }

    public void setData() {
        mCampusInfos = CampusManager.getInstance().get();
        mCampusDatas = new ArrayList<>();
        mCampusDatas.add("");
        for (CampusInfo campusInfo : mCampusInfos) {
            mCampusDatas.add(campusInfo.name);
        }
        mCount = mCampusDatas.size();
    }

    void initCombinedChart() {
        mCombinedChart.setScaleEnabled(false);
        mCombinedChart.setDescription("");
        mCombinedChart.setBackgroundColor(Color.WHITE);
//        mCombinedChart.setDrawGridBackground(false);     //设置图表内格子背景是否显示，默认是false
//        mCombinedChart.setDrawBarShadow(false);         //是否显示阴影。启动它会降低约40%的性能，默认false

        // draw bars behind lines       调整实体显示顺序
        mCombinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        mCombinedChart.getLegend().setEnabled(false);

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
//        xAxis.setXOffset(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);      //是否显示X坐标轴上的刻度竖线，默认是true
        xAxis.setGranularity(0f);           // 设置轴最小间隔
        xAxis.setAxisMinValue(0f);
        xAxis.setLabelRotationAngle(-60);       //设置x轴字体显示角度
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

        setLineChartData(null);
    }

    public void setLineChartData(ArrayList<Integer> showPosition) {
        ArrayList<SignPerformance> datas = new ArrayList<>();
        if (showPosition != null) {
            mCampusDatas.clear();
            mCampusDatas.add("");
            for (int i : showPosition) {
                mCampusDatas.add(mCampusInfos.get(i).name);
                datas.add(mOrignData.get(i));
            }
            mCount = mCampusDatas.size();
            lineData.removeDataSet(mCombinedChart.getLineData().getDataSetByLabel(getString(R.string.base_performance), true));
            lineData.removeDataSet(mCombinedChart.getLineData().getDataSetByLabel(getString(R.string.challenge_performance), true));
            barData.removeDataSet(mCombinedChart.getBarData().getDataSetByLabel(getString(R.string.performance), true));
            mCombinedChart.clearValues();
        } else {
            datas.addAll(mOrignData);
        }

        lineData.addDataSet(generateBaseLineData(datas));
        lineData.addDataSet(generateChallengeLineData(datas));
        barData.addDataSet(generateBarData(datas));
        barData.setBarWidth(0.3f);
        combinedData.setData(lineData);
        combinedData.setData(barData);
        mCombinedChart.setData(combinedData);

        mCombinedChart.fitScreen();
        //  避免小数,原理不清
        mCombinedChart.animateXY(1000, 1000);
        if (mCount < 7) {
            mCombinedChart.getXAxis().setLabelCount(mCount);
        }
        mCombinedChart.setVisibleXRangeMaximum(mCount > 7 ? 7 : mCount);      //设置屏幕显示条数
        mCombinedChart.invalidate();
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

    private LineDataSet generateBaseLineData(ArrayList<SignPerformance> campusPerformances) {
        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < mCount - 1; index++) {
            entries.add(new Entry(index + 1f, Float.parseFloat(campusPerformances.get(index).perweek_full_base)));
        }

        int color = getResources().getColor(R.color.chart_red);

        baseLineDataSet = new LineDataSet(entries, getString(R.string.base_performance));
        baseLineDataSet.setColor(color);
        baseLineDataSet.setLineWidth(1f);
        baseLineDataSet.setCircleColor(color);
        baseLineDataSet.setCircleRadius(3f);
        baseLineDataSet.setFillColor(color);
        baseLineDataSet.setMode(LineDataSet.Mode.LINEAR);
        baseLineDataSet.setValueTextSize(10f);
        baseLineDataSet.setValueTextColor(color);
        baseLineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);     //以左边坐标轴为准 还是以右边坐标轴
        baseLineDataSet.setDrawValues(false);

        return baseLineDataSet;
    }

    private LineDataSet generateChallengeLineData(ArrayList<SignPerformance> campusPerformances) {
        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < mCount - 1; index++) {
            entries.add(new Entry(index + 1f, Float.parseFloat(campusPerformances.get(index).perweek_full_challenge)));
        }

        int color = getResources().getColor(R.color.chart_green);

        challengeLineDataSet = new LineDataSet(entries, getString(R.string.challenge_performance));
        challengeLineDataSet.setColor(color);
        challengeLineDataSet.setLineWidth(1f);
        challengeLineDataSet.setCircleColor(color);
        challengeLineDataSet.setCircleRadius(3f);
        challengeLineDataSet.setFillColor(color);
        challengeLineDataSet.setMode(LineDataSet.Mode.LINEAR);
        challengeLineDataSet.setValueTextSize(10f);
        challengeLineDataSet.setValueTextColor(color);
        challengeLineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);     //以左边坐标轴为准 还是以右边坐标轴
        challengeLineDataSet.setDrawValues(false);

        return challengeLineDataSet;
    }

    private BarDataSet generateBarData(ArrayList<SignPerformance> campusPerformances) {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int index = 0; index < mCount; index++) {
            if (index == mCount - 1) {
                entries.add(new BarEntry(index + 1f, 0));
                continue;
            }
            entries.add(new BarEntry(index + 1f, Float.parseFloat(campusPerformances.get(index).perweek_real)));
        }

        barDataSet = new BarDataSet(entries, getString(R.string.performance));
        barDataSet.setColor(getResources().getColor(R.color.chart_blue));
//        set.setValueTextColor(R.color.chart_blue);
//        set.setValueTextSize(10f);
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);      //以左边坐标轴为准 还是以右边坐标轴为基准
        barDataSet.setDrawValues(false);

        return barDataSet;
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count; i++) {
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


    @OnClick({R.id.chart_switch, R.id.legend_base_performance, R.id.legend_challenge_performance})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_switch:
                if (curPieMode) {
                    curPieMode = false;
                    mCombinedChart.setVisibility(View.VISIBLE);
                    legendLayout.setVisibility(View.VISIBLE);
                    mPieChart.setVisibility(View.GONE);
                    switcTv.setText("饼图");
                } else {
                    curPieMode = true;
                    mPieChart.setVisibility(View.VISIBLE);
                    mCombinedChart.setVisibility(View.GONE);
                    legendLayout.setVisibility(View.GONE);
                    switcTv.setText("折线图");
                }
                break;
            case R.id.legend_base_performance:
                invalidateBasePerformance();
                break;
            case R.id.legend_challenge_performance:
                invalidateChallengePerformance();
                break;
        }
    }

    private ILineDataSet baseDataSet, challengeDataSet;
    private LineData tempLineData;
    private void invalidateBasePerformance() {
        if (tempLineData == null) {
            tempLineData = mCombinedChart.getLineData();
        }
        if (!baseCtv.isChecked()) {
            baseDataSet = tempLineData.getDataSetByLabel(getString(R.string.base_performance), true);
            lineData.removeDataSet(baseDataSet);
            baseCtv.setChecked(true);
        } else {
            lineData.addDataSet(baseDataSet);
            baseCtv.setChecked(false);
        }
        mCombinedChart.invalidate();
    }

    private void invalidateChallengePerformance() {
        if (lineData == null) lineData = mCombinedChart.getLineData();
        if (!challengeCtv.isChecked()) {
            challengeDataSet = lineData.getDataSetByLabel(getString(R.string.challenge_performance), true);
            lineData.removeDataSet(challengeDataSet);
            challengeCtv.setChecked(true);

        } else {
            lineData.addDataSet(challengeDataSet);
            challengeCtv.setChecked(false);
        }
        mCombinedChart.invalidate();
    }

}
