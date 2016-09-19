package com.ishow.ischool.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonlib.core.BaseFragment;
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
import com.ishow.ischool.common.manager.CampusManager;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wqf on 16/9/13.
 */
public class BarChartFragment extends BaseFragment {

    @BindView(R.id.chart1)
    BarChart mChartAmount;
    @BindView(R.id.chart2)
    BarChart mChartPercentage;
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
    private boolean curAmountMode = true;
    private BarData amountBarData, percentageBarData;
    private ArrayList<String> mCampusDatas;
    private String label1, label2, label3;
    private Random random = new Random();      //用于产生随机数字
    private boolean isFirst = true;

    //    public static BarChartFragment newInstance(String campus_id, String source) {
    public static BarChartFragment newInstance() {
        BarChartFragment fragment = new BarChartFragment();
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
        View view = inflater.inflate(R.layout.fragment_barchart, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        mCampusDatas = new ArrayList<>();
        mCampusDatas.addAll(CampusManager.getInstance().getCampusNames());
        initAmountChart();
    }

    private void initAmountChart() {
        mChartAmount.setScaleEnabled(false);
        mChartAmount.setDescription("");
        mChartAmount.setNoDataText("no data");

        mChartAmount.getLegend().setEnabled(false);

        YAxis leftAxis = mChartAmount.getAxisLeft();
        leftAxis.setDrawAxisLine(false);       //是否绘制坐标轴的线，即含有坐标的那条线，默认是true
        leftAxis.setSpaceBottom(0f);
        leftAxis.setAxisMinValue(0f);
        mChartAmount.getAxisRight().setEnabled(false);    // 隐藏右边的坐标轴

        XAxis xAxis = mChartAmount.getXAxis();
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
                    return mCampusDatas.get((int) value % mCampusDatas.size());
                } else {
                    return "";
                }
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        setAmountData(mCampusDatas, null);
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
                    return mCampusDatas.get((int) value % mCampusDatas.size());
                } else {
                    return "";
                }
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        setPercentageData(mCampusDatas, null);
    }

    public void setAmountData(ArrayList<String> xData, ArrayList<String> yData) {
        label1 = getString(R.string.attend_amount);
        label2 = getString(R.string.registration_amount);
        label3 = getString(R.string.full_payment_amount);
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();

        for (int i = 0; i < xData.size() + 1; i++) {
            yVals1.add(new BarEntry(i, random.nextInt(15) + 1));
            yVals2.add(new BarEntry(i, random.nextInt(20) + 2));
            yVals3.add(new BarEntry(i, random.nextInt(25) + 3));
        }

        // create 3 datasets with different types
        BarDataSet set1, set2, set3;
        if (mChartAmount.getData() != null && mChartAmount.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChartAmount.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) mChartAmount.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) mChartAmount.getData().getDataSetByIndex(2);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            mChartAmount.getData().notifyDataChanged();
            mChartAmount.notifyDataSetChanged();
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

            amountBarData = new BarData(dataSets);
            mChartAmount.setData(amountBarData);
//            chart.setFitBars(true);
        }

        float groupSpace = 0.31f;
        float barSpace = 0.08f; // x3 dataset
        float barWidth = 0.15f; // x3 dataset
        // (0.15 + 0.08) * 3 + 0.31 = 1.00 -> interval per "group"
        mChartAmount.getBarData().setBarWidth(barWidth);
        mChartAmount.getBarData().setDrawValues(false);
        mChartAmount.groupBars(0, groupSpace, barSpace);
        mChartAmount.animateXY(800, 800);//图表数据显示动画
        //        mChart.getXAxis().setAxisMaxValue(6);
        mChartAmount.setVisibleXRangeMaximum(6);  //设置屏幕显示条数
        mChartAmount.invalidate();

        mChartAmount.getXAxis().setGranularity(0f);
    }


    public void setPercentageData(ArrayList<String> xData, ArrayList<String> yData) {
        label1 = getString(R.string.registration_rate);
        label2 = getString(R.string.full_payment_rate);
        label3 = getString(R.string.full_payment_registration_rate);
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();

        for (int i = 0; i < xData.size() + 1; i++) {
            yVals1.add(new BarEntry(i, random.nextInt(15) + 1));
            yVals2.add(new BarEntry(i, random.nextInt(20) + 2));
            yVals3.add(new BarEntry(i, random.nextInt(25) + 3));
        }

        // create 3 datasets with different types
        BarDataSet set1, set2, set3;
        if (mChartPercentage.getData() != null && mChartPercentage.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChartPercentage.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) mChartPercentage.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) mChartPercentage.getData().getDataSetByIndex(2);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            mChartPercentage.getData().notifyDataChanged();
            mChartPercentage.notifyDataSetChanged();
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

            percentageBarData = new BarData(dataSets);
            mChartPercentage.setData(percentageBarData);
//            chart.setFitBars(true);
        }

        float groupSpace = 0.31f;
        float barSpace = 0.08f; // x3 dataset
        float barWidth = 0.15f; // x3 dataset
        // (0.15 + 0.08) * 3 + 0.31 = 1.00 -> interval per "group"
        mChartPercentage.getBarData().setBarWidth(barWidth);
        mChartPercentage.getBarData().setDrawValues(false);
        mChartPercentage.groupBars(0, groupSpace, barSpace);
        mChartPercentage.animateXY(800, 800);//图表数据显示动画
        //        mChart.getXAxis().setAxisMaxValue(6);
        mChartPercentage.setVisibleXRangeMaximum(6);  //设置屏幕显示条数
        mChartPercentage.invalidate();

        mChartPercentage.getXAxis().setGranularity(0f);
    }


    @OnClick({R.id.chart_switch, R.id.legend_attend_amount, R.id.legend_registration_amount, R.id.legend_full_payment_amount,
            R.id.legend_registration_rate, R.id.legend_full_payment_rate, R.id.legend_full_payment_registration_rate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_switch:
                if (curAmountMode) {
                    curAmountMode = false;
                    if (isFirst) {
                        initPercentageChart();
                        isFirst = false;
                    }
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
            amountBarData.removeDataSet(attendAmountDataSet);
            attendAmountCtv.setChecked(true);
        } else {
            amountBarData.addDataSet(attendAmountDataSet);
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
            amountBarData.removeDataSet(registrationAmountDataSet);
            registrationAmountCtv.setChecked(true);
        } else {
            amountBarData.addDataSet(registrationAmountDataSet);
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
            amountBarData.removeDataSet(fullPaymentAmountDataSet);
            fullPaymentAmountCtv.setChecked(true);

        } else {
            amountBarData.addDataSet(fullPaymentAmountDataSet);
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
            percentageBarData.removeDataSet(registrationRateDataSet);
            registrationRateCtv.setChecked(true);
        } else {
            percentageBarData.addDataSet(registrationRateDataSet);
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
            percentageBarData.removeDataSet(fullPaymentRateDataSet);
            fullPaymentRateCtv.setChecked(true);
        } else {
            percentageBarData.addDataSet(fullPaymentRateDataSet);
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
            percentageBarData.removeDataSet(fullPaymentRegistrationRateDataSet);
            fullPaymentRegistrationRateCtv.setChecked(true);
        } else {
            percentageBarData.addDataSet(fullPaymentRegistrationRateDataSet);
            fullPaymentRegistrationRateCtv.setChecked(false);
        }
        mChartPercentage.invalidate();
    }

}
