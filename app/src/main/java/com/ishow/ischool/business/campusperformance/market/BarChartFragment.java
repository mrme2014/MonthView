package com.ishow.ischool.business.campusperformance.market;

import android.content.Intent;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.campusperformance.SignAmount;
import com.ishow.ischool.bean.campusperformance.SignAmountResult;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.DataApi;
import com.ishow.ischool.common.manager.CampusManager;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.widget.table.MyMarkerView2;
import com.ishow.ischool.widget.table.MyMarkerView3;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/9/13.
 */
public class BarChartFragment extends BaseFragment {

    @BindView(R.id.chart1)
    public BarChart mChartAmount;
    @BindView(R.id.chart2)
    public BarChart mChartPercentage;
    @BindView(R.id.chart_title)
    TextView titleTv;
    @BindView(R.id.subtitle)
    TextView subtitleTv;
    @BindView(R.id.chart_switch)
    TextView switcTv;
    @BindView(R.id.chart1_layout)
    LinearLayout chart1Layout;
    @BindView(R.id.chart1_legend)
    LinearLayout chart1Legend;
    @BindView(R.id.legend_attend_amount)
    CheckedTextView attendAmountCtv;
    @BindView(R.id.legend_registration_amount)
    CheckedTextView registrationAmountCtv;
    @BindView(R.id.legend_full_payment_amount)
    CheckedTextView fullPaymentAmountCtv;
    @BindView(R.id.chart2_layout)
    LinearLayout chart2Layout;
    @BindView(R.id.chart2_legend)
    LinearLayout chart2Legend;
    @BindView(R.id.legend_registration_rate)
    CheckedTextView registrationRateCtv;
    @BindView(R.id.legend_full_payment_rate)
    CheckedTextView fullPaymentRateCtv;
    @BindView(R.id.legend_full_payment_registration_rate)
    CheckedTextView fullPaymentRegistrationRateCtv;

    public boolean curAmountMode = true;
    public BarData mBarDataAmount, mBarDataPercentage;
    private ArrayList<String> mXDatas;
    private int mCount = 0;
    private ArrayList<CampusInfo> mCampusInfos;
    public ArrayList<CampusInfo> mLastCampus;      // 上次显示的校区
    public ArrayList<SignAmount> mLastYdatas;
    private ArrayList<SignAmount> mYDatas;
    public int mParamBeginDate, mParamEndDate;
    private String label1, label2, label3;
    public boolean isFirst = true;
    public String campusParamAll = "";                     // 每次请求所有校区的
    public String campusParam = "";     // 默认所有

    public void pullData(final ArrayList<CampusInfo> showCampus, int beginMonth, int endMonth) {
        subtitleTv.setText(beginMonth + "-" + endMonth);
        campusParam = "";
        for (CampusInfo info : showCampus) {
            campusParam = campusParam + info.id + ",";
        }
        campusParam = campusParam.substring(0, campusParam.length() - 1);
        ApiFactory.getInstance().getApi(DataApi.class).getSignAmount(1, campusParamAll,
                beginMonth == -1 ? null : beginMonth, endMonth == -1 ? null : endMonth, null, "signTotal")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<SignAmountResult>() {
                    @Override
                    public void onSuccess(SignAmountResult result) {
                        lazyShow();
                        mYDatas = result.signTotal;
                        mYDatas.add(new SignAmount());
                        setBarChartData(showCampus, mChartAmount, mBarDataAmount);
                        setBarChartData(showCampus, mChartPercentage, mBarDataPercentage);
                    }

                    @Override
                    public void onError(String msg) {
                    }
                });
    }

    public static BarChartFragment newInstance(int beginMonth, int endMonth) {
        BarChartFragment fragment = new BarChartFragment();
        Bundle args = new Bundle();
        args.putInt("beginMonth", beginMonth);
        args.putInt("endMonth", endMonth);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mParamBeginDate = bundle.getInt("beginMonth");
            mParamEndDate = bundle.getInt("endMonth");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barchart, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isFirst) {
            mChartAmount.moveViewToX(0);
            mChartPercentage.moveViewToX(0);
            isFirst = false;
        }
    }

    /**
     * 懒加载，避免数据没有加载成功，点击崩溃
     */
    void lazyShow() {
        if (chart1Legend.getVisibility() != View.VISIBLE) {
            chart1Legend.setVisibility(View.VISIBLE);
        }
        if (chart2Legend.getVisibility() != View.VISIBLE) {
            chart2Legend.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void init() {
        mBarDataAmount = new BarData();
        mBarDataPercentage = new BarData();
        mLastCampus = new ArrayList<>();
        mLastYdatas = new ArrayList<>();
        mCampusInfos = CampusManager.getInstance().get();
        mXDatas = new ArrayList<>();
        mXDatas.addAll(CampusManager.getInstance().getCampusNames());
        mCount = mXDatas.size();

        ArrayList<CampusInfo> allCampus = new ArrayList<>();
        allCampus.addAll(CampusManager.getInstance().get());
        for (CampusInfo info : allCampus) {
            campusParamAll = campusParamAll + info.id + ",";
        }
        campusParamAll = campusParamAll.substring(0, campusParamAll.length() - 1);

        initAmountChart();
        initPercentageChart();
        pullData(mCampusInfos, mParamBeginDate, mParamEndDate);
    }

    private void initAmountChart() {
        mChartAmount.setScaleEnabled(false);
        mChartAmount.setDescription("");

        mChartAmount.getLegend().setEnabled(false);

        YAxis leftAxis = mChartAmount.getAxisLeft();
        leftAxis.setDrawAxisLine(false);       //是否绘制坐标轴的线，即含有坐标的那条线，默认是true
        leftAxis.setAxisMinimum(0f);
        leftAxis.setSpaceBottom(20f);
        mChartAmount.getAxisRight().setEnabled(false);    // 隐藏右边的坐标轴

        XAxis xAxis = mChartAmount.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);      //是否显示X坐标轴上的刻度竖线，默认是true
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);    // only intervals of 1 day
        xAxis.setLabelRotationAngle(-60);       //设置x轴字体显示角度
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setCenterAxisLabels(true);
//        xAxis.setAxisMaxValue(mCount + 2);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                LogUtil.d("BarChartFragment value = " + value);
                if (value >= 0 && value < mCount) {
                    return mXDatas.get((int) value);
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


    private void initPercentageChart() {
        mChartPercentage.setScaleEnabled(false);
        mChartPercentage.setDescription("");

        mChartPercentage.getLegend().setEnabled(false);

        //设置y轴的样式
        YAxis leftAxis = mChartPercentage.getAxisLeft();
        leftAxis.setDrawAxisLine(false);       //是否绘制坐标轴的线，即含有坐标的那条线，默认是true
        leftAxis.setAxisMinValue(0f);
        leftAxis.setAxisMaxValue(100f);
        leftAxis.setGranularity(20);
        leftAxis.setSpaceBottom(20f);
        mChartPercentage.getAxisRight().setEnabled(false);    // 隐藏右边的坐标轴
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                LogUtil.d("BarChartFragment leftAxis value = " + value);
                return (int) value + "%";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        //设置x轴的样式
        XAxis xAxis = mChartPercentage.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);      //是否显示X坐标轴上的刻度竖线，默认是true
        xAxis.setAxisMinValue(0f);
        xAxis.setGranularity(1f);    // only intervals of 1 day
//        xAxis.setLabelCount(mCampusDatas.size());
        xAxis.setLabelRotationAngle(-60);       //设置x轴字体显示角度
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setCenterAxisLabels(true);
//        xAxis.setAxisMaxValue(mCount + 2);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                LogUtil.d("BarChartFragment value = " + value);
                if (value >= 0.0) {
                    return mXDatas.get((int) value % mXDatas.size());
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
     * @param campusInfos  // 需要显示的校区
     * @param barChart     // 相应的表
     * @param barData      // 对应表的barData
     */
    public void setBarChartData(ArrayList<CampusInfo> campusInfos, BarChart barChart, BarData barData) {
        // 刷新上次显示(id)
        campusParam = "";
        for (CampusInfo info : campusInfos) {
            campusParam = campusParam + info.id + ",";
        }
        campusParam = campusParam.substring(0, campusParam.length() - 1);

        if (barChart == mChartAmount) {
            label1 = getString(R.string.attend_amount);
            label2 = getString(R.string.registration_amount);
            label3 = getString(R.string.full_payment_amount);
        } else {
            label1 = getString(R.string.registration_rate);
            label2 = getString(R.string.full_payment_rate);
            label3 = getString(R.string.full_payment_registration_rate);
        }

        ArrayList<CampusInfo> tempCampusInfos = new ArrayList<>();
        tempCampusInfos.addAll(campusInfos);
        mLastYdatas.clear();
        if (mLastCampus != null && mLastCampus.size() > 0) {      // 即不是第一次
            mXDatas.clear();
            ArrayList<CampusInfo> allCampusInfos = CampusManager.getInstance().get();
            for (CampusInfo campusInfo : campusInfos) {
                mXDatas.add(campusInfo.name);
                mLastYdatas.add(mYDatas.get(allCampusInfos.indexOf(campusInfo)));
            }
            mCount = mXDatas.size();
            mLastYdatas.add(new SignAmount());  // 填充最后一个
            barData.removeDataSet(barChart.getBarData().getDataSetByLabel(label1, true));
            barData.removeDataSet(barChart.getBarData().getDataSetByLabel(label2, true));
            barData.removeDataSet(barChart.getBarData().getDataSetByLabel(label3, true));
            barChart.clearValues();
        } else {
            mLastYdatas.addAll(mYDatas);
        }

        if (barChart == mChartPercentage) {
            mLastCampus.clear();         // 更新上一次显示的校区
            mLastCampus.addAll(tempCampusInfos);
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();


        for (int i = 0; i < mXDatas.size() + 1; i++) {
            if (barChart == mChartAmount) {
                yVals1.add(new BarEntry(i, mLastYdatas.get(i).scene));
                yVals2.add(new BarEntry(i, mLastYdatas.get(i).sign));
                yVals3.add(new BarEntry(i, mLastYdatas.get(i).fullPay));
            } else {
                yVals1.add(new BarEntry(i, mLastYdatas.get(i).signRate));
                yVals2.add(new BarEntry(i, mLastYdatas.get(i).fullRate));
                yVals3.add(new BarEntry(i, mLastYdatas.get(i).fullSignRate));
            }
        }

        // create 3 datasets with different types
        BarDataSet set1, set2, set3;
        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) barChart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) barChart.getData().getDataSetByIndex(2);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, label1);
            set1.setColor(getResources().getColor(R.color.chart_blue));
            set2 = new BarDataSet(yVals2, label2);
            set2.setColor(getResources().getColor(R.color.chart_red));
            set3 = new BarDataSet(yVals3, label3);
            set3.setColor(getResources().getColor(R.color.chart_green));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);
            dataSets.add(set3);

            barData = new BarData(dataSets);
            barChart.setData(barData);
//            chart.setFitBars(true);
        }

        float groupSpace = 0.31f;
        float barSpace = 0.08f; // x3 dataset
        float barWidth = 0.15f; // x3 dataset
        // (0.15 + 0.08) * 3 + 0.31 = 1.00 -> interval per "group"
        barChart.getBarData().setBarWidth(barWidth);
        barChart.getBarData().setDrawValues(false);
        barChart.groupBars(0, groupSpace, barSpace);
        barChart.animateXY(800, 800);//图表数据显示动画

        barChart.fitScreen();
//        if (mCount < 6) {
//            barChart.getXAxis().setLabelCount(mCount + 1);
//        }
        barChart.setVisibleXRangeMaximum(mCount > 6 ? 6 : mCount + 1);      //设置屏幕显示条数
        barChart.getXAxis().setGranularity(1f);

        if (barChart == mChartPercentage) {
            resetCheckTextState();          // 同步图例的显示与否
        }
        barChart.invalidate();
        setAmountChartMarkView(mLastYdatas);
        setPercentageChartMarkView(mLastYdatas);
    }

    void setAmountChartMarkView(ArrayList<SignAmount> curYdatas) {
        MyMarkerView2 mv = new MyMarkerView2(getContext(), !attendAmountCtv.isChecked(), !registrationAmountCtv.isChecked(),
                !fullPaymentAmountCtv.isChecked(), mXDatas, curYdatas);
        mv.setChartView(mChartAmount); // For bounds control
        mChartAmount.setMarker(mv);
    }

    void setPercentageChartMarkView(ArrayList<SignAmount> curYdatas) {
        MyMarkerView3 mv = new MyMarkerView3(getContext(), !registrationRateCtv.isChecked(), !fullPaymentRateCtv.isChecked(),
                !fullPaymentRegistrationRateCtv.isChecked(), mXDatas, curYdatas);
        mv.setChartView(mChartPercentage); // For bounds control
        mChartPercentage.setMarker(mv);
    }

    @OnClick({R.id.chart_switch, R.id.legend_attend_amount, R.id.legend_registration_amount, R.id.legend_full_payment_amount,
            R.id.legend_registration_rate, R.id.legend_full_payment_rate, R.id.legend_full_payment_registration_rate, R.id.table_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_switch:
                if (curAmountMode) {
                    curAmountMode = false;
                    initPercentageChart();
                    chart2Layout.setVisibility(View.VISIBLE);
                    chart1Layout.setVisibility(View.GONE);
                    titleTv.setText("数据对比(%)");
                    switcTv.setText("按人数");
                } else {
                    curAmountMode = true;
                    chart1Layout.setVisibility(View.VISIBLE);
                    chart2Layout.setVisibility(View.GONE);
                    titleTv.setText("人数对比(个)");
                    switcTv.setText("按比例");
                }
                break;
            case R.id.legend_attend_amount:
                invalidateAttendAmount();
                setAmountChartMarkView(mLastYdatas);
                break;
            case R.id.legend_registration_amount:
                invalidateRegistrationAmount();
                setAmountChartMarkView(mLastYdatas);
                break;
            case R.id.legend_full_payment_amount:
                invalidatefullPaymentAmount();
                setAmountChartMarkView(mLastYdatas);
                break;
            case R.id.legend_registration_rate:
                invalidateRegistrationRate();
                setPercentageChartMarkView(mLastYdatas);
                break;
            case R.id.legend_full_payment_rate:
                invalidateFullPaymentRate();
                setPercentageChartMarkView(mLastYdatas);
                break;
            case R.id.legend_full_payment_registration_rate:
                invalidateFullPaymentRegistrationRate();
                setPercentageChartMarkView(mLastYdatas);
                break;
            case R.id.table_layout:
                Intent intent = new Intent(getActivity(), Amount4MarketTableActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("campus", mXDatas);
                ArrayList<SignAmount> temp = new ArrayList<SignAmount>(mLastYdatas.subList(0, mLastYdatas.size() - 1));
                bundle.putParcelableArrayList("data", temp);
                intent.putExtras(bundle);
                JumpManager.jumpActivity(getActivity(), intent, Resource.NO_NEED_CHECK);
                break;
        }
    }

    void resetCheckTextState() {
        if (attendAmountCtv.isChecked()) {
            attendAmountDataSet = mChartAmount.getBarData().getDataSetByLabel(getString(R.string.attend_amount), true);
            mChartAmount.getBarData().removeDataSet(attendAmountDataSet);
        }
        if (registrationAmountCtv.isChecked()) {
            registrationAmountDataSet = mChartAmount.getBarData().getDataSetByLabel(getString(R.string.registration_amount), true);
            mChartAmount.getBarData().removeDataSet(registrationAmountDataSet);
        }
        if (fullPaymentAmountCtv.isChecked()) {
            fullPaymentAmountDataSet = mChartAmount.getBarData().getDataSetByLabel(getString(R.string.full_payment_amount), true);
            mChartAmount.getBarData().removeDataSet(fullPaymentAmountDataSet);
        }
        if (registrationRateCtv.isChecked()) {
            registrationRateDataSet = mChartPercentage.getBarData().getDataSetByLabel(getString(R.string.registration_rate), true);
            mChartPercentage.getBarData().removeDataSet(registrationRateDataSet);
        }
        if (fullPaymentRateCtv.isChecked()) {
            fullPaymentRateDataSet = mChartPercentage.getBarData().getDataSetByLabel(getString(R.string.full_payment_rate), true);
            mChartPercentage.getBarData().removeDataSet(fullPaymentRateDataSet);
        }
        if (fullPaymentRegistrationRateCtv.isChecked()) {
            fullPaymentRegistrationRateDataSet = mChartPercentage.getBarData().getDataSetByLabel(getString(R.string.full_payment_registration_rate), true);
            mChartPercentage.getBarData().removeDataSet(fullPaymentRegistrationRateDataSet);
        }
    }

    private IBarDataSet attendAmountDataSet, registrationAmountDataSet, fullPaymentAmountDataSet;
//    private BarData tempBarDataAmount;

    private void invalidateAttendAmount() {
//        if (tempBarDataAmount == null) {
//            tempBarDataAmount = mChartAmount.getBarData();
//        }
        if (!attendAmountCtv.isChecked()) {
            attendAmountDataSet = mChartAmount.getBarData().getDataSetByLabel(getString(R.string.attend_amount), true);
            mChartAmount.getBarData().removeDataSet(attendAmountDataSet);
            attendAmountCtv.setChecked(true);
        } else {
            mChartAmount.getBarData().addDataSet(attendAmountDataSet);
            attendAmountCtv.setChecked(false);
        }
        mChartAmount.highlightValue(null);
    }

    private void invalidateRegistrationAmount() {
//        if (tempBarDataAmount == null) {
//            tempBarDataAmount = mChartAmount.getBarData();
//        }
        if (!registrationAmountCtv.isChecked()) {
            registrationAmountDataSet = mChartAmount.getBarData().getDataSetByLabel(getString(R.string.registration_amount), true);
            mChartAmount.getBarData().removeDataSet(registrationAmountDataSet);
            registrationAmountCtv.setChecked(true);
        } else {
            mChartAmount.getBarData().addDataSet(registrationAmountDataSet);
            registrationAmountCtv.setChecked(false);
        }
        mChartAmount.highlightValue(null);
    }

    private void invalidatefullPaymentAmount() {
//        if (tempBarDataAmount == null) {
//            tempBarDataAmount = mChartAmount.getBarData();
//        }
        if (!fullPaymentAmountCtv.isChecked()) {
            fullPaymentAmountDataSet = mChartAmount.getBarData().getDataSetByLabel(getString(R.string.full_payment_amount), true);
            mChartAmount.getBarData().removeDataSet(fullPaymentAmountDataSet);
            fullPaymentAmountCtv.setChecked(true);

        } else {
            mChartAmount.getBarData().addDataSet(fullPaymentAmountDataSet);
            fullPaymentAmountCtv.setChecked(false);
        }
        mChartAmount.highlightValue(null);
    }


    private IBarDataSet registrationRateDataSet, fullPaymentRateDataSet, fullPaymentRegistrationRateDataSet;
//    private BarData tempBarDataRate;

    private void invalidateRegistrationRate() {
//        if (tempBarDataRate == null) {
//            tempBarDataRate = mChartPercentage.getBarData();
//        }
        if (!registrationRateCtv.isChecked()) {
            registrationRateDataSet = mChartPercentage.getBarData().getDataSetByLabel(getString(R.string.registration_rate), true);
            mChartPercentage.getBarData().removeDataSet(registrationRateDataSet);
            registrationRateCtv.setChecked(true);
        } else {
            mChartPercentage.getBarData().addDataSet(registrationRateDataSet);
            registrationRateCtv.setChecked(false);
        }
        mChartPercentage.highlightValue(null);
    }

    private void invalidateFullPaymentRate() {
//        if (tempBarDataRate == null) {
//            tempBarDataRate = mChartPercentage.getBarData();
//        }
        if (!fullPaymentRateCtv.isChecked()) {
            fullPaymentRateDataSet = mChartPercentage.getBarData().getDataSetByLabel(getString(R.string.full_payment_rate), true);
            mChartPercentage.getBarData().removeDataSet(fullPaymentRateDataSet);
            fullPaymentRateCtv.setChecked(true);
        } else {
            mChartPercentage.getBarData().addDataSet(fullPaymentRateDataSet);
            fullPaymentRateCtv.setChecked(false);
        }
        mChartPercentage.highlightValue(null);
    }

    private void invalidateFullPaymentRegistrationRate() {
//        if (tempBarDataRate == null) {
//            tempBarDataRate = mChartPercentage.getBarData();
//        }
        if (!fullPaymentRegistrationRateCtv.isChecked()) {
            fullPaymentRegistrationRateDataSet = mChartPercentage.getBarData().getDataSetByLabel(getString(R.string.full_payment_registration_rate), true);
            mChartPercentage.getBarData().removeDataSet(fullPaymentRegistrationRateDataSet);
            fullPaymentRegistrationRateCtv.setChecked(true);
        } else {
            mChartPercentage.getBarData().addDataSet(fullPaymentRegistrationRateDataSet);
            fullPaymentRegistrationRateCtv.setChecked(false);
        }
        mChartPercentage.highlightValue(null);
    }

}
