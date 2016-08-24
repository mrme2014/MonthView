package com.ishow.ischool.business.communication.add;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.commonlib.widget.LabelEditText;
import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resourse;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.business.student.pick.PickStudentActivity;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.SelectDialogFragment;
import com.ishow.ischool.widget.pickerview.PickerWheelViewPop;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class CommunicationAddActivity extends BaseActivity4Crm<CommunicationAddPresenter, CommunicationAddModel> implements CommunicationAddContract.View {

    private static final int REQUEST_PICK_STUDENT = 100;
    public static final String P_STUDENT = "student";

    @BindView(R.id.commun_student_name)
    LabelTextView studentNameTv;

    @BindView(R.id.commun_state)
    LabelTextView stateTv;

    @BindView(R.id.commun_faith)
    LabelTextView faithTv;

    @BindView(R.id.commun_oppose)
    LabelTextView opposeTv;

    @BindView(R.id.commun_back_date)
    LabelTextView backDateTv;

    @BindView(R.id.commun_date)
    LabelTextView dateTv;

    @BindView(R.id.commun_source)
    LabelEditText moneySourceTv;

    @BindView(R.id.commun_content)
    LabelEditText contentTv;


    private StudentInfo studentInfo;
    private CommunicationForm form = new CommunicationForm();

    private boolean isSubmitting;

    @Override
    protected void initEnv() {
        super.initEnv();
        studentInfo = getIntent().getParcelableExtra(P_STUDENT);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_communication_add, R.string.add_communication, R.menu.menu_communication_add, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        updateStudentView();
    }

    @Override
    protected void setUpData() {
        form.resources_id = Resourse.COMMUNICATION_ADD;
    }

    @Override
    public void onAddSuccess() {
        isSubmitting = false;
        showToast(R.string.add_success);
        finish();
    }

    @Override
    public void onAddFailed(String msg) {
        isSubmitting = false;
        showToast(msg);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        onSubmit();
        return super.onMenuItemClick(item);
    }


    public void onSubmit() {
        if (isSubmitting) {
            return;
        }
        if (checkForm()) {
            isSubmitting = true;
            mPresenter.addCommunication(form);
        }
    }

    private boolean checkForm() {

        form.content = contentTv.getText().toString();
        form.tuition_source = moneySourceTv.getText().toString();

        //----
        form.type = 1;
        form.campus_id = mUser.positionInfo.campusId;
        //----

        boolean check = true;
        String str = "";
        if (form.student_id == 0) {
            str += getString(R.string.check_student);
            check = false;
        }

        if (form.status == 0) {
            str += "\n" + getString(R.string.check_status);
            check = false;
        }
        if (form.belief == 0) {
            str += "\n" + getString(R.string.check_belief);
            check = false;
        }
        if (form.refuse == 0) {
            str += "\n" + getString(R.string.check_refuse);
            check = false;
        }
        if (form.status == 0) {
            str += "\n" + getString(R.string.check_student);
            check = false;
        }

        if (TextUtils.isEmpty(form.tuition_source)) {
            str += "\n" + getString(R.string.check_tuition_source);
            check = false;
        }
        if (form.communication_date == 0) {
            str += "\n" + getString(R.string.check_communication_date);
            check = false;
        }

        if (TextUtils.isEmpty(form.content)) {
            str += "\n" + getString(R.string.check_content);
            check = false;
        }
        if (!check) {
            showToast(str);
        }

        return check;
    }

    @OnClick({R.id.commun_student_name, R.id.commun_state, R.id.commun_faith, R.id.commun_oppose, R.id.commun_back_date, R.id.commun_date})
    void onClickItem(View view) {
        switch (view.getId()) {
            case R.id.commun_student_name:
                JumpManager.jumpActivityForResult(this, PickStudentActivity.class, REQUEST_PICK_STUDENT);
                break;
            case R.id.commun_state:
                final ArrayList<String> datas = AppUtil.getStateList();
                showItemDialog(datas, new SelectDialogFragment.OnItemSelectedListner() {
                    @Override
                    public void onItemSelected(int position, String txt) {
                        stateTv.setText(datas.get(position));
                        form.status = position + 1;
                    }
                });
                break;
            case R.id.commun_faith:
                final ArrayList<String> faiths = AppUtil.getBeliefList();
                showItemDialog(faiths, new SelectDialogFragment.OnItemSelectedListner() {

                    @Override
                    public void onItemSelected(int position, String txt) {
                        faithTv.setText(faiths.get(position));
                        form.belief = position + 1;
                    }
                });
                break;
            case R.id.commun_oppose:
                final ArrayList<String> opposes = AppUtil.getRefuseList();
                showItemDialog(opposes, new SelectDialogFragment.OnItemSelectedListner() {

                    @Override
                    public void onItemSelected(int position, String txt) {
                        opposeTv.setText(opposes.get(position));
                        form.refuse = position + 1;
                    }
                });
                break;
            case R.id.commun_back_date:
                ShowTimePickerDialog(backDateTv, new PickerWheelViewPop.PickCallback<Integer>() {
                    @Override
                    public void onPickCallback(Integer id, String... result) {
                        form.callback_date = id;
                        backDateTv.setText(result[0]);
                    }
                });
                break;
            case R.id.commun_date:
                ShowTimePickerDialog(dateTv, new PickerWheelViewPop.PickCallback<Integer>() {
                    @Override
                    public void onPickCallback(Integer id, String... result) {
                        form.communication_date = id;
                        dateTv.setText(result[0]);
                    }
                });
                break;
        }
    }

    private void ShowTimePickerDialog(View parent, PickerWheelViewPop.PickCallback callback) {

        PickerWheelViewPop pop = new PickerWheelViewPop(this);
        pop.renderYMDPanel(R.string.choose_birthday);
        pop.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        pop.addPickCallback(callback);
    }

    private void showItemDialog(ArrayList<String> stateList, SelectDialogFragment.OnItemSelectedListner onItemselectListner) {
        SelectDialogFragment.Builder builder = new SelectDialogFragment.Builder();
        SelectDialogFragment dialog = builder.setMessage(stateList).setOnItemselectListner(onItemselectListner)
                .Build();
        dialog.show(getSupportFragmentManager());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_STUDENT:
                    if (data != null) {
                        studentInfo = data.getParcelableExtra(PickStudentActivity.STUDENT);
                        updateStudentNameView();
                        form.student_id = studentInfo.student_id;
                    }
                    break;
            }
        }
    }

    private void updateStudentNameView() {
        studentNameTv.setText(studentInfo.name);
    }

    private void updateStudentView() {
        if (studentInfo != null) {
            form.student_id = studentInfo.student_id;
            updateStudentNameView();
        }
    }

    public static class CommunicationForm {
        int student_id;//	Int	1			学生ID	0
        int status;//	Int	1			学生状态： array( 1 => '晨读', 2 => '公开课', 3 => '报名中', 4 => '升学（中）', 5 => '升学（高）', 6 => '升学（影）', 7 => '其它' )	0
        int type;//	Int	1		1	沟通记录类型1是市场，2 是业务	0
        String content;//	String	1			沟通内容	0
        String result;//	String	0			沟通结果	0
        int refuse;//	Int	1			抗拒点：1 => '钱', 2 => '时间', 3 => '距离', 4 => '英语重要性', 5 => '学习方法', 6 => '自学', 7 => '父母不同意', 8 => '其它', 9 => '无'	0
        int belief;//	Int	1			学习信念 1：低，2：中，3：高	0
        String tuition_source;//	String	0			学费来源	0
        long communication_date;//	Int	1			沟通日期	0
        long callback_date;//	Int	0			回访时间	0
        int campus_id;//	Int	1			校区ID	0
        int resources_id;

    }
}
