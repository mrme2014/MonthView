package com.ishow.ischool.business.salesprocess;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.commonlib.application.ActivityStackManager;
import com.commonlib.util.LogUtil;
import com.commonlib.widget.TopBottomTextView;
import com.commonlib.widget.imageloader.ImageLoaderUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Constants;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.saleprocess.Principal;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.bean.saleprocess.SubordinateObject;
import com.ishow.ischool.bean.saleprocess.TableTotal;
import com.ishow.ischool.bean.user.Avatar;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.bean.user.PositionInfo;
import com.ishow.ischool.bean.user.UserInfo;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.fragment.TimeSeletByUserDialog;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.AvatarImageView;
import com.ishow.ischool.widget.custom.CircleImageView;

import java.io.Serializable;
import java.lang.reflect.Field;
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
    private int position_id;
    private int user_id;
    private int campus_id;
    private String campus_name;
    private String position_name;
    private String file_name;
    private int curuser_position_id;
    private String user_name;

    public static final int REQUEST_CODE = 1001;
    private Principal principal;

    private float downX = 0;
    private TreeMap map;
    private TimeSeletByUserDialog timeSeletByUser;
    //这个变量是当初 spinner 不能重复选择 ，
    // 后通过反射变量值 来实现了，
    // 但当屏幕旋转后 又出现了 spinner  无故回调的问题。。。
    // 所以加这个变量来区分 是认为选择spinner  还是因为 屏幕旋转引起的回调
    private boolean isUser;

    private int start_time, end_time;
    //*********************这里是从集团市场 销售流程传过来的*****************************************//
    private boolean from_commarket;
    private SubordinateObject extra;

    @Override
    protected void initEnv() {
        super.initEnv();
        user_id = getIntent().getIntExtra("user_id", user_id);
        campus_id = getIntent().getIntExtra("campus_id", campus_id);
        position_id = getIntent().getIntExtra("position_id", position_id);
        position_name = getIntent().getStringExtra("position_name");
        start_time = getIntent().getIntExtra("start_time", start_time);
        end_time = getIntent().getIntExtra("end_time", end_time);
        from_commarket = getIntent().getBooleanExtra("from_commarket", false);
        campus_name = getIntent().getStringExtra("campus_name");
        extra = getIntent().getParcelableExtra("extra");
        //如果 是从  集团 市场 销售流程传过来的    下面三句代码 就不能再初始化了
        if (!from_commarket) {
            campus_id = mUser.campusInfo.id;
            curuser_position_id = position_id = mUser.positionInfo.id;
            user_id = mUser.userInfo.user_id;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_saleprocess, R.string.sale_process, MODE_NONE);
        mToolbar = (Toolbar) findViewById(com.commonlib.R.id.toolbar);
        mToolbarTitle = (TextView) findViewById(com.commonlib.R.id.toolbar_title);
        mToolbarTitle.setText(getString(R.string.sale_process));
        mToolbar.setNavigationIcon(R.mipmap.icon_return);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityStackManager.getInstance().popActivity("CompanyMarketSaleprocess");
                SalesProcessActivity.this.finish();
            }
        });
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
                mChart.setDrawMarkers(false);
                isUser = true;
                return false;
            }
        });
        salesSpinner.setOnItemSelectedListener(this);
        mPresenter.initChart(this, mChart);
        mChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        break;
                    //  case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                        //      LogUtil.e(event.getX()-downX+"---"+ViewConfiguration.get(SalesProcessActivity.this).getScaledTouchSlop());
                        if (Math.abs(event.getX() - downX) <= ViewConfiguration.get(SalesProcessActivity.this).getScaledTouchSlop()) {
                            mChart.setDrawMarkers(true);
                        }
                        break;
                }
                if (mChart.getScaleX() > 1f || mChart.getScaleY() > 1f)
                    mChart.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //setUpData();
    }

    @Override
    protected void setUpData() {
        //  setUpDataByResult();
        start_time = AppUtil.getDayAgo(7);
        end_time = AppUtil.getTodayEnd();
        getSaleProcessData();
    }

    private void setUpDataByResult() {
        if (principal != null && !from_commarket) {
            file_name = principal.avatar;
            user_id = principal.user_id;
            user_name = principal.user_name;
            position_name = principal.position_name;
            campus_name = principal.campus_name;
            position_id = principal.position_id;
            campus_id = principal.campus_id;
            //setUpPersonInfo(file_name, user_id, user_name, position_name,campus_name, position_id);
        } else if (!from_commarket) {
            Avatar avatar = mUser.avatar;
            UserInfo userInfo = mUser.userInfo;
            CampusInfo campusInfo = mUser.campusInfo;
            PositionInfo positionInfo = mUser.positionInfo;
            campus_id = campusInfo.id;
            curuser_position_id = position_id = positionInfo.id;
            user_id = userInfo.user_id;
            campus_name = campusInfo.name;
            position_name = positionInfo.title;
            file_name = avatar.file_name;
            user_name = userInfo.user_name;
        }
        setUpPersonInfo(file_name, user_id, user_name, position_name, campus_name, position_id);
        if (from_commarket) {
            setUpPersonInfo(extra.avatar, user_id, extra.user_name, position_name, campus_name, position_id);
        }
    }

    private void setUpPersonInfo(String file_name, int user_id, String user_name, String position_name, String campus_name, int position_id) {
        if (file_name != null && !TextUtils.equals(file_name, "") && file_name != null && file_name != "[]") {
            salesAvartTxt.setVisibility(View.GONE);
            salesAvart.setVisibility(View.VISIBLE);
            ImageLoaderUtil.getInstance().loadImage(this, file_name, salesAvart);
        } else {
        /*    ImageLoaderUtil.getInstance().loadImage(this, file_name, salesAvart);
            salesAvart.setImageResource(R.mipmap.img_header_default);*/
            salesAvartTxt.setText(user_name, user_id, "");
            salesAvartTxt.setVisibility(View.VISIBLE);
            salesAvart.setVisibility(View.GONE);
        }
        salesJob.setFirstTxt(user_name);
        salesJob.setSecondTxt(position_name + " | " + campus_name);

        if (curuser_position_id == Constants.MORNING_READ_TEACHER || curuser_position_id == Constants.CHAT_COMMISSIONER) {
            salesJob.setCompoundDrawables(null, null, null, null);
        }
        //销讲师 或者 晨读讲师 就不能显示 第二个表格了
        if (position_id == Constants.COURSE_CONSULTANT_LEADER
                || position_id == Constants.CHAT_COMMISSIONER
                || Constants.COURSE_CONSULTANT == position_id
                || Constants.MORNING_READ_TEACHER == position_id)
            salesTable2.setVisibility(View.GONE);
        else salesTable2.setVisibility(View.VISIBLE);


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
        // start_time = DateUtil.date2Second(date.get(0));
        // end_time = DateUtil.date2Second(date.get(date.size() - 1)) + 24 * 3600;
        int oneday = AppUtil.getOneDayStart("2016-09-01");
        if (start_time > oneday)
            salesTrends.setSecondTxt(date.get(0) + "--" + date.get(date.size() - 1));
        else salesTrends.setSecondTxt("2016-09-01" + "--" + date.get(date.size() - 1));
//        start_time = DateUtil.date2Second(date.get(0));
//        end_time = DateUtil.date2Second(date.get(date.size() - 1)) + 24 * 3600;

        XAxis xAxis = mChart.getXAxis();
        xAxis.setLabelRotationAngle(-45);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                value = Math.abs(value);
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
            mChart.notifyDataSetChanged();
        }
    }

    /*campus_id	Int	0			校区ID 总部身份选择校区时，需要传	0
position_id	Int	0			职位ID	0
user_id	Int	0			指定看某个员工的	*/
    private void getSaleProcessData() {
        handProgressbar(true);
        if (map == null) map = new TreeMap();
        if (campus_id != Constants.CAMPUS_HEADQUARTERS && campus_id != 0) {
            map.put("campus_id", campus_id);
            map.put("position_id", position_id);
            map.put("user_id", user_id);
            map.put("begin_time", start_time);
            map.put("end_time", end_time);
        }
        map.put("type", type_time);
        mPresenter.getSaleProcessData(map, type_time);
        LogUtil.e(start_time + "getSaleProcessData" + end_time);
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
        map.clear();
        if (saleLegendApply.isChecked()) invalidateApplyCount();
        if (saleLegendFull.isChecked()) invalidateFullAmount();
        //  mChart.setDrawMarkers(true);
        if (position < mPresenter.getSpinnerData().size() - 2) {
            String selectTxt = mPresenter.getSpinnerData().get(position);
            String selectNum = selectTxt.substring(0, selectTxt.length() - 1);
            type_time = Integer.parseInt(selectNum);
            start_time = AppUtil.getDayAgo(type_time);
            int oneday = AppUtil.getOneDayStart("2016-09-01") - 12 * 3600;
            if (start_time < oneday) start_time = oneday;
            end_time = AppUtil.getTodayEnd();
            getSaleProcessData();
        } else if (position == mPresenter.getSpinnerData().size() - 2) {
            type_time = 999;
            int oneday = AppUtil.getOneDayStart("2016-09-01") - 12 * 3600;
            start_time = AppUtil.getDayAgo(type_time);
            if (start_time < oneday) start_time = oneday;
            end_time = AppUtil.getTodayEnd();
            getSaleProcessData();
        } else if (position == mPresenter.getSpinnerData().size() - 1) {
            type_time = 999;
            if (timeSeletByUser == null) {
                timeSeletByUser = new TimeSeletByUserDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("start_time", start_time);
                bundle.putInt("end_time", end_time);
                timeSeletByUser.setArguments(bundle);
                timeSeletByUser.setOnSelectResultCallback(new TimeSeletByUserDialog.OnSelectResultCallback() {
                    @Override
                    public void onResult(int starttime, int endtime) {
                        start_time = starttime;
                        end_time = endtime;
                        if (map == null) map = new TreeMap();
                        map.put("begin_time", starttime);
                        map.put("end_time", endtime);
                        LogUtil.e("timeSeletByUser" + start_time + "====" + end_time);
                        getSaleProcessData();
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
            }
            if (!timeSeletByUser.isAdded())
                timeSeletByUser.show(getSupportFragmentManager(), "dialog");
        }
        LogUtil.e("---------------" + start_time + "====" + end_time);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @OnClick({R.id.sales_table1, R.id.sales_table2, R.id.sales_job, R.id.sale_legend_apply, R.id.sale_legend_full})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sales_table1:
                if (process == null || process.table == null ||
                        process.table.table1 == null ||
                        process.table.table1.tablehead == null ||
                        process.table.table1.tablehead.size() == 0) {
                    showToast(getString(R.string.no_value_show));
                    return;
                }
                startActivity2SaleTable(true);
                break;
            case R.id.sales_table2:
                if (process == null || process.table == null ||
                        process.table.table2 == null ||
                        process.table.table2.tablehead == null ||
                        process.table.table2.tablehead.size() == 0) {
                    showToast(getString(R.string.no_value_show));
                    return;
                }
                startActivity2SaleTable(false);
                break;
            case R.id.sales_job:
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
            if (!data.hasExtra("no_choice")) {
                SubordinateObject extra = (SubordinateObject) data.getParcelableExtra(SelectSubordinateActivity.PICK_USER);
                position_id = data.getIntExtra(SelectSubordinateActivity.PICK_POSITION_ID, position_id);
                position_name = data.getStringExtra(SelectPositionActivity.PICK_POSITION);
                campus_id = data.getIntExtra(SelectPositionActivity.PICK_CAMPUS_ID, campus_id);
                user_id = extra.id;
                String extra_campus = data.getStringExtra(SelectPositionActivity.PICK_CAMPUS);
                if (extra_campus != null && extra_campus != "")
                    campus_name = extra_campus;

                setUpPersonInfo(extra.avatar, user_id, extra.user_name, position_name, this.campus_name, position_id);
            } else {
                if (from_commarket) {
                    setResult(101, data);
                    this.finish();
                    return;
                }
                Avatar avatar = mUser.avatar;
                UserInfo userInfo = mUser.userInfo;
                CampusInfo campusInfo = mUser.campusInfo;
                PositionInfo positionInfo = mUser.positionInfo;
                campus_id = campusInfo.id;
                curuser_position_id = position_id = positionInfo.id;
                user_id = userInfo.user_id;
                campus_name = campusInfo.name;
                position_name = positionInfo.title;
                file_name = avatar.file_name;
                user_name = userInfo.user_name;
                //setUpDataByResult();
                setUpPersonInfo(file_name,user_id,user_name,position_name,campus_name,campus_id);
                salesSpinner.setSelection(0, true);
                type_time = 7;
            }
            LogUtil.e("onActivityResult" + campus_id);
            if (map == null) map = new TreeMap();
            map.clear();
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
        //mChart.setDrawMarkers(!saleLegendFull.isChecked());
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
        //mChart.setDrawMarkers(!saleLegendApply.isChecked());
        //mChart.getData().notifyDataChanged();
        mChart.invalidate();
    }

    @Override
    public void getListSuccess(SaleProcess saleProcess) {
        this.process = saleProcess;
        handProgressbar(false);
        //如果是总部账号 数据回来后 要 重新初始化 campus_id 等字段  principal==null 是是为了 只初始化一次
        if (process != null && process.principal != null && principal == null) {
            principal = saleProcess.principal;
            setUpDataByResult();
        }
        //如果 不是总部账号 那么process.principal  就会为空   campus_id
        if (process.principal == null && campus_name == null && user_name == null && position_name == null)
            setUpDataByResult();
        setUpLable();
        mChart.invalidate();
        mChart.animateX(2500);
        mChart.animateY(2500);
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
    protected void onPause() {
        super.onPause();
        isUser = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handProgressbar(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityStackManager.getInstance().popActivity("CompanyMarketSaleprocess");
    }
}
