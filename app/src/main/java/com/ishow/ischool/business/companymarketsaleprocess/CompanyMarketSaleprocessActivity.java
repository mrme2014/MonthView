package com.ishow.ischool.business.companymarketsaleprocess;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.commonlib.util.DateUtil;
import com.commonlib.util.LogUtil;
import com.commonlib.widget.TopBottomTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Constants;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.saleprocess.ComMarketProcess;
import com.ishow.ischool.bean.saleprocess.SaleTable1;
import com.ishow.ischool.bean.saleprocess.SubordinateObject;
import com.ishow.ischool.business.salesprocess.SaleSatementTableFragment;
import com.ishow.ischool.business.salesprocess.SaleStatementTableActivity;
import com.ishow.ischool.business.salesprocess.SalesProcessActivity;
import com.ishow.ischool.business.salesprocess.SelectPositionActivity;
import com.ishow.ischool.business.salesprocess.SelectSubordinateActivity;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.fragment.TimeSeletByUserDialog;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.AvatarImageView;
import com.ishow.ischool.widget.custom.PieChartView;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by MrS on 2016/11/8.
 */

public class CompanyMarketSaleprocessActivity extends BaseActivity4Crm<ComSalePresenter, ComModel> implements ComIView, AdapterView.OnItemSelectedListener {
    @BindView(R.id.sales_avart_txt)
    AvatarImageView salesAvartTxt;
    @BindView(R.id.sales_job)
    TopBottomTextView salesJob;
    @BindView(R.id.sales_layout)
    LinearLayout salesLayout;
    @BindView(R.id.sales_trends)
    TopBottomTextView salesTrends;
    @BindView(R.id.sales_spinner)
    Spinner salesSpinner;
    @BindView(R.id.lineChart)
    PieChartView lineChart;
    @BindView(R.id.sales_table1)
    TopBottomTextView salesTable1;

    int begin_time, end_time;
    private boolean isUser = true;
    private TimeSeletByUserDialog timeSeletByUser;
    private SaleTable1 table1;

    private int curuser_position_id;
    private int campus_id;
    private String campus_name;
    public static final int REQUEST_CODE = 1001;
    private int position_id;
    private String position_name;
    private int user_id;
    boolean isHeadQuatyer;
    private ComMarketProcess process;
    private ArrayList<String> des;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_company_market_saleprocess, R.string.sale_process, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        // campus_id = mUser.campusInfo.id;
        campus_id = 2;
        //  campus_name = mUser.campusInfo.name;
        campus_name = "杭州校区";
        curuser_position_id = mUser.positionInfo.id;
        isHeadQuatyer = campus_id == Constants.CAMPUS_HEADQUARTERS;
          /*总部角色  拉取图表数据用现在接口，，，非总部的角色 用 市场------销售流程分析的接口*/
        if (campus_id == Constants.CAMPUS_HEADQUARTERS) {
            isHeadQuatyer = true;
        }
        salesAvartTxt.setText(mUser.userInfo.user_name, mUser.userInfo.user_id, mUser.avatar.file_name);
        salesJob.setFirstTxt(mUser.userInfo.user_name);
        salesJob.setSecondTxt(mUser.positionInfo.title + " | " + campus_name);
        // begin_time =  AppUtil.getWeekStart();
        // end_time = AppUtil.getWeekEnd();

        //  LogUtil.e(begin_time+"---setUpView--"+end_time);
    }

    private void getComMarketSaleProcess() {
        mPresenter.getComMarketSaleprocesssChart(begin_time, end_time);
        mPresenter.getComMarketSaleprocessTableData(begin_time, end_time);
    }

    @Override
    protected void setUpData() {
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_sale_process_spiner_item, AppUtil.getComMarketSaleProcessList());
        salesSpinner.setAdapter(adapter);
        salesSpinner.setOnItemSelectedListener(this);
        salesSpinner.setSelection(0);
        salesSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isUser = true;
                return false;
            }
        });
    }

    @OnClick({R.id.sales_layout, R.id.sales_table1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sales_layout:
                //校聊专员 晨读讲师
                if (curuser_position_id == Constants.MORNING_READ_TEACHER || curuser_position_id == Constants.CHAT_COMMISSIONER) {
                    return;
                }
                Intent intent1 = new Intent(this, SelectPositionActivity.class);
                intent1.putExtra("REQUEST_CODE", REQUEST_CODE);
                intent1.putExtra("CAMPUS_ID", campus_id);
                intent1.putExtra("CAMPUS_NAME", campus_name);
                JumpManager.jumpActivityForResult(this, intent1, REQUEST_CODE, Resource.NO_NEED_CHECK);
                break;
            case R.id.sales_table1:
                if (table1 == null || table1.tablehead == null || table1.tablebody == null || table1.tablebody.size() == 0) {
                    showToast(getString(R.string.no_value_show));
                    return;
                }
                Intent intent = new Intent(this, SaleStatementTableActivity.class);
                intent.putExtra(SaleSatementTableFragment.SHOW_TABLE1, true);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(SaleSatementTableFragment.TABLE_HEAD, (ArrayList<String>) table1.tablehead);
                bundle.putSerializable(SaleSatementTableFragment.TABLE_BODY, (Serializable) table1.tablebody);
                intent.putExtras(bundle);
                JumpManager.jumpActivity(this, intent, Resource.NO_NEED_CHECK);
                break;
        }
    }

    @Override
    public void getListSucess(SaleTable1 table1) {
        this.table1 = table1;
        //LogUtil.e(begin_time+"---"+(begin_time+12*3600)+"----"+end_time);

    }

    @Override
    public void getChartSucess(ComMarketProcess process) {
        if (des == null) des = new ArrayList<>();
        des.add(getString(R.string.class_numbers));
        des.add(getString(R.string.open_class));
        des.add(getString(R.string.apply_numbers));
        des.add(getString(R.string.full_amount_number));

        if (process == null || process.process == null) {
            salesTable1.setSpanedStr(getString(R.string.update_rate), "0", getString(R.string.fullpay_rate), "0", getString(R.string.tuikuan_rate), "0");
            List<String> list = new ArrayList<>();
            list.add("0");
            list.add("0");
            list.add("0");
            list.add("0");
            PieChartView.Biulder biulder = new PieChartView.Biulder();
            biulder.setPieChartBaseColor(R.color.colorPrimaryDark1).setDrawNums(list).setDrawTxtDes(des).DrawPercentFloor(1, 0, "0").DrawPercentFloor(3, 0, "0");
            lineChart.invalidate(biulder);

            return;
        }
        this.process = process;
        salesTrends.setSecondTxt(DateUtil.parseSecond2Str((long) (begin_time + 24 * 3600)) + " -- " + DateUtil.parseSecond2Str((long) end_time));
        List<String> list = new ArrayList<>();
        list.add(process.process.add_number);
        list.add(process.process.openclass_sign_number);
        list.add(process.process.openclass_apply_number);
        list.add(process.process.openclass_full_amount_number);

        String rate1 = process.process.openclass_apply_rate;
        String rate2 = process.process.full_amount_rate;

        PieChartView.Biulder biulder = new PieChartView.Biulder();
        biulder.setPieChartBaseColor(R.color.colorPrimaryDark).setDrawNums(list).setDrawTxtDes(des).DrawPercentFloor(1, 0, "35%").DrawPercentFloor(3, 0, "45%");
        lineChart.invalidate(biulder);
        salesTable1.setSpanedStr(getString(R.string.update_rate),
                process.process.openclass_sign_number,
                getString(R.string.fullpay_rate),
                process.process.full_amount_rate,
                getString(R.string.tuikuan_rate),
                process.process.openclass_apply_rate);
    }

    @Override
    public void getListFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            //以下三行代码是解决问题所在
            Field field = AdapterView.class.getDeclaredField("mOldSelectedPosition");
            field.setAccessible(true);  //设置mOldSelectedPosition可访问
            field.setInt(salesSpinner, AdapterView.INVALID_POSITION); //设置mOldSelectedPosition的值
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!isUser)
            return;
        isUser = false;

        if (position == 0) {
            begin_time = (int) AppUtil.getWeekStart();
            end_time = (int) AppUtil.getWeekEnd();
        } else if (position == 1) {
            begin_time = (int) AppUtil.getMonthStart();
            end_time = (int) AppUtil.getMonthEnd();
        } else if (position == 2) {
            begin_time = (int) AppUtil.getLastWeekStart();
            end_time = (int) AppUtil.getLastWeekEnd();
        } else if (position == 3) {
            begin_time = (int) AppUtil.getLastMonthStart();
            end_time = (int) AppUtil.getLastMonthEnd();
        } else if (position == 4) {
            if (timeSeletByUser == null) {
                timeSeletByUser = new TimeSeletByUserDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("start_time", begin_time);
                bundle.putInt("end_time", end_time);
                timeSeletByUser.setArguments(bundle);
                timeSeletByUser.setOnSelectResultCallback(new TimeSeletByUserDialog.OnSelectResultCallback() {
                    @Override
                    public void onResult(int start_time, int over_time) {
                        begin_time = start_time;
                        end_time = over_time;
                        LogUtil.e("-setOnSelectResultCallback----" + begin_time + "----" + end_time);
                    }

                    @Override
                    public void onEorr(String error) {
                        showToast(error);
                    }

                    @Override
                    public void onCacel() {
                        timeSeletByUser = null;
                    }
                });
                if (!timeSeletByUser.isAdded())
                    timeSeletByUser.show(getSupportFragmentManager(), "dialog");
            }
        }
        LogUtil.e(position + "-----" + begin_time + "----" + end_time);
        getComMarketSaleProcess();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && requestCode == REQUEST_CODE && data != null) {
            if (!data.hasExtra("no_choice")) {
                SubordinateObject extra = (SubordinateObject) data.getParcelableExtra(SelectSubordinateActivity.PICK_USER);
                position_id = data.getIntExtra(SelectSubordinateActivity.PICK_POSITION_ID, position_id);
                position_name = data.getStringExtra(SelectPositionActivity.PICK_POSITION);
                campus_id = data.getIntExtra(SelectPositionActivity.PICK_CAMPUS_ID, campus_id);
                user_id = extra.id;
                String extra_campus = data.getStringExtra(SelectPositionActivity.PICK_CAMPUS);
                if (extra_campus != null && extra_campus != "")
                    campus_name = extra_campus;

                LogUtil.e("onActivityResult" + campus_id);
                if (campus_id == Constants.CAMPUS_HEADQUARTERS)
                    getComMarketSaleProcess();
                else
                    startActivity2OldSaleProcessActivity(extra);

            } else {
                getComMarketSaleProcess();
            }
        } else if (requestCode == 101 && requestCode == REQUEST_CODE && data != null) {
            salesSpinner.setSelection(0);
            getComMarketSaleProcess();
        }
    }

    private void startActivity2OldSaleProcessActivity(SubordinateObject extra) {
         /*要改的 这里*/
        Intent intent = new Intent(this, SalesProcessActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("campus_id", campus_id);
        intent.putExtra("position_id", position_id);
        intent.putExtra("position_name", position_name);
        intent.putExtra("start_time", begin_time);
        intent.putExtra("end_time", end_time);
        intent.putExtra("campus_name", campus_name);
        intent.putExtra("extra", extra);
        intent.putExtra("from_commarket", true);

        startActivityForResult(intent, 101);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
