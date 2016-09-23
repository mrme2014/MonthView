package com.ishow.ischool.business.salesprocess;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.commonlib.widget.LabelTextView;
import com.commonlib.widget.TopBottomTextView;
import com.commonlib.widget.imageloader.ImageLoaderUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.bean.saleprocess.SubordinateObject;
import com.ishow.ischool.bean.saleprocess.Table;
import com.ishow.ischool.bean.user.Avatar;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.bean.user.UserInfo;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.CampusManager;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.widget.custom.CircleImageView;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqf on 16/8/14.
 */
public class SalesProcessActivity extends BaseActivity4Crm<SalesProcessPresenter, SalesProcessModel> implements SalesProcessContract.View<SaleProcess>, AdapterView.OnItemSelectedListener {

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

    private LabelTextView ltv;
    private SaleProcess process;
    private int type_time = 7;
    private int campus_id;
    private int position_id;
    private int user_id;

    public static final int REQUEST_CODE = 1001;


    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_saleprocess, R.string.sale_process, R.menu.menu_sale, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        MenuItem item = mToolbar.getMenu().findItem(R.id.submit);
        ltv = (LabelTextView) MenuItemCompat.getActionView(item);
        ltv.setAboutMenuItem();
        Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.icon_screen_down_white);
        ltv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        ltv.setUpMenu(true);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_sale_process_spiner_item, mPresenter.getSpinnerData());
        salesSpinner.setAdapter(adapter);
        salesSpinner.setOnItemSelectedListener(this);

        mPresenter.initChart(this, mChart);
        mChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mChart.getScaleX() > 1f || mChart.getScaleY() > 1f)
                    mChart.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        setUpData();
    }

    @Override
    protected void setUpData() {
        Avatar avatar = mUser.avatar;
        if (avatar != null && !TextUtils.equals(avatar.file_name, "") && avatar.file_name != null)
            ImageLoaderUtil.getInstance().loadImage(this, avatar.file_name, salesAvart);
        else salesAvart.setImageResource(R.mipmap.img_header_default);

        UserInfo userInfo = mUser.userInfo;
        CampusInfo positionInfo = mUser.campusInfo;
        campus_id = positionInfo.id;
        position_id = positionInfo.id;
        user_id = userInfo.user_id;
        if (userInfo != null && positionInfo != null) {
            salesJob.setFirstTxt(userInfo.user_name);
        }

        ltv.setEllipsizeText(positionInfo.name, 7);
    }


    private void setUpLable() {
        if (process.table1 != null) {
            Table table = process.table1.table;
            List<String> tablehead = process.table1.tablehead;
            if (table == null) return;
            salesTable1.setSpanedStr(tablehead.get(6), table.apply_numbers + "", tablehead.get(8), table.full_amount + "", tablehead.get(9), table.full_amount_rate * 100 + "%");
        }
        /*if (process.table2 != null) {
            Table table = process.table2.table;
            if (table == null) return;
            List<String> tablehead = process.table2.tablehead;
        }*/

        XAxis xAxis = mChart.getXAxis();
        xAxis.setLabelRotationAngle(-45);
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value >= process.chart.date.size())
                    return "";
                String s = process.chart.date.get((int) value);
                return s.substring(5, s.length());
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        mPresenter.setData(this, mChart, process.chart.full_amount, process.chart.apply_number);
        mChart.setVisibleXRangeMaximum(type_time == 7 ? type_time : 15);
        if (type_time == 7) mChart.fitScreen();
    }

    private void getSaleProcessData() {
        handProgressbar(true);
        mPresenter.getSaleProcessData(campus_id, position_id, user_id, type_time);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectTxt = mPresenter.getSpinnerData().get(position);
        String selectNum = selectTxt.substring(0, selectTxt.length() - 1);
        type_time = Integer.parseInt(selectNum);
        getSaleProcessData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @OnClick({R.id.sales_table1, R.id.sales_table2, R.id.sales_job, R.id.sale_legend_apply, R.id.sale_legend_full})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sales_table1:
                if (process.table1 == null)
                    return;
                Intent intent = new Intent(this, SaleStatementTableActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(SaleStatementTableActivity.TABLE_HEAD, (ArrayList<String>) process.table1.tablehead);
                bundle.putSerializable(SaleStatementTableActivity.TABLE_BODY, process.table1.tablebody);
                intent.putExtras(bundle);
                JumpManager.jumpActivity(this, intent, Resource.NO_NEED_CHECK);
                break;
            case R.id.sales_table2:
                if (process.table2 == null)
                    return;
                Intent intent2 = new Intent(this, SaleStatementTableActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putStringArrayList(SaleStatementTableActivity.TABLE_HEAD, (ArrayList<String>) process.table2.tablehead);
                bundle2.putSerializable(SaleStatementTableActivity.TABLE_BODY, process.table2.tablebody);
                intent2.putExtras(bundle2);
                JumpManager.jumpActivity(this, intent2, Resource.NO_NEED_CHECK);
                break;
            case R.id.sales_job:
                Intent intent1 = new Intent(this, SelectPositionActivity.class);
                intent1.putExtra("REQUEST_CODE", REQUEST_CODE);
                intent1.putExtra("CAMPUS_ID",campus_id);
                JumpManager.jumpActivityForResult(this, intent1, REQUEST_CODE, Resource.NO_NEED_CHECK);
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
        if (requestCode == REQUEST_CODE && requestCode == REQUEST_CODE && data != null) {
            SubordinateObject extra = (SubordinateObject) data.getParcelableExtra(SelectSubordinateActivity.PICK_USER);
            position_id = data.getIntExtra(SelectSubordinateActivity.PICK_POSITION_ID, position_id);
            user_id = extra.id;
            salesJob.setFirstTxt(extra.user_name);
            salesJob.setSecondTxt(data.getStringExtra(SelectPositionActivity.PICK_POSITION));
            salesAvart.setImageResource(R.mipmap.img_header_default);
            getSaleProcessData();
        }
    }

    private ArrayList<CampusInfo> campusInfos;

    @OnClick(R.id.submit)
    void onMenuClick() {
        if (campusInfos == null) {
            campusInfos = CampusManager.getInstance().get();
        } else getCampusSucess(campusInfos);
    }

    private ILineDataSet apply_data_set;
    private ILineDataSet full_data_set;
    private LineData lineData;

    private void invalidateFullAmount() {
        if (lineData == null) lineData = mChart.getLineData();
        if (lineData == null) return;
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
        if (lineData == null) return;
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
        this.process = saleProcess;
        handProgressbar(false);
        setUpLable();
        mChart.invalidate();
    }

    @Override
    public void getListFail(String msg) {
        handProgressbar(false);
        showToast(msg);
    }

    private void getCampusSucess(final ArrayList<CampusInfo> campusInfos) {
        if (campusInfos == null || campusInfos.size() == 0) return;
        PickerDialogFragment.Builder builder = new PickerDialogFragment.Builder();
        ArrayList<String> campus = new ArrayList();
        for (int i = 0; i < campusInfos.size(); i++) {
            campus.add(campusInfos.get(i).name);
        }
        builder.setBackgroundDark(true).setDialogTitle(R.string.switch_role).setDialogType(PickerDialogFragment.PICK_TYPE_OTHERS).setDatas(0, 1, campus);
        PickerDialogFragment fragment = builder.Build();
        fragment.show(getSupportFragmentManager(), "dialog");
        fragment.addCallback(new PickerDialogFragment.Callback<int[]>() {
            @Override
            public void onPickResult(int[] selectIds, String... result) {
                ltv.setText(result[0]);
                campus_id = campusInfos.get(selectIds[0]).id;
                getSaleProcessData();
            }
        });
    }
}
