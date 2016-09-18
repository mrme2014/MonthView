package com.ishow.ischool.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonlib.core.BaseFragment;
import com.commonlib.util.LogUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
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

/**
 * Created by wqf on 16/9/13.
 */
public class BarChartFragment extends BaseFragment {

    private BarChart mChartQuantity, mChartPercentage;
    private TextView titleTv, subtitleTv, switcTv;
    private boolean curQuantityMode = true;
    private ArrayList<String> mCampusDatas;
    private String label1, label2, label3;
    private Random random = new Random();      //用于产生随机数字

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
        mChartQuantity = (BarChart) view.findViewById(R.id.chart1);
        mChartPercentage = (BarChart) view.findViewById(R.id.chart2);
        titleTv = (TextView) view.findViewById(R.id.chart_title);
        subtitleTv = (TextView) view.findViewById(R.id.subtitle);
        switcTv = (TextView) view.findViewById(R.id.chart_switch);
        switcTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curQuantityMode) {
                    curQuantityMode = false;
                    mChartPercentage.setVisibility(View.VISIBLE);
                    mChartQuantity.setVisibility(View.GONE);
                    titleTv.setText("招生数据对比");
                    subtitleTv.setText("比例(%)");
                    switcTv.setText("按人数");
                } else {
                    curQuantityMode = true;
                    mChartQuantity.setVisibility(View.VISIBLE);
                    mChartPercentage.setVisibility(View.GONE);
                    titleTv.setText("招生人数对比");
                    subtitleTv.setText("人数(个)");
                    switcTv.setText("按比例");
                }
            }
        });
        return view;
    }

    @Override
    public void init() {
        mCampusDatas = new ArrayList<>();
        mCampusDatas.addAll(CampusManager.getInstance().getCampusNames());
        initQuantityChart();
        initPercentageChart();
        setData(mCampusDatas, null, mChartQuantity);
        setData(mCampusDatas, null, mChartPercentage);
    }

    private void initQuantityChart() {
        mChartQuantity.setDescription("");
        mChartQuantity.setNoDataText("no data");

        Legend legend = mChartQuantity.getLegend();      // 设置坐标线描述的样式
        legend.setFormSize(10f);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setWordWrapEnabled(true);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        YAxis leftAxis = mChartQuantity.getAxisLeft();
        leftAxis.setDrawAxisLine(false);       //是否绘制坐标轴的线，即含有坐标的那条线，默认是true
        leftAxis.setSpaceBottom(0f);
        leftAxis.setAxisMinValue(0f);
        mChartQuantity.getAxisRight().setEnabled(false);    // 隐藏右边的坐标轴

        XAxis xAxis = mChartQuantity.getXAxis();
        xAxis.setAxisMinValue(0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);      //是否显示X坐标轴上的刻度竖线，默认是true
        xAxis.setGranularity(0f);    // only intervals of 1 day
//        xAxis.setLabelCount(mCampusDatas.size());
        xAxis.setLabelRotationAngle(-80);       //设置x轴字体显示角度
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setXOffset(20);
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
    }


    private void initPercentageChart() {
        mChartPercentage.setDescription("");
        mChartPercentage.setNoDataText("no data");

        Legend legend = mChartPercentage.getLegend();      // 设置坐标线描述的样式
        legend.setFormSize(10f);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setWordWrapEnabled(true);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

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
                return (int)value + "%";
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
        xAxis.setLabelRotationAngle(-80);       //设置x轴字体显示角度
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setXOffset(100);
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
    }

    public void setData(ArrayList<String> xData, ArrayList<String> yData, BarChart chart) {
        if (chart == mChartQuantity) {
            label1 = "总现场";
            label2 = "总报名";
            label3 = "总全款";
        } else {
            label1 = "总报名率";
            label2 = "总全款率";
            label3 = "总全款报名率";
        }
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
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) chart.getData().getDataSetByIndex(2);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
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

            BarData data = new BarData(dataSets);
            chart.setData(data);
//            chart.setFitBars(true);
        }

        float groupSpace = 0.31f;
        float barSpace = 0.08f; // x3 dataset
        float barWidth = 0.15f; // x3 dataset
        // (0.15 + 0.08) * 3 + 0.31 = 1.00 -> interval per "group"
        chart.getBarData().setBarWidth(barWidth);
        chart.getBarData().setDrawValues(false);
        chart.groupBars(0, groupSpace, barSpace);
        //        mChart.animateXY(800, 800);//图表数据显示动画
        //        mChart.getXAxis().setAxisMaxValue(6);
        chart.setVisibleXRangeMaximum(6);  //设置屏幕显示条数
        chart.invalidate();

        chart.getXAxis().setGranularity(1f);
    }


}
