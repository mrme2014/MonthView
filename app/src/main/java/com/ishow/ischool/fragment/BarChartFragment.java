package com.ishow.ischool.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.campusperformance.SignAmount;
import com.ishow.ischool.bean.campusperformance.SignAmountResult;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.business.campusperformance.CampusPerformanceTableActivity;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.DataApi;
import com.ishow.ischool.common.manager.CampusManager;
import com.ishow.ischool.common.manager.JumpManager;

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
    @BindView(R.id.legend_attend_amount)
    CheckedTextView attendAmountCtv;
    @BindView(R.id.legend_registration_amount)
    CheckedTextView registrationAmountCtv;
    @BindView(R.id.legend_full_payment_amount)
    CheckedTextView fullPaymentAmountCtv;
    @BindView(R.id.chart2_layout)
    LinearLayout chart2Layout;
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
    public ArrayList<CampusInfo> lastShowCampus;      // 上次显示的校区
    private ArrayList<SignAmount> mYDatas;
    private String label1, label2, label3;

    public void pullData(final ArrayList<CampusInfo> showCampus, int beginMonth, int endMonth) {
        String campusParam = "";     // 默认所有
        ArrayList<CampusInfo> allCampus = new ArrayList<>();
        allCampus.addAll(CampusManager.getInstance().get());
        for (CampusInfo info : allCampus) {
            campusParam = campusParam + info.id + ",";
        }
        ApiFactory.getInstance().getApi(DataApi.class).getSignAmount(1, campusParam,
                beginMonth, endMonth, null, "signTotal")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<SignAmountResult>() {
                    @Override
                    public void onSuccess(SignAmountResult result) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barchart, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        mBarDataAmount = new BarData();
        mBarDataPercentage = new BarData();
        lastShowCampus = new ArrayList<>();
        mCampusInfos = CampusManager.getInstance().get();
        mXDatas = new ArrayList<>();
        mXDatas.addAll(CampusManager.getInstance().getCampusNames());
        mCount = mXDatas.size();

        initAmountChart();
        initPercentageChart();
        pullData(mCampusInfos, 201607, 201609);
    }

    private void initAmountChart() {
        mChartAmount.setScaleEnabled(false);
        mChartAmount.setDescription("");
        mChartAmount.setNoDataText("no data");

        mChartAmount.getLegend().setEnabled(false);

        YAxis leftAxis = mChartAmount.getAxisLeft();
        leftAxis.setDrawAxisLine(false);       //是否绘制坐标轴的线，即含有坐标的那条线，默认是true
        leftAxis.setAxisMinValue(0f);
        leftAxis.setSpaceBottom(20f);
        mChartAmount.getAxisRight().setEnabled(false);    // 隐藏右边的坐标轴

        XAxis xAxis = mChartAmount.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);      //是否显示X坐标轴上的刻度竖线，默认是true
        xAxis.setAxisMinValue(0f);
        xAxis.setGranularity(0f);    // only intervals of 1 day
        xAxis.setLabelRotationAngle(-60);       //设置x轴字体显示角度
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setCenterAxisLabels(true);
//        xAxis.setAxisMaxValue(mCount + 2);
        xAxis.setValueFormatter(new AxisValueFormatter() {
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


    private void initPercentageChart() {
        mChartPercentage.setScaleEnabled(false);
        mChartPercentage.setDescription("");
        mChartPercentage.setNoDataText("no data");

        mChartPercentage.getLegend().setEnabled(false);

        //设置y轴的样式
        YAxis leftAxis = mChartPercentage.getAxisLeft();
        leftAxis.setDrawAxisLine(false);       //是否绘制坐标轴的线，即含有坐标的那条线，默认是true
        leftAxis.setAxisMinValue(0f);
        leftAxis.setAxisMaxValue(100f);
        leftAxis.setGranularity(20);
        leftAxis.setSpaceBottom(20f);
        mChartPercentage.getAxisRight().setEnabled(false);    // 隐藏右边的坐标轴
        leftAxis.setValueFormatter(new AxisValueFormatter() {
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
        xAxis.setGranularity(0f);    // only intervals of 1 day
//        xAxis.setLabelCount(mCampusDatas.size());
        xAxis.setLabelRotationAngle(-60);       //设置x轴字体显示角度
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setCenterAxisLabels(true);
//        xAxis.setAxisMaxValue(mCount + 2);
        xAxis.setValueFormatter(new AxisValueFormatter() {
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
        ArrayList<SignAmount> datas = new ArrayList<>();
        if (lastShowCampus != null && lastShowCampus.size() > 0) {      // 即不是第一次
            mXDatas.clear();
            ArrayList<CampusInfo> allCampusInfos = CampusManager.getInstance().get();
            for (CampusInfo campusInfo : campusInfos) {
                mXDatas.add(campusInfo.name);
                datas.add(mYDatas.get(allCampusInfos.indexOf(campusInfo)));
            }
            mCount = mXDatas.size();
            datas.add(new SignAmount());  // 填充最后一个
            barData.removeDataSet(barChart.getBarData().getDataSetByLabel(label1, true));
            barData.removeDataSet(barChart.getBarData().getDataSetByLabel(label2, true));
            barData.removeDataSet(barChart.getBarData().getDataSetByLabel(label3, true));
            barChart.clearValues();
        } else {
            datas.addAll(mYDatas);
        }

        if (barChart == mChartPercentage) {
            lastShowCampus.clear();         // 更新上一次显示的校区
            lastShowCampus.addAll(tempCampusInfos);
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();


        for (int i = 0; i < mXDatas.size() + 1; i++) {
            if (barChart == mChartAmount) {
                yVals1.add(new BarEntry(i, datas.get(i).scene));
                yVals2.add(new BarEntry(i, datas.get(i).sign));
                yVals3.add(new BarEntry(i, datas.get(i).fullPay));
            } else {
                yVals1.add(new BarEntry(i, datas.get(i).signRate));
                yVals2.add(new BarEntry(i, datas.get(i).fullRate));
                yVals3.add(new BarEntry(i, datas.get(i).fullSignRate));
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
        if (mCount < 6) {
            barChart.getXAxis().setLabelCount(mCount);
        }
        barChart.setVisibleXRangeMaximum(mCount > 6 ? 6 : mCount);      //设置屏幕显示条数
        barChart.invalidate();
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
                    titleTv.setText("招生数据对比");
                    subtitleTv.setText("比例(%)");
                    switcTv.setText("按人数");
                } else {
                    curAmountMode = true;
                    chart1Layout.setVisibility(View.VISIBLE);
                    chart2Layout.setVisibility(View.GONE);
                    titleTv.setText("招生人数对比");
                    subtitleTv.setText("人数(个)");
                    switcTv.setText("按比例");
                }
                break;
            case R.id.legend_attend_amount:
                invalidateAttendAmount();
                break;
            case R.id.legend_registration_amount:
                invalidateRegistrationAmount();
                break;
            case R.id.legend_full_payment_amount:
                invalidatefullPaymentAmount();
                break;
            case R.id.legend_registration_rate:
                invalidateRegistrationRate();
                break;
            case R.id.legend_full_payment_rate:
                invalidateFullPaymentRate();
                break;
            case R.id.legend_full_payment_registration_rate:
                invalidateFullPaymentRegistrationRate();
                break;
            case R.id.table_layout:
                Intent intent = new Intent(getActivity(), CampusPerformanceTableActivity.class);
                JumpManager.jumpActivity(getActivity(), intent, Resource.NO_NEED_CHECK);
                break;
        }
    }

    private IBarDataSet attendAmountDataSet, registrationAmountDataSet, fullPaymentAmountDataSet;
    private BarData tempBarDataAmount;

    private void invalidateAttendAmount() {
        if (tempBarDataAmount == null) {
            tempBarDataAmount = mChartAmount.getBarData();
        }
        if (!attendAmountCtv.isChecked()) {
            attendAmountDataSet = tempBarDataAmount.getDataSetByLabel(getString(R.string.attend_amount), true);
            mBarDataAmount.removeDataSet(attendAmountDataSet);
            attendAmountCtv.setChecked(true);
        } else {
            mBarDataAmount.addDataSet(attendAmountDataSet);
            attendAmountCtv.setChecked(false);
        }
        mChartAmount.invalidate();
    }

    private void invalidateRegistrationAmount() {
        if (tempBarDataAmount == null) {
            tempBarDataAmount = mChartAmount.getBarData();
        }
        if (!registrationAmountCtv.isChecked()) {
            registrationAmountDataSet = tempBarDataAmount.getDataSetByLabel(getString(R.string.registration_amount), true);
            mBarDataAmount.removeDataSet(registrationAmountDataSet);
            registrationAmountCtv.setChecked(true);
        } else {
            mBarDataAmount.addDataSet(registrationAmountDataSet);
            registrationAmountCtv.setChecked(false);
        }
        mChartAmount.invalidate();
    }

    private void invalidatefullPaymentAmount() {
        if (tempBarDataAmount == null) {
            tempBarDataAmount = mChartAmount.getBarData();
        }
        if (!fullPaymentAmountCtv.isChecked()) {
            fullPaymentAmountDataSet = tempBarDataAmount.getDataSetByLabel(getString(R.string.full_payment_amount), true);
            mBarDataAmount.removeDataSet(fullPaymentAmountDataSet);
            fullPaymentAmountCtv.setChecked(true);

        } else {
            mBarDataAmount.addDataSet(fullPaymentAmountDataSet);
            fullPaymentAmountCtv.setChecked(false);
        }
        mChartAmount.invalidate();
    }


    private IBarDataSet registrationRateDataSet, fullPaymentRateDataSet, fullPaymentRegistrationRateDataSet;
    private BarData tempBarDataRate;

    private void invalidateRegistrationRate() {
        if (tempBarDataRate == null) {
            tempBarDataRate = mChartPercentage.getBarData();
        }
        if (!registrationRateCtv.isChecked()) {
            registrationRateDataSet = tempBarDataRate.getDataSetByLabel(getString(R.string.registration_rate), true);
            mBarDataPercentage.removeDataSet(registrationRateDataSet);
            registrationRateCtv.setChecked(true);
        } else {
            mBarDataPercentage.addDataSet(registrationRateDataSet);
            registrationRateCtv.setChecked(false);
        }
        mChartPercentage.invalidate();
    }

    private void invalidateFullPaymentRate() {
        if (tempBarDataRate == null) {
            tempBarDataRate = mChartPercentage.getBarData();
        }
        if (!fullPaymentRateCtv.isChecked()) {
            fullPaymentRateDataSet = tempBarDataRate.getDataSetByLabel(getString(R.string.full_payment_rate), true);
            mBarDataPercentage.removeDataSet(fullPaymentRateDataSet);
            fullPaymentRateCtv.setChecked(true);
        } else {
            mBarDataPercentage.addDataSet(fullPaymentRateDataSet);
            fullPaymentRateCtv.setChecked(false);
        }
        mChartPercentage.invalidate();
    }

    private void invalidateFullPaymentRegistrationRate() {
        if (tempBarDataRate == null) {
            tempBarDataRate = mChartPercentage.getBarData();
        }
        if (!fullPaymentRegistrationRateCtv.isChecked()) {
            fullPaymentRegistrationRateDataSet = tempBarDataRate.getDataSetByLabel(getString(R.string.full_payment_registration_rate), true);
            mBarDataPercentage.removeDataSet(fullPaymentRegistrationRateDataSet);
            fullPaymentRegistrationRateCtv.setChecked(true);
        } else {
            mBarDataPercentage.addDataSet(fullPaymentRegistrationRateDataSet);
            fullPaymentRegistrationRateCtv.setChecked(false);
        }
        mChartPercentage.invalidate();
    }

}
