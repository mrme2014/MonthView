package com.ishow.ischool.business.salesprocess;

import android.graphics.Color;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.commonlib.widget.TopBottomTextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.saleprocess.ChartBean;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.widget.custom.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by wqf on 16/8/14.
 */
public class SalesProcessActivity extends BaseActivity4Crm<SalesProcessPresenter, SalesProcessModel> implements SalesProcessContract.View, OnChartValueSelectedListener {

    @BindView(R.id.lineChart)
    LineChart mChart;
    @BindView(R.id.sales_avart)
    CircleImageView salesAvart;
    @BindView(R.id.sales_job)
    TopBottomTextView salesJob;
    @BindView(R.id.sales_table1)
    TopBottomTextView salesTable1;
    @BindView(R.id.sales_table2)
    TopBottomTextView salesTable2;
    @BindView(R.id.sales_trends)
    TopBottomTextView salesTrends;
    @BindView(R.id.sales_spinner)
    Spinner salesSpinner;
    @BindView(R.id.sale_legend_apply)
    Button saleLegendApply;
    @BindView(R.id.sale_legend_full)
    Button saleLegendFull;

    private SaleProcess process;
    private ChartBean chartBean;


    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_saleprocess, R.string.sale_process, R.menu.menu_statisticslist, MODE_BACK);
    }

    @Override
    protected void setUpView() {


        ArrayList<String> list = new ArrayList<>();
        list.add("7天");
        list.add("15天");
        list.add("30天");
        list.add("180天");
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_sale_process_spiner_item, list);
        salesSpinner.setAdapter(adapter);
    }

    @Override
    protected void setUpData() {
        process = new SaleProcess();
        chartBean = process.chartBean;
        initChart();
    }

    @OnItemSelected(R.id.sales_spinner)
    void OnSpinnerItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * 1.初始化LineChart
     * 2.添加数据x，y轴数据
     * 3.刷新图表
     */
    void initChart() {

        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("No Data To Show.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);


        // add data
        setData();

        mChart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        //xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(ColorTemplate.getHoloBlue());
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisMinValue(0);
        //xAxis.setCenterAxisLabels(true);
        xAxis.setLabelRotationAngle(145);
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
             /*   LogUtil.e(value + "getFormattedValue");
                if (value == 0) return "";*/
                return chartBean.date.get((int) value);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        // leftAxis.setAxisMaxValue(200f);
        leftAxis.setAxisMinValue(0);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

    }

    private void setData() {
        if (chartBean == null)
            return;

        List<String> full_amount = chartBean.full_amount;
        List<String> apply_number = chartBean.apply_number;

        ArrayList<Entry> point1 = null;
        if (apply_number != null) {
            int aplply_size = apply_number.size();
            point1 = new ArrayList<Entry>();
            for (int i = 0; i < aplply_size; i++) {
                point1.add(new Entry(i, Float.valueOf(apply_number.get(i))));
            }
        }

        ArrayList<Entry> point2 = null;
        if (full_amount != null) {
            int full_size = full_amount.size();
            point2 = new ArrayList<Entry>();
            for (int i = 0; i < full_size; i++) {
                point2.add(new Entry(i, Float.valueOf(full_amount.get(i))));
            }
        }

        LineDataSet set1, set2;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);

            set1.setValues(point1);
            set2.setValues(point2);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(point1, getString(R.string.apply_count));
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(getResources().getColor(R.color.colorPrimary));
            set1.setCircleColor(ColorTemplate.getHoloBlue());
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(getResources().getColor(R.color.colorPrimary));
            set1.setDrawCircleHole(false);
            set1.setDrawValues(set1.isDrawValuesEnabled());

            // create a dataset and give it a type
            set2 = new LineDataSet(point2, getString(R.string.full_amount));
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(Color.RED);
            set2.setCircleColor(getResources().getColor(R.color.colorAccent));
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(Color.RED);
            set2.setDrawCircleHole(false);
            set1.setHighLightColor(getResources().getColor(R.color.colorAccent));
            set2.setDrawValues(set2.isDrawValuesEnabled());
            //set2.setFillFormatter(new MyFillFormatter(900f));
            //set1.removeFirst();
            // set2.removeFirst();
            // set1.removeLast();
            // set2.removeLast();


            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            dataSets.add(set2);
            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            data.setValueTextColor(Color.GRAY);
            data.setValueTextSize(9f);

            // set data
            mChart.setData(data);
        }
    }

    private ILineDataSet apply_data_set;
    private ILineDataSet full_data_set;
    private LineData lineData;
    //private boolean apply,full;
    @OnClick({R.id.sales_table1, R.id.sales_table2, R.id.sales_job, R.id.sale_legend_apply, R.id.sale_legend_full})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sales_table1:
                break;
            case R.id.sales_table2:
                break;
            case R.id.sales_job:
                break;
            case R.id.sale_legend_apply:
                if (lineData == null) lineData = mChart.getLineData();
                if (saleLegendApply.isClickable()) {
                    apply_data_set = lineData.getDataSetByLabel(getResources().getString(R.string.apply_count), true);
                    lineData.removeDataSet(apply_data_set);
                    saleLegendApply.setClickable(false);

                } else {
                    lineData.addDataSet(apply_data_set);
                    saleLegendApply.setClickable(true);
                }
                //apply =!apply;
                mChart.invalidate();
                break;
            case R.id.sale_legend_full:
                if (lineData == null) lineData = mChart.getLineData();
                if (saleLegendFull.isClickable()) {
                    full_data_set = lineData.getDataSetByLabel(getResources().getString(R.string.full_amount), true);
                    lineData.removeDataSet(full_data_set);
                    saleLegendFull.setClickable(false);
                } else {
                    lineData.addDataSet(full_data_set);
                    saleLegendFull.setClickable(true);
                }
                mChart.invalidate();
                break;
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return true;
    }
}
