package com.ishow.ischool.business.registrationform;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.widget.LabelTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.registrationform.RegistraInfo;
import com.ishow.ischool.bean.registrationform.RegistraResult;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.RegistraTableRowTextView;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;

/**
 * Created by MrS on 2016/11/24.
 */

public class registrationInfoConfirmActivity extends BaseActivity4Crm<regisPresenter, regisModel> implements regisView {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.name)
    LabelTextView name;
    @BindView(R.id.english_name)
    LabelTextView englishName;
    @BindView(R.id.phone)
    LabelTextView phone;
    @BindView(R.id.identy)
    LabelTextView identy;
    @BindView(R.id.course_type)
    LabelTextView courseType;
    @BindView(R.id.date_want)
    LabelTextView dateWant;
    @BindView(R.id.registra_left1)
    LabelTextView registraLeft1;
    @BindView(R.id.registra_left2)
    LabelTextView registraLeft2;
    @BindView(R.id.registra_left3)
    LabelTextView registraLeft3;
    @BindView(R.id.registra_row1)
    RegistraTableRowTextView registraRow1;
    @BindView(R.id.registra_row2)
    RegistraTableRowTextView registraRow2;
    @BindView(R.id.registra_row3)
    RegistraTableRowTextView registraRow3;
    @BindView(R.id.registra_row4)
    RegistraTableRowTextView registraRow4;
    @BindView(R.id.cheap_type)
    LabelTextView cheapType;
    @BindView(R.id.pay_type)
    LabelTextView payType;
    @BindView(R.id.pay_money)
    LabelTextView payMoney;
    @BindView(R.id.pay_real)
    LabelTextView payReal;
    @BindView(R.id.pay_recept_no)
    LabelTextView payReceptNo;
    @BindView(R.id.pay_date)
    LabelTextView payDate;
    @BindView(R.id.sec_pay_date)
    LabelTextView secPayDate;
    @BindView(R.id.pay_memo)
    LabelTextView payMemo;

    public static final String STUDENT_ID = "student_id";
    public static final String STUDENT_STATUS = "student_status";
    public static final String REQUEST_CODE = "request_code";

    private int student_id;
    private int student_status;
    private String action;
    private String feilds = "payListInfo,studentInfo";

    @Override
    protected void initEnv() {
        super.initEnv();
        student_id = getIntent().getIntExtra(STUDENT_ID, student_id);
        student_status = getIntent().getIntExtra(STUDENT_STATUS, student_status);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_registration_info_sure, R.string.registration_apply_sure_title, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        if (student_status == 1) {
            action = "apply";
        } else if (student_status == 2) {
            action = "pay";
        }
        mPresenter.getPayInfo(student_id, student_status, action, feilds);
    }

    @Override
    protected void setUpData() {

    }


    @Override
    public void getRegistraInfo(RegistraResult registraResult) {
        if (registraResult != null) {
            List<List<Integer>> free_time_arr = registraResult.free_time_arr;
            setUpFreeTimeTable(free_time_arr);
            StudentInfo studentInfo = registraResult.studentInfo;
            List<RegistraInfo> payListInfo = registraResult.payListInfo;
            if (payListInfo != null && payListInfo.size() > 0) {
                RegistraInfo registraInfo = payListInfo.get(payListInfo.size() - 2);
                setUpRegistrationInfoFirst(registraInfo);
            }

            setUpStudentInfo(studentInfo);
        }
    }

    private void setUpFreeTimeTable(List<List<Integer>> free_time_arr) {
        registraRow1.setTxtList(AppUtil.getWeek());
        registraRow2.setIconList((free_time_arr == null || free_time_arr.size() == 0) ? null : free_time_arr.get(0));
        registraRow3.setIconList((free_time_arr == null || free_time_arr.size() == 0) ? null : free_time_arr.get(free_time_arr.size() > 1 ? 1 : 0));
        registraRow4.setIconList((free_time_arr == null || free_time_arr.size() == 0) ? null : free_time_arr.get(free_time_arr.size() > 2 ? 2 : 1));
    }

    private void setUpRegistrationInfoFirst(RegistraInfo registraInfo) {
        String pay_info = registraInfo.pay_info;
        Type type1 = new TypeToken<List<PayType>>() {
        }.getType();
        Gson gson = new Gson();
        List<PayType> typeList = gson.fromJson(pay_info, type1);
        if (typeList != null) {
            payMoney.setText(registraInfo.arrearage + "元");
            payReal.setText(typeList.get(typeList.size() - 1).balance + "元");
            for (int i = 0; i < typeList.size(); i++) {
                payType.append(typeList.get(i).method + "  ");
            }
        }

        cheapType.setText(registraInfo.preferential_course_name);

        payMemo.setText(registraInfo.memo + "");
        payDate.setText(DateUtil.parseSecond2Str(Long.valueOf(registraInfo.pay_time)));
        if (registraInfo.arrearage_time != 0)
            secPayDate.setText(DateUtil.parseSecond2Str(Long.valueOf(registraInfo.arrearage_time)));
        payReceptNo.setText(registraInfo.receipt_no);

    }

    private void setUpStudentInfo(StudentInfo studentInfo) {
        name.setText(studentInfo.name);
        englishName.setText(studentInfo.english_name);
        phone.setText(studentInfo.mobile);
        identy.setText(studentInfo.idcard);
        courseType.setText(studentInfo.intention_class_name);
        dateWant.setText(DateUtil.parseSecond2Str(Long.valueOf(studentInfo.intention_time)));
    }

    @Override
    public void getRegistraError(String error) {
        showToast(error);
    }

    @Override
    public void payActionSucess(String info) {

    }
}
