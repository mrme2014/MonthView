package com.ishow.ischool.business.campusperformance.market;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
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
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.campusperformance.SignPerformance;
import com.ishow.ischool.bean.campusperformance.SignPerformanceResult;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.DataApi;
import com.ishow.ischool.common.manager.CampusManager;
import com.ishow.ischool.widget.table.MyMarkerView1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ishow.ischool.R.string.performance;

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
    @BindView(R.id.from_switch)
    TextView fromTv;
    @BindView(R.id.chart_switch)
    TextView switcTv;
    @BindView(R.id.legend_layout)
    LinearLayout legendLayout;
    @BindView(R.id.legend_base_performance)
    CheckedTextView baseCtv;
    @BindView(R.id.legend_challenge_performance)
    CheckedTextView challengeCtv;

    public boolean curPieMode = false;
    private ArrayList<String> mXDatas;      // 横坐标数据,需要显示的校区name
    private int mCount = 0;                 // 横坐标个数,即数据个数
    private ArrayList<CampusInfo> mCampusInfos;
    public ArrayList<CampusInfo> mParamCampus;      // 上次显示的校区
    public ArrayList<SignPerformance> mLastYdatas;      // 上次显示的数据(纵坐标)
    public String campusParamAll = "";                     // 所有校区id,用于每次请求所有校区的数据
    public String campusParam = "";                 // 默认所有
    public ArrayList<SignPerformance> mYDatas;      // 纵坐标数据,即每个校区的数据
    public int mParamBeginDate = 201607, mParamEndDate = 201609;
    public Integer mParamDataType = -1;       //数据类型 1:晨读 3:校聊

    private LineData lineData = new LineData();
    private LineDataSet baseLineDataSet, challengeLineDataSet;
    private BarData barData = new BarData();
    private BarDataSet barDataSet;
    private CombinedData combinedData = new CombinedData();

    public void pullData(final ArrayList<CampusInfo> showCampus, int beginMonth, int endMonth, int dataType) {
        mParamBeginDate = beginMonth;
        mParamEndDate = endMonth;
        subtitleTv.setText(mParamBeginDate + "-" + mParamEndDate);
        ApiFactory.getInstance().getApi(DataApi.class).getSignPerformance(campusParamAll,
                beginMonth, endMonth, dataType == -1 ? null : dataType, "campusTotal")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<SignPerformanceResult>() {
                    @Override
                    public void onSuccess(SignPerformanceResult result) {
                        mYDatas = result.campusTotal;
                        setLineChartData(showCampus);
                        setPieChartData(mParamCampus);
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
        setData();
        initPieChart();
        initCombinedChart();
        pullData(mCampusInfos, mParamBeginDate, mParamEndDate, mParamDataType);
    }

    public void setData() {
        mParamCampus = new ArrayList<>();
        mLastYdatas = new ArrayList<>();
        mCampusInfos = CampusManager.getInstance().get();
        mXDatas = new ArrayList<>();
        mXDatas.addAll(CampusManager.getInstance().getCampusNames());
        mCount = mXDatas.size();

        // 初始获取所有校区id
        ArrayList<CampusInfo> allCampus = new ArrayList<>();
        allCampus.addAll(CampusManager.getInstance().get());
        for (CampusInfo info : allCampus) {
            campusParamAll = campusParamAll + info.id + ",";
        }
        campusParamAll = campusParamAll.substring(0, campusParamAll.length() - 1);
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
        xAxis.setAxisMinValue(0f);
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

    /**
     * @param campusInfos // 本次需要显示的校区
     */
    public void setLineChartData(ArrayList<CampusInfo> campusInfos) {
        ArrayList<CampusInfo> tempCampusInfos = new ArrayList<>();
        tempCampusInfos.addAll(campusInfos);
        mLastYdatas.clear();
        if (mParamCampus != null && mParamCampus.size() > 0) {      // 即不是第一次
            mXDatas.clear();
            ArrayList<CampusInfo> allCampusInfos = CampusManager.getInstance().get();
            for (CampusInfo campusInfo : tempCampusInfos) {
                mXDatas.add(campusInfo.name);
                mLastYdatas.add(mYDatas.get(allCampusInfos.indexOf(campusInfo)));
            }
            mCount = mXDatas.size();
            lineData.removeDataSet(mCombinedChart.getLineData().getDataSetByLabel(getString(R.string.base_performance), true));
            lineData.removeDataSet(mCombinedChart.getLineData().getDataSetByLabel(getString(R.string.challenge_performance), true));
            barData.removeDataSet(mCombinedChart.getBarData().getDataSetByLabel(getString(performance), true));
            mCombinedChart.clearValues();
        } else {
            mLastYdatas.addAll(mYDatas);
        }
        mParamCampus.clear();         // 更新上一次显示的校区
        mParamCampus.addAll(tempCampusInfos);

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

    void setChartMarkView(ArrayList<SignPerformance> datas) {
        MyMarkerView1 mv = new MyMarkerView1(getContext(), !baseCtv.isChecked(), !challengeCtv.isChecked(), mXDatas, datas);
        mv.setChartView(mCombinedChart); // For bounds control
        mCombinedChart.setMarker(mv);
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
        mPieChart.setEntryLabelColor(getContext().getResources().getColor(R.color.txt_6));
        mPieChart.setEntryLabelTextSize(10f);
    }

    private LineDataSet generateBaseLineData(ArrayList<SignPerformance> campusPerformances) {
        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < mCount; index++) {
            entries.add(new Entry(index, Float.parseFloat(campusPerformances.get(index).perweek_full_base)));
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

        for (int index = 0; index < mCount; index++) {
            entries.add(new Entry(index, Float.parseFloat(campusPerformances.get(index).perweek_full_challenge)));
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
//            if (index == mCount - 1) {
//                entries.add(new BarEntry(index, 0));
//                continue;
//            }
            entries.add(new BarEntry(index, Float.parseFloat(campusPerformances.get(index).perweek_real)));
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
        campusParam = "";
        for (CampusInfo info : campusInfos) {
            campusParam = campusParam + info.id + ",";
        }
        campusParam = campusParam.substring(0, campusParam.length() - 1);


        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        float total = 0;
        for (int i = 0; i < campusInfos.size(); i++) {
            int position = mCampusInfos.indexOf(campusInfos.get(i));
            total = total + Integer.parseInt(mYDatas.get(position).perweek_real);
        }
        for (int j = 0; j < campusInfos.size(); j++) {
            int position = mCampusInfos.indexOf(campusInfos.get(j));
            double result = Math.round((Integer.parseInt(mYDatas.get(position).perweek_real) / total) * 10000) / 10000.0;     // 保留4位小数(*100后保留2位小数)
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


    @OnClick({R.id.from_switch, R.id.chart_switch, R.id.legend_base_performance, R.id.legend_challenge_performance, R.id.table_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.from_switch:
                if (mFromPopup != null && mFromPopup.isShowing()) {
                    mFromPopup.dismiss();
                } else {
                    showFromPopup();
                }
                break;
            case R.id.chart_switch:
                if (curPieMode) {
                    curPieMode = false;
                    mCombinedChart.setVisibility(View.VISIBLE);
                    legendLayout.setVisibility(View.VISIBLE);
                    mPieChart.setVisibility(View.GONE);
                    titleTv.setText("业绩趋势(个)");
                    switcTv.setText("饼图");
                } else {
                    curPieMode = true;
                    mPieChart.setVisibility(View.VISIBLE);
                    mCombinedChart.setVisibility(View.GONE);
                    legendLayout.setVisibility(View.GONE);
                    titleTv.setText("业绩对比(%)");
                    switcTv.setText("折线图");
                }
                break;
            case R.id.legend_base_performance:
                invalidateBasePerformance();
                setChartMarkView(mLastYdatas);
                break;
            case R.id.legend_challenge_performance:
                invalidateChallengePerformance();
                setChartMarkView(mLastYdatas);
                break;
            case R.id.table_layout:
                Intent intent = new Intent(getActivity(), MonthPerformance4MarketTableActivity.class);
                intent.putExtra("campusParam", campusParam);
                startActivity(intent);
                break;
        }
    }

    private PopupWindow mFromPopup;
    private ListView from_lv;
    private ArrayList<String> fromList;

    void showFromPopup() {
        if (mFromPopup == null) {
            View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.filter_chart_from_popup, null);
            mFromPopup = new PopupWindow(contentView);
            mFromPopup.setWidth(fromTv.getWidth());
            mFromPopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            //外部是否可以点击
            mFromPopup.setBackgroundDrawable(new BitmapDrawable());
            mFromPopup.setOutsideTouchable(true);

            from_lv = (ListView) contentView.findViewById(R.id.from_lv);
            final ViewGroup.LayoutParams params = from_lv.getLayoutParams();
            params.width = fromTv.getWidth();
            from_lv.setLayoutParams(params);
            fromList = new ArrayList<>();
            fromList.add("全部");
            fromList.add("晨读");
            fromList.add("校聊");
            List<Map<String, Object>> listitems = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < fromList.size(); i++) {
                Map<String, Object> listitem = new HashMap<String, Object>();
                listitem.put("from", fromList.get(i));
                listitems.add(listitem);
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), listitems, R.layout.item_performance_simple_list,
                    new String[]{"from"}, new int[]{R.id.from_tv});
            from_lv.setAdapter(simpleAdapter);
            from_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Integer type = null;
                    if (i == 0) {
                        type = -1;
                    } else if (i == 1) {
                        type = 1;
                    } else if (i == 2) {
                        type = 3;
                    }
                    fromTv.setText(fromList.get(i));
                    if (type != mParamDataType) {
                        mParamDataType = type;
                        pullData(mParamCampus, mParamBeginDate, mParamEndDate, mParamDataType);
                    }
                    mFromPopup.dismiss();
                }
            });
        }
        mFromPopup.showAsDropDown(fromTv);
    }

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

    private ILineDataSet baseDataSet, challengeDataSet;
    private LineData tempLineData;

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

}
