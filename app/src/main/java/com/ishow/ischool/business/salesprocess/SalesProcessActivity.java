package com.ishow.ischool.business.salesprocess;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Spinner;

import com.commonlib.util.LogUtil;
import com.commonlib.util.UIUtil;
import com.commonlib.widget.LabelTextView;
import com.commonlib.widget.TopBottomTextView;
import com.commonlib.widget.imageloader.ImageLoaderUtil;
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
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.saleprocess.ChartBean;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.bean.saleprocess.Table;
import com.ishow.ischool.bean.user.Avatar;
import com.ishow.ischool.bean.user.PositionInfo;
import com.ishow.ischool.bean.user.UserInfo;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
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
    CheckedTextView saleLegendApply;
    @BindView(R.id.sale_legend_full)
    CheckedTextView saleLegendFull;

    private SaleProcess process;
    private ChartBean chartBean;
    private LabelTextView ltv;
    public static final int REQUEST_CODE = 10001;


    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_saleprocess, R.string.sale_process, R.menu.menu_sale, MODE_BACK);
    }

    @Override
    protected void setUpView() {

        MenuItem item = mToolbar.getMenu().findItem(R.id.submit);
        ltv = (LabelTextView) MenuItemCompat.getActionView(item);
        ltv.setPadding(0, 0, UIUtil.dip2px(this, 10), 0);

        ArrayList<String> list = new ArrayList<>();
        list.add("7天");
        list.add("15天");
        list.add("30天");
        list.add("180天");
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_sale_process_spiner_item, list);
        salesSpinner.setAdapter(adapter);

        mChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mChart.getScaleX() > 1f || mChart.getScaleY() > 1f)
                    mChart.requestDisallowInterceptTouchEvent(true);
                    //mChart.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    @Override
    protected void setUpData() {
        process = new SaleProcess();
        chartBean = process.chartBean;

        if (process.saleTable1 != null) {
            Table table = process.saleTable1.table;
            List<String> tablehead = process.saleTable1.tablehead;
            if (table == null) return;
            salesTable1.setSpanedStr(tablehead.get(6), table.apply_numbers + "", tablehead.get(8), table.full_amount + "", tablehead.get(9), table.full_amount_rate + "");

        }

        if (process.saleTable2 != null) {
            Table table = process.saleTable2.table;
            if (table == null) return;
            List<String> tablehead = process.saleTable2.tablehead;

        }


        Avatar avatar = mUser.avatar;
        if (avatar != null && !TextUtils.equals(avatar.file_name, "") && avatar.file_name != null)
            ImageLoaderUtil.getInstance().loadImage(this, avatar.file_name, salesAvart);
        UserInfo userInfo = mUser.userInfo;
        PositionInfo positionInfo = mUser.positionInfo;
        if (userInfo != null && positionInfo != null) {
            salesJob.setFirstTxt(userInfo.user_name);
        }
        ltv.setText(positionInfo.campus);
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
        xAxis.setTextColor(ContextCompat.getColor(this, R.color.sale_gray_txt_color_));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisMinValue(0);
        //xAxis.setCenterAxisLabels(true);
        //xAxis.setLabelRotationAngle(145);
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
        leftAxis.setTextColor(ContextCompat.getColor(this, R.color.sale_gray_txt_color_));
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
            set1.setColor(ContextCompat.getColor(this, R.color.sale_apply_fill_color_));
            set1.setCircleColor(ContextCompat.getColor(this, R.color.sale_apply_fill_color_));
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(255);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighlightEnabled(true);
            set1.setHighLightColor(ContextCompat.getColor(this, R.color.sale_apply_fill_color_));
            set1.setDrawCircleHole(false);
            set1.setDrawValues(set1.isDrawValuesEnabled());

            // create a dataset and give it a type
            set2 = new LineDataSet(point2, getString(R.string.full_amount));
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(ContextCompat.getColor(this, R.color.sale_full_fill_color_));
            set2.setCircleColor(ContextCompat.getColor(this, R.color.sale_full_fill_color_));
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(255);
            set2.setFillColor(ContextCompat.getColor(this, R.color.sale_full_fill_color_));
            set2.setDrawCircleHole(false);
            set2.setDrawHighlightIndicators(true);
            set2.setHighLightColor(ContextCompat.getColor(this, R.color.sale_full_fill_color_));
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


    //private boolean apply,full;
    @OnClick({R.id.sales_table1, R.id.sales_table2, R.id.sales_job, R.id.sale_legend_apply, R.id.sale_legend_full})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sales_table1:
                Intent intent = new Intent(this, SaleStatementTableActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(SaleStatementTableActivity.TABLE_HEAD, (ArrayList<String>) process.saleTable1.tablehead);
                bundle.putParcelableArrayList(SaleStatementTableActivity.TABLE_BODY, (ArrayList<? extends Parcelable>) process.saleTable1.tablebody);
                intent.putExtras(bundle);
                JumpManager.jumpActivity(this, intent, Resource.NO_NEED_CHECK);
                break;
            case R.id.sales_table2:
                break;
            case R.id.sales_job:
                JumpManager.jumpActivityForResult(this, SelectSubordinatesActivity.class, REQUEST_CODE, Resource.NO_NEED_CHECK);
                break;
            case R.id.sale_legend_apply:
                invalidateApplyCount();
                break;
            case R.id.sale_legend_full:
                invalidateFullAmount();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (lineData == null) lineData = mChart.getLineData();
        LogUtil.e(e.toString() + "----" + h.toString());
        //ToastUtils.showToast(this,h.);
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return true;
    }

    private ILineDataSet apply_data_set;
    private ILineDataSet full_data_set;
    private LineData lineData;

    private void invalidateFullAmount() {
        if (lineData == null) lineData = mChart.getLineData();
        if (!saleLegendFull.isChecked()) {
            full_data_set = lineData.getDataSetByLabel(getResources().getString(R.string.full_amount), true);
            lineData.removeDataSet(full_data_set);
            saleLegendFull.setChecked(true);
        } else {
            lineData.addDataSet(full_data_set);
            saleLegendFull.setChecked(false);
        }
        mChart.invalidate();
    }

    private void invalidateApplyCount() {
        if (lineData == null) lineData = mChart.getLineData();
        if (!saleLegendApply.isChecked()) {
            apply_data_set = lineData.getDataSetByLabel(getResources().getString(R.string.apply_count), true);
            lineData.removeDataSet(apply_data_set);
            saleLegendApply.setChecked(true);

        } else {
            lineData.addDataSet(apply_data_set);
            saleLegendApply.setChecked(false);
        }
        mChart.invalidate();
    }

    @Override
    public void getListSuccess(SaleProcess saleProcess) {

    }

    @Override
    public void getListFail(String msg) {

    }
}
