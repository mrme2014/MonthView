package com.ishow.ischool.business.teachprocess;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.util.LogUtil;
import com.commonlib.widget.LabelTextView;
import com.commonlib.widget.TopBottomTextView;
import com.commonlib.widget.imageloader.ImageLoaderUtil;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Constants;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.saleprocess.SubordinateObject;
import com.ishow.ischool.bean.teachprocess.Option;
import com.ishow.ischool.bean.teachprocess.TeachProcess;
import com.ishow.ischool.bean.user.Avatar;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.bean.user.PositionInfo;
import com.ishow.ischool.bean.user.UserInfo;
import com.ishow.ischool.business.salesprocess.SelectPositionActivity;
import com.ishow.ischool.business.salesprocess.SelectSubordinateActivity;
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
 * Created by MrS on 2016/10/9.
 */

public class TeachProcessActivity extends BaseActivity4Crm<TeachPresenter, TeachModel> implements TeachProcessConact.View, AdapterView.OnItemSelectedListener, View.OnClickListener {
    @BindView(R.id.sales_avart)
    CircleImageView salesAvart;
    @BindView(R.id.sales_avart_txt)
    AvatarImageView salesAvartTxt;
    @BindView(R.id.sales_job)
    TopBottomTextView salesJob;
    @BindView(R.id.sales_trends)
    TopBottomTextView salesTrends;
    @BindView(R.id.sales_table1)
    TopBottomTextView salesTable1;
    @BindView(R.id.sales_table2)
    TextView salesTable2;
    @BindView(R.id.sales_spinner)
    Spinner salesSpinner;
    @BindView(R.id.lineChart)
    HorizontalBarChart lineChart;

    protected int start_time, end_time, position_id, user_id, campus_id, curuser_position_id;
    protected String campus_name, position_name, user_name, file_name;
    public static final int REQUEST_CODE = 1001;
    @BindView(R.id.full_amount)
    LabelTextView fullAmount;
    @BindView(R.id.upgrade_amount)
    LabelTextView upgradeAmount;
    @BindView(R.id.upgrade_base_amount)
    LabelTextView upgradeBaseAmount;
    @BindView(R.id.class_amount)
    LabelTextView classAmount;
    @BindView(R.id.lineChartTip)
    LinearLayout lineChartTip;


    private TeachProcess teachProcess;
    private Option principal;
    private TimeSeletByUserDialog timeSeletByUser;
    //这个变量是当初 spinner 不能重复选择 ，
    // 后通过反射变量值 来实现了，
    // 但当屏幕旋转后 又出现了 spinner  无故回调的问题。。。
    // 所以加这个变量来区分 是认为选择spinner  还是因为 屏幕旋转引起的回调
    private boolean isUser;
    private TreeMap map;

    @Override
    protected void setUpContentView() {
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        setContentView(R.layout.activity_teach_process, R.string.teach_process_title, R.menu.menu_submit, MODE_BACK);
        LogUtil.e("onCreate");
    }

    @Override
    protected void setUpView() {

        mPresenter.initChart(lineChart);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_sale_process_spiner_item, mPresenter.getSpinnerData());
        salesSpinner.setAdapter(adapter);
        salesSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isUser = true;
                return false;
            }
        });
        salesSpinner.setSelection(1);
        salesSpinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void setUpData() {

        campus_id = mUser.campusInfo.id;
        curuser_position_id = position_id = mUser.positionInfo.id;
        user_id = mUser.userInfo.user_id;
        //user_id = 107;
//        Calendar calendar = Calendar.getInstance();
//        start_time = (int) AppUtil.getMonthStart(calendar.get(Calendar.YEAR) + "", calendar.get(Calendar.MONTH) + "");
//        end_time = (int) AppUtil.getMonthEnd(calendar.get(Calendar.YEAR) + "", calendar.get(Calendar.MONTH) + "");
//        campus_name = mUser.campusInfo.name;
//        position_name = mUser.positionInfo.title;
//        file_name = mUser.avatar.file_name;

        if (curuser_position_id == Constants.MORNING_READ_TEACHER || curuser_position_id == Constants.CHAT_COMMISSIONER) {
            salesJob.setCompoundDrawables(null, null, null, null);
        }

        start_time = AppUtil.getDayAgoMislls(30);
        end_time = AppUtil.getTodayEndMislls();
        getTeachProcessData();
    }

    private void setUpPersonInfoByResult() {
        if (principal != null) {
            file_name = principal.avatar;
            user_id = principal.user_id;
            user_name = principal.user_name;
            position_name = principal.position_name;
            campus_name = principal.campus_name;
            position_id = principal.position_id;
            campus_id = principal.campus_id;
            //setUpPersonInfo(file_name, user_id, user_name, position_name,campus_name, position_id);
        } else {
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
    }

    private void getTeachProcessData() {
        if (map == null) map = new TreeMap();
        map.put("start_time", start_time);
        map.put("end_time", end_time);
        handProgressbar(true);
        mPresenter.getTeachProcessData(map);
    }

    @Override
    public void getListSucess(TeachProcess process) {
        handProgressbar(false);
        this.teachProcess = process;
        //如果是总部账号 数据回来后 要 重新初始化 campus_id 等字段  principal==null 是是为了 只初始化一次
        if (process != null && !process.option.isCampus && principal == null) {
            principal = process.option;
            setUpPersonInfoByResult();
        }
        //如果 不是总部账号 那么process.principal  就会为空   campus_id
        if (process != null && process.option.isCampus && campus_name == null && user_name == null && position_name == null) {
            principal = process.option;
            setUpPersonInfoByResult();
        }


        invalidateChart();
        setUpLabel();
    }

    private void invalidateChart() {
        if (teachProcess == null || teachProcess.selfChartData == null || teachProcess.selfChartData.head == null || teachProcess.selfChartData.body == null) {
            lineChartTip.setVisibility(View.GONE);
            return;
        }

        final List<String> body = teachProcess.selfChartData.body.get(0);
        final List<String> head = teachProcess.selfChartData.head;
        if (head == null || head.size() == 0 || body == null || body.size() == 0) {
            lineChartTip.setVisibility(View.GONE);
            lineChart.clear();
            return;
        }
        lineChartTip.setVisibility(View.VISIBLE);
        final int head_size = head.size() - 2;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.clear();

        for (int i = 0; i < head_size; i++) {
            float aFloat = Float.parseFloat(body.get(i));
            yVals1.add(new BarEntry((head_size - i) * 15f, aFloat));
        }

        mPresenter.setData(lineChart, yVals1);

        lineChart.invalidate();
        fullAmount.setLabelTextRight(body.get(2));
        upgradeAmount.setLabelTextRight(body.get(1));
        upgradeBaseAmount.setLabelTextRight(body.get(3));
        classAmount.setLabelTextRight(body.get(0));

    }

    private void setUpLabel() {
        //  LogUtil.e(start_time+"----"+end_time+"/********/"+principal.start_time+"******"+principal.end_time);
        salesTrends.setSecondTxt(DateUtil.parseSecond2Str((long) start_time) + "-" + DateUtil.parseSecond2Str((long) end_time));
        salesTable1.setVisibility(teachProcess.tableListData_22 == null ? View.GONE : View.VISIBLE);
        if (teachProcess == null || teachProcess.tableListData_22 == null
                || teachProcess.tableListData_22.head == null
                || teachProcess.tableListData_22.body == null)
            return;
        List<String> rate = teachProcess.tableListData_22.head;
        List<String> number = teachProcess.tableListData_22.body.get(0);
        if (rate == null || number == null || rate.size() == 0 || number.size() == 0)
            return;
        int rate_size = rate.size();
        int number_size = number.size();
        salesTable1.setSpanedStr(rate.get(rate_size - 3), number.get(number_size - 3), rate.get(rate_size - 2), number.get(number_size - 2), rate.get(rate_size - 1), number.get(number_size - 1));
    }

    @Override
    public void getListFaild(String error) {
        handProgressbar(false);
        showToast(error);
    }

    @OnClick({R.id.sales_job, R.id.sales_table1, R.id.sales_table2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sales_job:
                if (curuser_position_id == Constants.MORNING_READ_TEACHER || curuser_position_id == Constants.CHAT_COMMISSIONER) {
                    return;
                }
                Intent intent1 = new Intent(this, TeachSelectPositionActivity.class);
                intent1.putExtra("REQUEST_CODE", REQUEST_CODE);
                intent1.putExtra("CAMPUS_ID", campus_id);
                intent1.putExtra("CAMPUS_NAME", campus_name);
                JumpManager.jumpActivityForResult(this, intent1, REQUEST_CODE, Resource.NO_NEED_CHECK);
                break;
            case R.id.sales_table1:
                if (teachProcess == null || teachProcess.tableListData_22 == null ||
                        teachProcess.tableListData_22.head == null ||
                        teachProcess.tableListData_22.body == null ||
                        teachProcess.tableListData_22.head.size() == 0 ||
                        teachProcess.tableListData_22.body.size() == 0) {
                    showToast(getString(R.string.no_value_show));
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("table1_head", (ArrayList<String>) teachProcess.tableListData_22.head);
                bundle.putStringArrayList("table1_body", (ArrayList<String>) teachProcess.tableListData_22.body.get(0));
                Intent intent = new Intent(this, TeachProcessTableActivity.class);
                intent.putExtra(TeachProcessTableActivity.SHOW_TABLE1, true);
                if (salesTable2.getVisibility() == View.VISIBLE && teachProcess.tableListData != null && teachProcess.tableListData.head != null && teachProcess.tableListData.body != null) {
                    intent.putExtra(TeachProcessTableActivity.SHOW_MENU, true);
                    bundle.putStringArrayList("table2_head", (ArrayList<String>) teachProcess.tableListData.head);
                    bundle.putSerializable("table2_body", (Serializable) teachProcess.tableListData.body);
                }
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.sales_table2:
                if (teachProcess == null || teachProcess.tableListData == null ||
                        teachProcess.tableListData.head == null ||
                        teachProcess.tableListData.body == null ||
                        teachProcess.tableListData.head.size() == 0 ||
                        teachProcess.tableListData.body.size() == 0) {
                    showToast(getString(R.string.no_value_show));
                    return;
                }
                Bundle bundle1 = new Bundle();
                bundle1.putStringArrayList("table2_head", (ArrayList<String>) teachProcess.tableListData.head);
                bundle1.putSerializable("table2_body", (Serializable) teachProcess.tableListData.body);
                Intent intent2 = new Intent(this, TeachProcessTableActivity.class);
                if (teachProcess.tableListData_22 != null && teachProcess.tableListData_22.head != null && teachProcess.tableListData_22.body != null) {
                    intent2.putExtra(TeachProcessTableActivity.SHOW_MENU, true);
                    bundle1.putStringArrayList("table1_head", (ArrayList<String>) teachProcess.tableListData_22.head);
                    bundle1.putStringArrayList("table1_body", (ArrayList<String>) teachProcess.tableListData_22.body.get(0));
                }
                intent2.putExtras(bundle1);
                startActivity(intent2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && requestCode == REQUEST_CODE && data != null) {
            if (!data.hasExtra("no_choice")) {
                //选择其他职位  其他下属
                SubordinateObject extra = (SubordinateObject) data.getParcelableExtra(SelectSubordinateActivity.PICK_USER);
                position_id = data.getIntExtra(SelectSubordinateActivity.PICK_POSITION_ID, position_id);
                position_name = data.getStringExtra(SelectPositionActivity.PICK_POSITION);
                campus_id = data.getIntExtra(SelectPositionActivity.PICK_CAMPUS_ID, campus_id);
                user_id = extra.id;
                String extra_campus = data.getStringExtra(SelectPositionActivity.PICK_CAMPUS);
                if (extra_campus != null && extra_campus != "")
                    campus_name = extra_campus;
                setUpPersonInfo(extra.avatar, user_id, extra.user_name, position_name, this.campus_name, position_id);
                if (map == null) map = new TreeMap();
                map.put("position_id", position_id);
                map.put("user_id", user_id);
                getTeachProcessData();
            } else {
                //选择自己
                map.clear();
                setUpData();
                setUpPersonInfoByResult();
                //salesSpinner.setSelection(0, true);
            }

        }
    }

    private void setUpPersonInfo(String avatar, int user_id, String user_name, String position_name, String campus_name, int position_id) {
        if (avatar != null && !TextUtils.equals(avatar, "") && avatar != null && avatar != "[]") {
            salesAvart.setVisibility(View.VISIBLE);
            salesAvartTxt.setVisibility(View.GONE);
            ImageLoaderUtil.getInstance().loadImage(this, avatar, salesAvart);
        } else {
            salesAvartTxt.setText(user_name, user_id, "");
            salesAvartTxt.setVisibility(View.VISIBLE);
            salesAvart.setVisibility(View.GONE);
        }
        salesJob.setFirstTxt(user_name);
        salesJob.setSecondTxt(position_name + " | " + campus_name);

        // LogUtil.e(avatar+"---setUpPersonInfo---"+salesAvart.getVisibility());
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
        int dayAgo = 0;
        if (position < mPresenter.getSpinnerData().size() - 2) {
            String selectTxt = mPresenter.getSpinnerData().get(position);
            String selectNum = selectTxt.substring(0, selectTxt.length() - 1);
            dayAgo = Integer.parseInt(selectNum);
            start_time = AppUtil.getDayAgoMislls(dayAgo);
            end_time = AppUtil.getTodayEndMislls();
            getTeachProcessData();
        } else if (position == mPresenter.getSpinnerData().size() - 2) {
            start_time = AppUtil.getDayAgoMislls(999);
            end_time = AppUtil.getTodayEndMislls();
            getTeachProcessData();
        } else if (position == mPresenter.getSpinnerData().size() - 1) {
            if (timeSeletByUser == null) {
                timeSeletByUser = new TimeSeletByUserDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("start_time", start_time);
                bundle.putInt("end_time", end_time + 24 * 3600 - 1);
                timeSeletByUser.setArguments(bundle);
                timeSeletByUser.setOnSelectResultCallback(new TimeSeletByUserDialog.OnSelectResultCallback() {
                    @Override
                    public void onResult(int starttime, int endtime) {
                        start_time = starttime;
                        end_time = endtime;
                        LogUtil.e("timeSeletByUser"+start_time+"===="+end_time);
                        getTeachProcessData();
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
        // LogUtil.e("timeSeletByUser"+start_time+"===="+end_time);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // LogUtil.e("onNothingSelected" + salesSpinner.getSelectedItemPosition());
    }

    @Override
    protected void onPause() {
        super.onPause();
        isUser = false;
    }
}
