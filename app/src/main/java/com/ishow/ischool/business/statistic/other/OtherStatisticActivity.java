package com.ishow.ischool.business.statistic.other;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commonlib.core.BaseView;
import com.commonlib.util.DateUtil;
import com.commonlib.util.StringUtils;
import com.commonlib.widget.pull.BaseItemDecor;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Constants;
import com.ishow.ischool.bean.statistics.Alias;
import com.ishow.ischool.bean.statistics.OtherStatistics;
import com.ishow.ischool.bean.statistics.OtherStatisticsTable;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.CampusManager;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.ListViewForScrollView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

import static com.ishow.ischool.R.id.chart_date;

public class OtherStatisticActivity extends BaseActivity4Crm<OtherPresenter, OtherModel> implements BaseView {

    @BindView(R.id.filter_type)
    TextView filertType;
    @BindView(R.id.filter_campus)
    TextView filertCampus;
    @BindView(R.id.filter_date)
    TextView filertDate;
    @BindView(R.id.filter_layout)
    LinearLayout filertLayout;

    @BindView(R.id.line_chart)
    BarChart mBarChart;
    @BindView(R.id.pie_chart)
    PieChart mPieChart;

    @BindView(R.id.chart_change)
    TextView chartChangeTv;
    @BindView(R.id.chart_table)
    ListViewForScrollView mTableListView;

    @BindView(R.id.chart_name)
    TextView chartNameTv;

    @BindView(chart_date)
    TextView chartDateTv;

    private TableAdaper mTableAdaper;
    private OtherStatisticsTable mTableData;

    private HashMap<String, String> params;
    private Calendar calendar;
    private View headerView;


    @Override
    protected void initEnv() {
        super.initEnv();
        CampusManager.getInstance().init(this);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_other_statistic, R.string.other_statistic, -1, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        calendar = Calendar.getInstance();
    }

    @Override
    protected void setUpData() {
        params = new HashMap<>();
        params.put("campus", mUser.positionInfo.campusId + "");
        params.put("type", "1");
        params.put("type_name", getString(R.string.type_apply));
        params.put("start_time", AppUtil.getLastMonthStart() + "");
        params.put("end_time", AppUtil.getLastMonthEnd() + "");
        mPresenter.getOtherStatistics(params);

        //默认值
        filertCampus.setText(mUser.positionInfo.campus);
        if (mUser.positionInfo.campusId != Constants.CAMPUS_HEADQUARTERS) {
            filertCampus.setCompoundDrawables(null, null, null, null);
        }
        updateChartTitle();
    }

    public void showChar() {
        if (mBarChart.getVisibility() == View.VISIBLE) {
            showBarChar();
        } else {
            showPieChar();
        }
    }

    public void showBarChar() {
        mPieChart.setVisibility(View.GONE);
        mBarChart.setVisibility(View.VISIBLE);
        mBarChart.setFocusable(true);
        mBarChart.setFocusableInTouchMode(true);
        mBarChart.requestFocus();
        chartChangeTv.setText(R.string.cake_chart);
        initBarChart();
    }

    public void showPieChar() {
        mPieChart.setVisibility(View.VISIBLE);
        mBarChart.setVisibility(View.GONE);
        chartChangeTv.setText(R.string.bar_chart);
        mPieChart.setFocusable(true);
        mPieChart.setFocusableInTouchMode(true);
        mPieChart.requestFocus();
        initPieChar();

    }

    public void updateChartTitle() {
        if ("3".equals(params.get("type"))) {
            chartNameTv.setText(getString(R.string.chart_name_1, params.get("type_name")));
        } else {
            chartNameTv.setText(getString(R.string.chart_name, params.get("type_name")));
        }
        if (!StringUtils.isEmpty(params.get("start_time")) && !StringUtils.isEmpty(params.get("end_time")))
            chartDateTv.setText(DateUtil.parseSecond2Str(Long.parseLong(params.get("start_time"))) + " - "
                    + DateUtil.parseSecond2Str(Long.parseLong(params.get("end_time"))));
    }


    private void showTable(OtherStatisticsTable other) {
        mTableListView.setVisibility(View.VISIBLE);
        mTableAdaper = new TableAdaper(this);
        if (headerView == null) {
            headerView = getLayoutInflater().inflate(R.layout.table_item_head, null, false);
            mTableListView.addHeaderView(headerView);
        }
        TextView noTv = (TextView) headerView.findViewById(R.id.item_table_head_1);
        TextView nameTv = (TextView) headerView.findViewById(R.id.item_table_head_2);
        TextView numTv = (TextView) headerView.findViewById(R.id.item_table_head_3);
        String[] headers = other.header;

        if (headers != null) {
            noTv.setText(headers[0]);
            nameTv.setText(headers[1]);
            numTv.setText(headers[2]);
        }

        mTableListView.setAdapter(mTableAdaper);
        mTableAdaper.setDatas(other.data);
    }

    private void dismissTable() {
        mTableListView.setVisibility(View.GONE);
    }

    @OnClick({R.id.filter_type, R.id.filter_campus, R.id.filter_date, R.id.chart_change})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter_type:
                if (mTypePopup != null && mTypePopup.isShowing()) {
                    mTypePopup.dismiss();
                } else {
                    closePop();
                    showTypePopup();
                }
                break;
            case R.id.filter_campus:
                if (mUser.positionInfo.campusId == Constants.CAMPUS_HEADQUARTERS) {
                    if (mCampusPopup != null && mCampusPopup.isShowing()) {
                        mCampusPopup.dismiss();
                    } else {
                        closePop();
                        showCampusPopup();
                    }
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
            case R.id.chart_change:
                if (mPieChart.getVisibility() == View.VISIBLE) {
                    showBarChar();
                } else {
                    showPieChar();
                }
                break;
        }
    }

    void closePop() {
        if (mTypePopup != null && mTypePopup.isShowing()) {
            mTypePopup.dismiss();
        } else if (mCampusPopup != null && mCampusPopup.isShowing()) {
            mCampusPopup.dismiss();
        } else if (mDatePopup != null && mDatePopup.isShowing()) {
            mDatePopup.dismiss();
        }
    }


    private PopupWindow mTypePopup, mCampusPopup, mDatePopup;
    private ArrayList<CampusInfo> mList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private CampusSelectAdapter mAdapter;
    private RelativeLayout inverseLayout;
    private TextView inverseTv;
    private CheckBox inverseCb;
    private boolean isAllSelected = true;
    private TextView startDateTv, endDateTv;
    private String mFilterStartTime;
    private String mFilterEndTime;

    void showCampusPopup() {
        if (mCampusPopup == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.filter_campus_layout_other, null);
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
            resetTv.setText(R.string.cancel);

            mList = CampusManager.getInstance().getAll();
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new CampusSelectAdapter(this, mList);
            recyclerView.addItemDecoration(new BaseItemDecor(this, 10));
            recyclerView.setAdapter(mAdapter);
//            mAdapter.selectAllItems();
            mAdapter.setCurCampusInfo(mUser.positionInfo.campusId);
        }
        mCampusPopup.showAsDropDown(filertLayout);
    }

    void showTypePopup() {
        if (mTypePopup == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.filter_type_other_layout, null);
            mTypePopup = new PopupWindow(contentView);
            mTypePopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mTypePopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

            TextView applyTv = (TextView) contentView.findViewById(R.id.apply_tv);
            TextView refusePointTv = (TextView) contentView.findViewById(R.id.refuse_point_tv);
            TextView sourceTv = (TextView) contentView.findViewById(R.id.source_tv);
            View blankView = contentView.findViewById(R.id.blank_view_type);
            applyTv.setOnClickListener(onClickListener);
            refusePointTv.setOnClickListener(onClickListener);
            sourceTv.setOnClickListener(onClickListener);
            blankView.setOnClickListener(onClickListener);
        }
        mTypePopup.showAsDropDown(filertLayout);
    }

    void showDatePopup() {
        if (mDatePopup == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.filter_date_layout, null);
            mDatePopup = new PopupWindow(contentView);
            mDatePopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mDatePopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

            startDateTv = (TextView) contentView.findViewById(R.id.start_date);
            endDateTv = (TextView) contentView.findViewById(R.id.end_date);
            TextView resetTv = (TextView) contentView.findViewById(R.id.date_reset);
            TextView okTv = (TextView) contentView.findViewById(R.id.date_ok);
            View blankView = contentView.findViewById(R.id.blank_view_date);
            startDateTv.setOnClickListener(onClickListener);
            endDateTv.setOnClickListener(onClickListener);
            resetTv.setOnClickListener(onClickListener);
            okTv.setOnClickListener(onClickListener);
            blankView.setOnClickListener(onClickListener);
            resetTv.setText(R.string.cancel);
            startDateTv.setText(getString(R.string.filter_start_date, DateUtil.parseSecond2Str(Long.parseLong(params.get("start_time")))));
            endDateTv.setText(getString(R.string.filter_end_date, DateUtil.parseSecond2Str(Long.parseLong(params.get("end_time")))));
            //默认时间
            startTime = Long.parseLong(params.get("start_time"));
            endTime = Long.parseLong(params.get("end_time"));
        }
        mDatePopup.showAsDropDown(filertLayout);
    }

    private long startTime;
    private long endTime;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.campus_reset:
                    mCampusPopup.dismiss();
                    break;
                case R.id.campus_ok:
                    CampusInfo campusInfo = mAdapter.getCurCampusInfo();
                    params.put("campus", campusInfo.id + "");
                    mPresenter.getOtherStatistics(params);
//                    updateChartTitle();
                    filertCampus.setText(campusInfo.name);
                    mCampusPopup.dismiss();
                    break;
                case R.id.apply_tv:
                    filertType.setText(R.string.type_apply);
                    mTypePopup.dismiss();
                    params.put("type", "1");
                    params.put("type_name", getString(R.string.type_apply));
                    mPresenter.getOtherStatistics(params);
                    updateChartTitle();
                    break;
                case R.id.refuse_point_tv:
                    filertType.setText(R.string.type_refuse_point);
                    updateChartTitle();
                    mTypePopup.dismiss();
                    params.put("type", "2");
                    params.put("type_name", getString(R.string.type_refuse_point));
                    mPresenter.getOtherStatistics(params);
                    updateChartTitle();
                    break;
                case R.id.source_tv:
                    filertType.setText(R.string.type_source);
                    mTypePopup.dismiss();
                    params.put("type", "3");
                    params.put("type_name", getString(R.string.type_source));
                    mPresenter.getOtherStatistics(params);
                    updateChartTitle();
                    break;
                case R.id.blank_view_type:
                    mTypePopup.dismiss();
                    break;
                case R.id.start_date:
                    DatePicker startPicker = new DatePicker(OtherStatisticActivity.this, DatePicker.YEAR_MONTH);
                    startPicker.setRangeStart(1970, 1, 1);       //开始范围
                    startPicker.setRangeEnd(2099, 12, 31);       //结束范围
                    startPicker.setSelectedItem(AppUtil.getYear(startTime), AppUtil.getMonth(startTime));  //得到月，因为从0开始的，所以要加1
                    startPicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                        @Override
                        public void onDatePicked(String year, String month) {
                            startTime = AppUtil.getMonthStart(year, month);
                            startDateTv.setText(getString(R.string.filter_start_date, DateUtil.parseSecond2Str(startTime)));
                        }
                    });
                    startPicker.show();
                    break;
                case R.id.end_date:
                    DatePicker endPicker = new DatePicker(OtherStatisticActivity.this, DatePicker.YEAR_MONTH);
                    endPicker.setRangeStart(1970, 1, 1);       //开始范围
                    endPicker.setRangeEnd(2099, 12, 31);       //结束范围
                    endPicker.setSelectedItem(AppUtil.getYear(endTime), AppUtil.getMonth(endTime));  //得到月，因为从0开始的，所以要加1
                    endPicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                        @Override
                        public void onDatePicked(String year, String month) {
                            if (AppUtil.getMonthEnd(year, month) < startTime) {
                                showToast(R.string.end_le_start);
                                return;
                            }
                            endTime = AppUtil.getMonthEnd(year, month);
                            endDateTv.setText(getString(R.string.filter_end_date, DateUtil.parseSecond2Str(endTime)));
                        }
                    });
                    endPicker.show();
                    break;
                case R.id.date_reset:
                    mDatePopup.dismiss();
                    break;
                case R.id.date_ok:
                    mDatePopup.dismiss();
                    updateChartTitle();
                    params.put("start_time", startTime + "");
                    params.put("end_time", endTime + "");
                    mPresenter.getOtherStatistics(params);
                    updateChartTitle();
                    break;
                case R.id.blank_view_date:
                    mDatePopup.dismiss();
                    break;
            }
        }
    };


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
        //mBarChart.setPinchZoom(true);

        // set an alternative background color
        mBarChart.setBackgroundColor(Color.WHITE);

        mBarChart.setFitBars(true);
        mBarChart.setDrawValueAboveBar(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(13);
        xAxis.setLabelRotationAngle(-75);
        xAxis.mLabelHeight = 0;
        xAxis.mLabelRotatedHeight = 0;
        xAxis.setAvoidFirstLastClipping(true);

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setGranularity(1);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mBarChart.getAxisRight().setEnabled(false);


        // add data
        setBarData(mTableData);

        mBarChart.animateX(2500);

        // get the legend (only possible after setting data)
        Legend l = mBarChart.getLegend();

        // modify the legend ...
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setForm(Legend.LegendForm.LINE);

        // // dont forget to refresh the drawing
        // mBarChart.invalidate();
    }

    private void setBarData(OtherStatisticsTable table) {
        if (table == null || table.data == null || table.data.isEmpty()) {
            mBarChart.clear();
            return;
        }

        ArrayList<OtherStatistics> others = table.data;

        float start = 0f;
        int count = others.size() > 8 && "1".equals(params.get("type")) ? 8 : others.size();

        mBarChart.getXAxis().setAxisMinimum(start);
        mBarChart.getXAxis().setAxisMaximum(start + count + 1);

        CampusIAxisValueFormatter formatter = new CampusIAxisValueFormatter(others, count);
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
            ArrayList<Integer> colors = new ArrayList<>();
            for (Alias alias : table.alias) {
                colors.add(ColorTemplate.rgb(alias.color));
            }
            set1.setColors(colors);

            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");

            ArrayList<Integer> colors = new ArrayList<>();
            for (Alias alias : table.alias) {
                colors.add(ColorTemplate.rgb(alias.color));
            }
            set1.setColors(colors);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            set1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    if (value == 0) {
                        return "";
                    } else {
                        return String.valueOf((int) value);
                    }
                }
            });

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            mBarChart.setData(data);
        }
    }


    public void initPieChar() {
        mPieChart = (PieChart) findViewById(R.id.pie_chart);

        mPieChart = (PieChart) findViewById(R.id.pie_chart);
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


        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//        mPieChart.setOnChartValueSelectedListener(this);

//        setData(4, 100);
        setPieData(mTableData);

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);


        Legend l = mPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(5f);
        l.setWordWrapEnabled(true);

        // entry label styling
        mPieChart.setEntryLabelColor(Color.BLACK);
        mPieChart.setEntryLabelTextSize(12f);


    }

    private void setPieData(OtherStatisticsTable table) {
        if (table == null || table.data == null || table.data.isEmpty()) {
            mPieChart.clear();
            return;
        }

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        ArrayList<OtherStatistics> others = table.data;

        int count = others.size() > 8 && "1".equals(params.get("type")) ? 8 : others.size();
        int value = 0;
        for (int i = 0; i < count; i++) {
            value += others.get(i).value;
        }

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count; i++) {
            if (others.get(i).value > 0) {
                entries.add(new PieEntry(((float) others.get(i).value) / value * 100, others.get(i).name));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();
        for (Alias alias : table.alias) {
            colors.add(ColorTemplate.rgb(alias.color));
        }
        dataSet.setColors(colors);

        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);

//        data.setValueTypeface(mTfLight);
        mPieChart.setData(data);

        // undo all highlights
        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }


    public void onGetSuccess(OtherStatisticsTable table) {
        mTableData = table;
        if (!params.get("type").equals("3")) {
            showTable(table);
        } else {
            dismissTable();
        }

        showChar();
    }

    public void onGetFailed(String msg) {
        showToast(msg);
//        testOtherData();
    }

    public void testOtherData() {
        Gson gson = new Gson();
        String m = "{ \"title\": \"各学校报名量统计\", \"alias\": [ \"序号\", \"学校\", \"报名量 \" ], \"data\": [ { \"value\": 30, \"name\": \"北京大学\", \"color\": \"#a3bc56\" }, { \"value\": 12, \"name\": \"南开大学\", \"color\": \"#fd9d9e\" }, { \"value\": 23, \"name\": \"厦门大学\", \"color\": \"#60c0dd\" } ] }";
        OtherStatisticsTable table = gson.fromJson(m, OtherStatisticsTable.class);
        mTableData = table;
        showBarChar();
        showTable(table);
    }

}
