package com.ishow.ischool.business.salesprocess;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Spinner;

import com.commonlib.util.UIUtil;
import com.commonlib.widget.LabelTextView;
import com.commonlib.widget.TopBottomTextView;
import com.commonlib.widget.imageloader.ImageLoaderUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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
public class SalesProcessActivity extends BaseActivity4Crm<SalesProcessPresenter, SalesProcessModel> implements SalesProcessContract.View {

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

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_sale_process_spiner_item, mPresenter.getSpinnerData());
        salesSpinner.setAdapter(adapter);

        mChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mChart.getScaleX() > 1f || mChart.getScaleY() > 1f)
                    mChart.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    @Override
    protected void setUpData() {
        process = new SaleProcess(30);
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
        //initChart();
        mPresenter.initChart(this,mChart,chartBean.date);
        mPresenter.setData(this,mChart,chartBean.full_amount,chartBean.apply_number);
        mChart.setVisibleXRangeMaximum(15);
    }

    @OnItemSelected(R.id.sales_spinner)
    void OnSpinnerItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

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
                //invalidateFullAmount();
                process = new SaleProcess(10);
                mPresenter.setData(this,mChart,process.chartBean.full_amount,process.chartBean.apply_number);
                mChart.setVisibleXRangeMaximum(7);
                mChart.invalidate();
                mChart.fitScreen();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
