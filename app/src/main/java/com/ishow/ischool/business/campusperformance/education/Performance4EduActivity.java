package com.ishow.ischool.business.campusperformance.education;

import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commonlib.util.LogUtil;
import com.commonlib.widget.pull.BaseItemDecor;
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
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.CampusSelectAdapter;
import com.ishow.ischool.bean.campusperformance.EducationMonth;
import com.ishow.ischool.bean.campusperformance.EducationMonthResult;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.CampusManager;
import com.ishow.ischool.widget.custom.ListViewForScrollView;
import com.ishow.ischool.widget.table.MyMarkerView4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

import static com.ishow.ischool.R.string.performance;

/**
 * Created by mini on 16/10/10.
 */

public class Performance4EduActivity extends BaseActivity4Crm<Performance4EduPresenter, Performance4EduModel> implements Performance4EduContract.View {
    @BindView(R.id.filter_layout)
    LinearLayout filertLayout;
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
    @BindView(R.id.combinedChart_layout)
    LinearLayout combinedChartLayout;
    @BindView(R.id.legend_layout)
    LinearLayout legendLayout;
    @BindView(R.id.legend_base_performance)
    CheckedTextView baseCtv;
    @BindView(R.id.legend_challenge_performance)
    CheckedTextView challengeCtv;
    @BindView(R.id.chart_table)
    ListViewForScrollView listViewForScrollView;
    private Performance4EduTableAdaper mTableAdapter;
    private View headerView;

    public boolean curPieMode = false;
    private ArrayList<String> mXDatas;      // 横坐标数据,需要显示的校区name
    private int mCount = 0;                 // 横坐标个数,即数据个数
    public ArrayList<CampusInfo> mAllCampus;           // 此Activity所有校区
    public ArrayList<CampusInfo> mLastCampus;      // 上次显示的校区
    public ArrayList<EducationMonth> mLastYdatas;      // 上次显示的数据(纵坐标)
    public String mCampusParam = "";                     // 所有校区id,用于每次请求所有校区的数据
    public String mLastCampusParam = "";                 // 上次显示的校区id,用于传递给表格用
    public ArrayList<EducationMonth> mYDatas;      // 纵坐标数据,即每个校区的数据
    public int mParamBeginDate, mParamEndDate;

    private LineData lineData = new LineData();
    private LineDataSet baseLineDataSet, challengeLineDataSet;
    private BarData barData = new BarData();
    private BarDataSet barDataSet;
    private CombinedData combinedData = new CombinedData();

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_edu_performance, R.string.campus_performance, -1, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        initData();
        initTable();
        initCombinedChart();
        initPieChart();
    }

    @Override
    protected void setUpData() {
        mPresenter.getEduMonthPerformance(mCampusParam, mParamBeginDate, mParamEndDate);
    }

    public void initData() {
        initDefaultDate();
        mAllCampus = new ArrayList<>();
        mLastCampus = new ArrayList<>();
        mLastYdatas = new ArrayList<>();
        mXDatas = new ArrayList<>();
        ArrayList<CampusInfo> mAllCampusInfos = CampusManager.getInstance().get();
        for (int i = 0; i < mAllCampusInfos.size(); i++) {
            if ((mAllCampusInfos.get(i).id >= 2 && mAllCampusInfos.get(i).id <= 5) || (mAllCampusInfos.get(i).id >= 7 && mAllCampusInfos.get(i).id <= 16)) {
                mAllCampus.add(mAllCampusInfos.get(i));
                mXDatas.add(mAllCampusInfos.get(i).name);
            }
        }
        mCount = mXDatas.size();

        // 初始获取所有校区id
        for (CampusInfo info : mAllCampus) {
            mCampusParam = mCampusParam + info.id + ",";
        }
        mCampusParam = mCampusParam.substring(0, mCampusParam.length() - 1);
    }

    @Override
    public void getListSuccess(EducationMonthResult educationMonthResult) {
        lazyShow();
        mYDatas = educationMonthResult.educationMonth;
        Collections.sort(mYDatas, new Comparator<EducationMonth>() {
            public int compare(EducationMonth arg0, EducationMonth arg1) {
                return (arg0.campusid).compareTo(arg1.campusid);
            }
        });
        setLineChartData(mAllCampus);
        setPieChartData(mAllCampus);
    }

    @Override
    public void getListFail(String msg) {

    }

    /**
     * 懒加载，避免数据没有加载成功，点击崩溃
     */
    void lazyShow() {
        if (legendLayout.getVisibility() != View.VISIBLE) {
            legendLayout.setVisibility(View.VISIBLE);
        }
        if (listViewForScrollView.getVisibility() != View.VISIBLE) {
            listViewForScrollView.setVisibility(View.VISIBLE);
        }
    }

    void initDefaultDate() {
        calendar = Calendar.getInstance();      //初始化日历类
        int curYear = calendar.get(Calendar.YEAR);
        String startMonth = calendar.get(Calendar.MONTH) + "";
        if (Integer.parseInt(startMonth) == 0) {
            curYear = curYear - 1;
            startMonth = "12";
        } else if (Integer.parseInt(startMonth) < 10) {
            startMonth = "0" + startMonth;
        }
        mFilterStartTime = curYear + startMonth;
        String endMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1); //得到月，因为从0开始的，所以要加1
        if (Integer.parseInt(endMonth) < 10) {
            endMonth = "0" + endMonth;
        }
        mFilterEndTime = curYear + endMonth;
        mParamBeginDate = Integer.parseInt(curYear + startMonth);
        mParamEndDate = Integer.parseInt(curYear + endMonth);
    }

    private void initTable() {
        mTableAdapter = new Performance4EduTableAdaper(this, mXDatas, mLastYdatas);
        if (headerView == null) {
            headerView = getLayoutInflater().inflate(R.layout.item_performance_education_table_head, null);
            listViewForScrollView.addHeaderView(headerView);
        }
        TextView nameTv = (TextView) headerView.findViewById(R.id.item_name);
        TextView challengeTv = (TextView) headerView.findViewById(R.id.item_challenge);
        TextView baseTv = (TextView) headerView.findViewById(R.id.item_base);
        TextView realTv = (TextView) headerView.findViewById(R.id.item_real);
        nameTv.setText("校区");
        challengeTv.setText("冲刺目标");
        baseTv.setText("红线目标");
        realTv.setText("当前业绩");

        listViewForScrollView.setAdapter(mTableAdapter);
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
        xAxis.setGranularity(1f);           // 设置轴最小间隔
        xAxis.setAxisMinimum(0f);
        xAxis.setLabelRotationAngle(-60);       //设置x轴字体显示角度
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                LogUtil.d("LineChartFragment value = " + value);
                if (value >= 0 && value < mCount) {
                    return mXDatas.get((int) value);        // % mXDatas.size()
                } else {
                    return "";
                }
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
    }


    private void initPieChart() {
        mPieChart.setUsePercentValues(true);
        mPieChart.setDescription("");
        mPieChart.setExtraOffsets(25, 10, 25, 10);     //设置图表外，布局内显示的偏移量

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

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(5f);
        l.setWordWrapEnabled(true);

        // entry label styling
        mPieChart.setEntryLabelColor(getResources().getColor(R.color.txt_6));
        mPieChart.setEntryLabelTextSize(10f);
    }

    /**
     * @param campusInfos // 本次需要显示的校区
     */
    public void setLineChartData(ArrayList<CampusInfo> campusInfos) {
        ArrayList<CampusInfo> tempCampusInfos = new ArrayList<>();
        tempCampusInfos.addAll(campusInfos);
        mLastYdatas.clear();
        if (mLastCampus != null && mLastCampus.size() > 0) {      // 即不是第一次
            mXDatas.clear();
            for (CampusInfo campusInfo : tempCampusInfos) {
                mXDatas.add(campusInfo.name);
                mLastYdatas.add(mYDatas.get(mAllCampus.indexOf(campusInfo)));
            }
            mCount = mXDatas.size();
            lineData.removeDataSet(mCombinedChart.getLineData().getDataSetByLabel(getString(R.string.base_performance), true));
            lineData.removeDataSet(mCombinedChart.getLineData().getDataSetByLabel(getString(R.string.challenge_performance), true));
            barData.removeDataSet(mCombinedChart.getBarData().getDataSetByLabel(getString(performance), true));
            mCombinedChart.clearValues();
        } else {
            mLastYdatas.addAll(mYDatas);
        }
        mLastCampus.clear();         // 更新上一次显示的校区
        mLastCampus.addAll(tempCampusInfos);
        // 刷新table
        mTableAdapter.notifyDataSetChanged();

        lineData.addDataSet(generateBaseLineData(mLastYdatas));
        lineData.addDataSet(generateChallengeLineData(mLastYdatas));
        barData.addDataSet(generateBarData(mLastYdatas));
        barData.setBarWidth(0.3f);
        combinedData.setData(lineData);
        combinedData.setData(barData);
        mCombinedChart.setData(combinedData);

        mCombinedChart.fitScreen();
        //  避免小数,原理不清
        mCombinedChart.animateXY(1000, 1000);

        if (mCount < 7) {
            mCombinedChart.getXAxis().setLabelCount(mCount + 1);
        }
        if (mCount != 1) {
            mCombinedChart.setVisibleXRangeMaximum(mCount > 7 ? 7 : mCount);      //设置屏幕显示条数
        }
        mCombinedChart.getXAxis().setGranularity(1f);
        mCombinedChart.invalidate();
        setChartMarkView(mLastYdatas);
    }

    void setChartMarkView(ArrayList<EducationMonth> datas) {
        MyMarkerView4 mv = new MyMarkerView4(Performance4EduActivity.this, !baseCtv.isChecked(), !challengeCtv.isChecked(), mXDatas, datas);
        mv.setChartView(mCombinedChart); // For bounds control
        mCombinedChart.setMarker(mv);
    }

    private LineDataSet generateBaseLineData(ArrayList<EducationMonth> campusPerformances) {
        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < campusPerformances.size(); index++) {
            entries.add(new Entry(index, Float.parseFloat(campusPerformances.get(index).full_base)));
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

    private LineDataSet generateChallengeLineData(ArrayList<EducationMonth> campusPerformances) {
        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < campusPerformances.size(); index++) {
            entries.add(new Entry(index, Float.parseFloat(campusPerformances.get(index).full_challenge)));
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

    private BarDataSet generateBarData(ArrayList<EducationMonth> campusPerformances) {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int index = 0; index < campusPerformances.size(); index++) {
            entries.add(new BarEntry(index, Float.parseFloat(campusPerformances.get(index).permonth_real)));
        }

        barDataSet = new BarDataSet(entries, getString(performance));
        barDataSet.setColor(getResources().getColor(R.color.chart_blue));
//        set.setValueTextColor(R.color.chart_blue);
//        set.setValueTextSize(10f);
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);      //以左边坐标轴为准 还是以右边坐标轴为基准
        barDataSet.setDrawValues(false);

        return barDataSet;
    }


    public void setPieChartData(ArrayList<CampusInfo> campusInfos) {
        // 刷新上次显示(id)
        mLastCampusParam = "";
        for (CampusInfo info : campusInfos) {
            mLastCampusParam = mLastCampusParam + info.id + ",";
        }
        mLastCampusParam = mLastCampusParam.substring(0, mLastCampusParam.length() - 1);


        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        float total = 0;
        for (int i = 0; i < campusInfos.size(); i++) {
            int position = mAllCampus.indexOf(campusInfos.get(i));
            total = total + Float.parseFloat(mYDatas.get(position).permonth_real);
        }
        for (int j = 0; j < campusInfos.size(); j++) {
            int position = mAllCampus.indexOf(campusInfos.get(j));
            double result = Math.round((Float.parseFloat(mYDatas.get(position).permonth_real) / total) * 10000) / 10000.0;     // 保留4位小数(*100后保留2位小数)
            if (result > 0) {
                double f = result * 100;
                LogUtil.d("double = " + f);
                BigDecimal bigDecimal = new BigDecimal(String.valueOf(f));
                LogUtil.d("bigDecimal = " + bigDecimal.toString());
                LogUtil.d("float = " + Float.parseFloat(bigDecimal.toString()));
                // 这里已经是2位小数了,饼状图只支持1位小数显示
                entries.add(new PieEntry(Float.parseFloat(bigDecimal.toString()), campusInfos.get(j).name));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLinePart1OffsetPercentage(80f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueTextColor(Color.WHITE);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.random_color1));
        colors.add(getResources().getColor(R.color.random_color2));
        colors.add(getResources().getColor(R.color.random_color3));
        colors.add(getResources().getColor(R.color.random_color4));
        colors.add(getResources().getColor(R.color.random_color5));
        colors.add(getResources().getColor(R.color.random_color6));
        colors.add(getResources().getColor(R.color.random_color7));
        colors.add(getResources().getColor(R.color.random_color8));
        colors.add(getResources().getColor(R.color.random_color9));
        colors.add(getResources().getColor(R.color.random_color10));
        colors.add(getResources().getColor(R.color.random_color11));
        colors.add(getResources().getColor(R.color.random_color12));
        colors.add(getResources().getColor(R.color.random_color13));
        colors.add(getResources().getColor(R.color.random_color14));
        colors.add(getResources().getColor(R.color.random_color15));
        colors.add(getResources().getColor(R.color.random_color16));
        colors.add(getResources().getColor(R.color.random_color17));
        colors.add(getResources().getColor(R.color.random_color18));
        colors.add(getResources().getColor(R.color.random_color19));
        colors.add(getResources().getColor(R.color.random_color20));
        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        mPieChart.setData(data);

        // undo all highlights
        mPieChart.highlightValues(null);
        mPieChart.invalidate();
        resetCheckTextState();          // 同步图例的显示与否
    }

    private ILineDataSet baseDataSet, challengeDataSet;

    void resetCheckTextState() {
        if (baseCtv.isChecked()) {
            baseDataSet = mCombinedChart.getLineData().getDataSetByLabel(getString(R.string.base_performance), true);
            lineData.removeDataSet(baseDataSet);
        }
        if (challengeCtv.isChecked()) {
            challengeDataSet = mCombinedChart.getLineData().getDataSetByLabel(getString(R.string.challenge_performance), true);
            lineData.removeDataSet(challengeDataSet);
        }

        mCombinedChart.invalidate();
    }

    @OnClick({R.id.filter_campus, R.id.filter_date, R.id.chart_switch, R.id.legend_base_performance, R.id.legend_challenge_performance})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter_campus:
                if (mCampusPopup != null && mCampusPopup.isShowing()) {
                    mCampusPopup.dismiss();
                } else {
                    closePop();
                    showCampusPopup();
                }
                break;
            case R.id.filter_date:
                if (mDatePopup != null && mDatePopup.isShowing()) {
                    mDatePopup.dismiss();
                } else {
                    closePop();
                    showDatePopup();
                }
                break;
            case R.id.chart_switch:
                if (curPieMode) {
                    curPieMode = false;
                    combinedChartLayout.setVisibility(View.VISIBLE);
                    mPieChart.setVisibility(View.GONE);
                    switcTv.setText("饼图");
                } else {
                    curPieMode = true;
                    mPieChart.setVisibility(View.VISIBLE);
                    combinedChartLayout.setVisibility(View.GONE);
                    switcTv.setText("折线图");
                }
                break;
            case R.id.legend_base_performance:
                invalidateBasePerformance();
//                setChartMarkView(mLastYdatas);
                break;
            case R.id.legend_challenge_performance:
                invalidateChallengePerformance();
//                setChartMarkView(mLastYdatas);
                break;
        }
    }

    private void invalidateBasePerformance() {
//        if (tempLineData == null) {
//            tempLineData = mCombinedChart.getLineData();
//        }
        if (!baseCtv.isChecked()) {
            baseDataSet = mCombinedChart.getLineData().getDataSetByLabel(getString(R.string.base_performance), true);
            lineData.removeDataSet(baseDataSet);
            baseCtv.setChecked(true);
        } else {
            lineData.addDataSet(baseDataSet);
            baseCtv.setChecked(false);
        }

        mCombinedChart.invalidate();
    }

    private void invalidateChallengePerformance() {
//        if (lineData == null) lineData = mCombinedChart.getLineData();
        if (!challengeCtv.isChecked()) {
            challengeDataSet = mCombinedChart.getLineData().getDataSetByLabel(getString(R.string.challenge_performance), true);
            lineData.removeDataSet(challengeDataSet);
            challengeCtv.setChecked(true);
        } else {
            lineData.addDataSet(challengeDataSet);
            challengeCtv.setChecked(false);
        }

        mCombinedChart.invalidate();
    }


    private PopupWindow mCampusPopup, mDatePopup;
    LinearLayoutManager layoutManager;
    CampusSelectAdapter mAdapter;

    private boolean startDateFinished = false;
    private boolean endDateFinished = false;
    DatePicker startDatePicker, endDatePicker;
    private RelativeLayout inverseLayout;
    private TextView inverseTv;
    private CheckBox inverseCb;
    private boolean isAllSelected = true;
    private TextView startDateTv, endDateTv;
    private String mFilterStartTime, mFilterEndTime;
    private Calendar calendar;

    void showCampusPopup() {
        if (mCampusPopup == null) {
            View contentView = LayoutInflater.from(Performance4EduActivity.this).inflate(R.layout.filter_campus_layout, null);
            mCampusPopup = new PopupWindow(contentView);
            mCampusPopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mCampusPopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            //外部是否可以点击
//            mCampusPopup.setBackgroundDrawable(new BitmapDrawable());
//            mCampusPopup.setOutsideTouchable(true);

            inverseLayout = (RelativeLayout) contentView.findViewById(R.id.inverse_layout);
            inverseTv = (TextView) contentView.findViewById(R.id.inverse_tv);
            inverseCb = (CheckBox) contentView.findViewById(R.id.inverse_checkbox);
            RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerview);
            TextView resetTv = (TextView) contentView.findViewById(R.id.campus_reset);
            TextView okTv = (TextView) contentView.findViewById(R.id.campus_ok);
            inverseLayout.setOnClickListener(onClickListener);
            inverseCb.setOnClickListener(onClickListener);
            resetTv.setOnClickListener(onClickListener);
            okTv.setOnClickListener(onClickListener);

            layoutManager = new LinearLayoutManager(Performance4EduActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new CampusSelectAdapter(Performance4EduActivity.this, mAllCampus);
            recyclerView.addItemDecoration(new BaseItemDecor(this, 10));
            recyclerView.setAdapter(mAdapter);
            mAdapter.selectAllItems();
        }
        mCampusPopup.showAsDropDown(filertLayout);
    }

    void showDatePopup() {
        if (mDatePopup == null) {
            View contentView = LayoutInflater.from(Performance4EduActivity.this).inflate(R.layout.filter_date_layout, null);
            mDatePopup = new PopupWindow(contentView);
            mDatePopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mDatePopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

            startDateTv = (TextView) contentView.findViewById(R.id.start_date);
            endDateTv = (TextView) contentView.findViewById(R.id.end_date);
            TextView resetTv = (TextView) contentView.findViewById(R.id.date_reset);
            TextView okTv = (TextView) contentView.findViewById(R.id.date_ok);
            View blankView = contentView.findViewById(R.id.blank_view_date);
            startDateTv.setText(getString(R.string.item_start_time) + " :   " + mFilterStartTime);
            endDateTv.setText(getString(R.string.item_end_time) + " :   " + mFilterEndTime);
            startDateTv.setOnClickListener(onClickListener);
            endDateTv.setOnClickListener(onClickListener);
            resetTv.setOnClickListener(onClickListener);
            okTv.setOnClickListener(onClickListener);
            blankView.setOnClickListener(onClickListener);
        }
        mDatePopup.showAsDropDown(filertLayout);
    }

    void closePop() {
        if (mCampusPopup != null && mCampusPopup.isShowing()) {
            mCampusPopup.dismiss();
        } else if (mDatePopup != null && mDatePopup.isShowing()) {
            mDatePopup.dismiss();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            switch (view.getId()) {
                case R.id.inverse_layout:
                    if (isAllSelected) {
                        inverseCb.setChecked(false);
                        mAdapter.clearAllItems();
                    } else {
                        inverseCb.setChecked(true);
                        mAdapter.selectAllItems();
                    }
                    isAllSelected = !isAllSelected;
                    break;
                case R.id.inverse_checkbox:
                    if (isAllSelected) {
                        mAdapter.clearAllItems();
                    } else {
                        mAdapter.selectAllItems();
                    }
                    isAllSelected = !isAllSelected;
                    break;
                case R.id.campus_reset:
                    mCampusPopup.dismiss();
                    break;
                case R.id.campus_ok:
                    int j = 0;
                    if (mLastCampus.size() == mAdapter.getSelectedItem().size()) {        // 判断数据是否变化,否则就不用刷新
                        for (int i = 0; i < mLastCampus.size(); i++) {
                            if (mLastCampus.get(i).id == mAdapter.getSelectedItem().get(i).id) {
                                j = i;
                            } else {
                                break;
                            }
                        }
                        if (j == mLastCampus.size()) {
                            mCampusPopup.dismiss();
                            break;
                        }
                    }


                    ArrayList<CampusInfo> selectedItem = mAdapter.getSelectedItem();
                    if (selectedItem.size() == 0) {
                        showToast("校区选项必须选择一个");
                    } else {
                        mLastCampus.clear();
                        mLastCampus.addAll(mAdapter.getSelectedItem());
                        if (mLastYdatas != null && mLastYdatas.size() > 0) {
                            setLineChartData(mLastCampus);
                            setPieChartData(mLastCampus);
                        }
                    }
                    mCampusPopup.dismiss();
                    break;
                case R.id.start_date:
                    startDatePicker = new DatePicker(Performance4EduActivity.this, DatePicker.YEAR_MONTH);
                    startDatePicker.setRangeStart(1970, 1);         //开始范围
                    startDatePicker.setRangeEnd(2099, 12);          //结束范围
                    startDatePicker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);  //得到月，因为从0开始的，所以要加1
                    startDatePicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                        @Override
                        public void onDatePicked(String year, String month) {
                            if (endDateFinished) {
                                String endDateYear = endDatePicker.getSelectedYear();
                                String endDateMonth = endDatePicker.getSelectedMonth();
                                if (!year.equals(endDateYear)) {
                                    year = endDateYear;
                                }
                                if (Integer.parseInt(endDateMonth) <= 6) {      //当end<=6(上半年)
                                    if (Integer.parseInt(month) > Integer.parseInt(endDateMonth)) { //若start>end,则start=end(不合理情况)
                                        month = endDateMonth;
                                    }
                                } else {                                        //当end>6(下半年)
                                    if (Integer.parseInt(month) > Integer.parseInt(endDateMonth)) { //若start>end,则start=end(不合理情况)
                                        month = endDateMonth;
                                    } else if (Integer.parseInt(month) <= 6) {                      //若start<=6,则start=7
                                        month = "07";
                                    }
                                }
                                startDatePicker.setSelectedItem(Integer.parseInt(year), Integer.parseInt(month));
                            }
                            startDateTv.setText(getString(R.string.item_start_time) + " :   " + year + month);
                            mFilterStartTime = year + month;
                            startDateFinished = true;
                        }
                    });
                    startDatePicker.show();
                    break;
                case R.id.end_date:
                    endDatePicker = new DatePicker(Performance4EduActivity.this, DatePicker.YEAR_MONTH);
                    endDatePicker.setRangeStart(1970, 1);           //开始范围
                    endDatePicker.setRangeEnd(2099, 12);            //结束范围
                    endDatePicker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);  //得到月，因为从0开始的，所以要加1
                    endDatePicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                        @Override
                        public void onDatePicked(String year, String month) {
                            if (startDateFinished) {
                                String startDateYear = startDatePicker.getSelectedYear();
                                String startDateMonth = startDatePicker.getSelectedMonth();
                                if (!year.equals(startDateYear)) {
                                    year = startDateYear;
                                }
                                if (Integer.parseInt(startDateMonth) <= 6) {    //当start<=6(上半年)
                                    if (Integer.parseInt(month) > Integer.parseInt(startDateMonth)) {   //若end>start
                                        if (Integer.parseInt(month) > 6) {
                                            month = "06";                              //若end>6,则end=6
                                        }
                                    } else {                                                            //若end<start(不合理情况)
                                        month = startDateMonth;
                                    }
                                } else {                                        //当start>6(下半年)
                                    if (Integer.parseInt(month) < Integer.parseInt(startDateMonth)) {   //若end<start
                                        month = startDateMonth;
                                    }
                                }
                                endDatePicker.setSelectedItem(Integer.parseInt(year), Integer.parseInt(month));
                            }
                            endDateTv.setText(getString(R.string.item_end_time) + " :   " + year + month);
                            mFilterEndTime = year + month;
                            endDateFinished = true;
                        }
                    });
                    endDatePicker.show();
                    break;
                case R.id.date_reset:
                    mDatePopup.dismiss();
                    break;
                case R.id.date_ok:
                    if (mParamBeginDate == Integer.parseInt(mFilterStartTime) && mParamEndDate == Integer.parseInt(mFilterEndTime)) {
                        mDatePopup.dismiss();
                        break;
                    }

                    mPresenter.getEduMonthPerformance(mCampusParam, mFilterStartTime != null ? Integer.parseInt(mFilterStartTime) : null,
                            mFilterEndTime != null ? Integer.parseInt(mFilterEndTime) : null);
                    mParamBeginDate = Integer.parseInt(mFilterStartTime);
                    mParamEndDate = Integer.parseInt(mFilterEndTime);
                    mDatePopup.dismiss();
                    break;
                case R.id.blank_view_date:
                    mDatePopup.dismiss();
                    break;
            }
        }
    };
}
