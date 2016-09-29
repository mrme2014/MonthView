package com.ishow.ischool.business.salesprocess;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Spinner;

import com.commonlib.widget.TopBottomTextView;
import com.commonlib.widget.imageloader.ImageLoaderUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.bean.saleprocess.SubordinateObject;
import com.ishow.ischool.bean.saleprocess.TableTotal;
import com.ishow.ischool.bean.user.Avatar;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.bean.user.PositionInfo;
import com.ishow.ischool.bean.user.UserInfo;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.widget.custom.AvatarImageView;
import com.ishow.ischool.widget.custom.CircleImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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
    @BindView(R.id.sales_avart_txt)
    AvatarImageView salesAvartTxt;


    private SaleProcess process;
    private int type_time = 7;
    private int campus_id;
    private int position_id;
    private int user_id;

    public static final int REQUEST_CODE = 1001;

    private final int HIDE_TABLE_PERMISSION1 = 17;
    private final int HIDE_TABLE_PERMISSION2 = 18;
    private final int HIDE_TABLE_PERMISSION3 = 19;
    private final int HIDE_TABLE_PERMISSION4 = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_saleprocess, R.string.sale_process, MODE_BACK);
    }

    @Override
    protected void setUpView() {


        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_sale_process_spiner_item, mPresenter.getSpinnerData());
        salesSpinner.setAdapter(adapter);
        salesSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mChart.onTouchEvent(event);
                mChart.clearAnimation();
                mChart.clearFocus();
                return false;
            }
        });
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

        //setUpData();
    }

    @Override
    protected void setUpData() {
        Avatar avatar = mUser.avatar;
        UserInfo userInfo = mUser.userInfo;
        CampusInfo campusInfo = mUser.campusInfo;
        PositionInfo positionInfo = mUser.positionInfo;

        if (avatar != null && !TextUtils.equals(avatar.file_name, "") && avatar.file_name != null)
            ImageLoaderUtil.getInstance().loadImage(this, avatar.file_name, salesAvart);
        else {
            salesAvart.setImageResource(R.mipmap.img_header_default);
            salesAvartTxt.setText(userInfo.user_name, userInfo.user_id, "");
            salesAvartTxt.setVisibility(View.VISIBLE);
            salesAvart.setVisibility(View.GONE);
        }
        campus_id = campusInfo.id;
        position_id = positionInfo.id;
        user_id = userInfo.user_id;
        if (userInfo != null && positionInfo != null) {
            salesJob.setFirstTxt(userInfo.user_name);
            salesJob.setSecondTxt(mUser.positionInfo.title + " |" + campusInfo.name);
        }
        if (position_id == HIDE_TABLE_PERMISSION1) {
            salesJob.setCompoundDrawables(null, null, null, null);
        }
        //销讲师 或者 晨读讲师 就不能显示 第二个表格了
        if (position_id == HIDE_TABLE_PERMISSION1
                || position_id == HIDE_TABLE_PERMISSION2
                || HIDE_TABLE_PERMISSION3 == position_id
                || HIDE_TABLE_PERMISSION4 == position_id)
            salesTable2.setVisibility(View.GONE);

    }


    private void setUpLable() {
        if (process.table != null && process.table.table1 != null) {
            TableTotal total = process.table.table1.tabletotal;
            if (total != null)
                salesTable1.setSpanedStr(getString(R.string.apply_count), total.apply_number == null ? "0" : total.apply_number + "", getString(R.string.full_amount), total.full_amount_number == null ? "0" : total.full_amount_number + "", getString(R.string.full_amount_rate), total.full_amount_rate);
            else
                salesTable1.setSpanedStr(getString(R.string.apply_count), "0", getString(R.string.full_amount), "0", getString(R.string.full_amount_rate), "0");
        }
        if (salesTable2.getVisibility() == View.VISIBLE) {
            if (process.table.table2 != null && process.table.table2.tabletotal != null) {
                TableTotal total = process.table.table2.tabletotal;
                salesTable2.setSpanedStr(getString(R.string.apply_count), total.apply_number == null ? "0" : total.apply_number + "", getString(R.string.full_amount), total.full_amount_number == null ? "0" : total.full_amount_number + "", getString(R.string.full_amount_rate), total.full_amount_rate);
            } else
                salesTable2.setSpanedStr(getString(R.string.apply_count), "0", getString(R.string.full_amount), "0", getString(R.string.full_amount_rate), "0");
        }
        final List<String> date = process.chart.date;
        salesTrends.setSecondTxt(date.get(0) + "--" + date.get(date.size() - 1));

        XAxis xAxis = mChart.getXAxis();
        xAxis.setLabelRotationAngle(-45);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value >= date.size())
                    return "";
                String s = date.get((int) value);
                return s.substring(5, s.length());
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        xAxis.setGranularity(type_time == 7 ? 1 : 5);
        mPresenter.setData(this, mChart, process.chart.full_amount, process.chart.apply_number);
        mChart.setVisibleXRangeMaximum(type_time == 7 ? 7 : 15);
        if (type_time == 7) {
            mChart.fitScreen();
        }
    }

    /*campus_id	Int	0			校区ID 总部身份选择校区时，需要传	0
position_id	Int	0			职位ID	0
user_id	Int	0			指定看某个员工的	*/
    private void getSaleProcessData() {
        handProgressbar(true);
        TreeMap map = new TreeMap();
        if (campus_id!=1){
            map.put("campus_id",campus_id);
            map.put("position_id",position_id);
            map.put("user_id",user_id);
        }
        map.put("type",type_time);
        mPresenter.getSaleProcessData(map, type_time);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position != mPresenter.getSpinnerData().size() - 1) {
            String selectTxt = mPresenter.getSpinnerData().get(position);
            String selectNum = selectTxt.substring(0, selectTxt.length() - 1);
            type_time = Integer.parseInt(selectNum);
        } else {
            type_time = 999;
        }
        getSaleProcessData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @OnClick({R.id.sales_table1, R.id.sales_table2, R.id.sales_job, R.id.sale_legend_apply, R.id.sale_legend_full})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sales_table1:
                if (process != null && process.table != null && process.table.table1 == null) {
                    showToast(getString(R.string.no_value_show));
                    return;
                }
                startActivity2SaleTable(true);
                break;
            case R.id.sales_table2:
                if (process != null && process.table != null && process.table.table2 == null) {
                    showToast(getString(R.string.no_value_show));
                    return;
                }
                startActivity2SaleTable(false);
                break;
            case R.id.sales_job:
                if (position_id == HIDE_TABLE_PERMISSION1) {
                    return;
                }
                Intent intent1 = new Intent(this, SelectPositionActivity.class);
                intent1.putExtra("REQUEST_CODE", REQUEST_CODE);
                intent1.putExtra("CAMPUS_ID", campus_id);
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

    private void startActivity2SaleTable(boolean showTable1) {

        if (process == null || process.table == null || process.table.table1 == null)
            return;
        Intent intent = new Intent(this, SaleStatementTableActivity.class);
        intent.putExtra(SaleSatementTableFragment.SHOW_TABLE1, showTable1);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(SaleSatementTableFragment.TABLE_HEAD, (ArrayList<String>) process.table.table1.tablehead);
        bundle.putSerializable(SaleSatementTableFragment.TABLE_BODY, (Serializable) process.table.table1.tablebody);
        if (View.VISIBLE == salesTable2.getVisibility() && process.table.table2 != null) {
            bundle.putStringArrayList(SaleSatementTableFragment.TABLE_HEAD_TABLE2, (ArrayList<String>) process.table.table2.tablehead);
            bundle.putSerializable(SaleSatementTableFragment.TABLE_BODY_BODY2, (Serializable) process.table.table2.tablebody);
            intent.putExtra(SaleStatementTableActivity.SHOW_MENU, true);
        }
        intent.putExtras(bundle);
        JumpManager.jumpActivity(this, intent, Resource.NO_NEED_CHECK);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handProgressbar(false);
    }
}
