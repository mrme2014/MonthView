package com.ishow.ischool.business.communication.add;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.util.KeyBoardUtil;
import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Constants;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.market.Communication;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.business.student.pick4teach.PickStudentActivity;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.common.rxbus.RxBus;
import com.ishow.ischool.event.CommunicationAddRefreshEvent;
import com.ishow.ischool.fragment.SelectDialogFragment;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class CommunicationAddActivity extends BaseActivity4Crm<CommunicationAddPresenter, CommunicationAddModel> implements CommunicationAddContract.View {

    private static final int REQUEST_PICK_STUDENT = 100;
    public static final String P_STUDENT_INFO = "student_info";
    public static final String P_COMMUNICATION_OLD = "communication";
    private static final String P_IS_TEACH = "is_teach";

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
    LabelTextView moneySourceTv;

    @BindView(R.id.commun_content)
    EditText contentTv;

    @BindView(R.id.commun_content_count)
    TextView contentCountTv;


    private StudentInfo studentInfo;
    private CommunicationForm form = new CommunicationForm();

    private boolean isSubmitting;
    private int max_length = 600;
    private Communication oldCommunication;

    private int from;

    @Override
    protected void initEnv() {
        super.initEnv();
        studentInfo = getIntent().getParcelableExtra(P_STUDENT_INFO);
        oldCommunication = getIntent().getParcelableExtra(P_COMMUNICATION_OLD);
        from = getIntent().getIntExtra(Constants.FROM_M_E, 0);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_communication_add, R.string.add_communication, R.menu.menu_communication_add, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        updateDateView();
        updateStudentView();
        updateOldView();


        contentTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= max_length + 1) {
                    contentCountTv.setText(0 + "");
                    String s = editable.toString();
                    contentTv.setText(s.substring(0, max_length));
                    contentTv.setSelection(max_length);
                } else {
                    contentCountTv.setText((max_length - editable.length()) + "");
                    contentTv.setSelection(editable.length());
                }
                //contentCountTv.setText(String.valueOf(200 - editable.length()));
            }
        });
    }

    private void updateDateView() {
        form.communication_date = new Date().getTime() / 1000;
        dateTv.setText(DateUtil.parseSecond2Str(form.communication_date));
        contentCountTv.setText(max_length + "");
    }

    private void updateOldView() {
        if (oldCommunication != null) {
            stateTv.setText(AppUtil.getStateById(oldCommunication.communicationInfo.status));
            faithTv.setText(AppUtil.getBeliefById(oldCommunication.communicationInfo.belief));
            opposeTv.setText(AppUtil.getRefuseById(oldCommunication.communicationInfo.refuse));
            if (oldCommunication.communicationInfo.callback_date > 0) {
                backDateTv.setText(DateUtil.parseSecond2Str(oldCommunication.communicationInfo.callback_date));
            }
            moneySourceTv.setText(oldCommunication.communicationInfo.tuition_source);

            form.status = oldCommunication.communicationInfo.status;
            form.belief = oldCommunication.communicationInfo.belief;
            form.refuse = oldCommunication.communicationInfo.refuse;
            form.callback_date = oldCommunication.communicationInfo.callback_date;
            form.tuition_source = oldCommunication.communicationInfo.tuition_source;
        }
    }

    private void finishActivity() {
        if (!checkEmpty()) {
            this.finish();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.ready_finish_activity)).setPositiveButton(getString(R.string.ready_finish_activity_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    CommunicationAddActivity.this.finish();
                }
            }).setNegativeButton(getString(R.string.ready_finish_activity_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private boolean checkEmpty() {
        if (!TextUtils.equals(studentNameTv.getText().toString(), ""))
            return true;
        if (!TextUtils.equals(stateTv.getText().toString(), ""))
            return true;
        if (!TextUtils.equals(faithTv.getText().toString(), ""))
            return true;
        if (!TextUtils.equals(opposeTv.getText().toString(), ""))
            return true;
        if (!TextUtils.equals(backDateTv.getText().toString(), ""))
            return true;
//        if (!TextUtils.equals(dateTv.getText().toString(), ""))
//            return true;
        if (!TextUtils.equals(moneySourceTv.getText().toString(), ""))
            return true;
        if (!TextUtils.equals(contentTv.getText().toString(), ""))
            return true;
        return false;
    }

    @Override
    protected void onNavigationBtnClicked() {
        finishActivity();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finishActivity();
    }

    @Override
    protected void setUpData() {
        form.resources_id = Resource.SHARE_COMMUNICATION_ADDM;
    }

    @Override
    public void onAddSuccess() {
        handProgressbar(false);
        isSubmitting = false;
        showToast(R.string.add_success);
        RxBus.getDefault().post(new CommunicationAddRefreshEvent());
        finish();
    }

    @Override
    public void onAddFailed(String msg) {
        handProgressbar(false);
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
        KeyBoardUtil.closeKeybord(contentTv, this);
        if (checkForm()) {
            isSubmitting = true;
            mPresenter.addCommunication(form);
            handProgressbar(true);
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

        if (form.communication_date == 0) {
            str += "\n" + getString(R.string.check_communication_date);
            check = false;
        }

        if (TextUtils.isEmpty(form.content)) {
            str += "\n" + getString(R.string.check_content);
            check = false;
        }
        if (!check) {
            showToast(R.string.add_communication_check_failed);
        }

        return check;
    }

    @OnClick({R.id.commun_student_name, R.id.commun_state, R.id.commun_faith, R.id.commun_oppose, R.id.commun_back_date, R.id.commun_date, R.id.commun_source})
    void onClickItem(View view) {
        switch (view.getId()) {
            case R.id.commun_student_name:
                Intent intent;
                if (from == Constants.FROM_TEACH) {
                    intent = new Intent(this, PickStudentActivity.class);
                } else {
                    intent = new Intent(this, com.ishow.ischool.business.student.pick.PickStudentActivity.class);
                }
                JumpManager.jumpActivityForResult(this, intent, REQUEST_PICK_STUDENT, Resource.NO_NEED_CHECK);
                break;
            case R.id.commun_state:
                final ArrayList<String> datas = AppUtil.getStateList();
                AppUtil.showItemDialog(getSupportFragmentManager(), datas, new SelectDialogFragment.OnItemSelectedListner() {
                    @Override
                    public void onItemSelected(int position, String txt) {
                        stateTv.setText(datas.get(position));
                        form.status = position + 1;
                    }
                });
                break;
            case R.id.commun_faith:
                final ArrayList<String> faiths = AppUtil.getBeliefList();
                AppUtil.showItemDialog(getSupportFragmentManager(), faiths, new SelectDialogFragment.OnItemSelectedListner() {

                    @Override
                    public void onItemSelected(int position, String txt) {
                        faithTv.setText(faiths.get(position));
                        form.belief = position + 1;
                    }
                });
                break;
            case R.id.commun_oppose:
                final ArrayList<String> opposes = AppUtil.getRefuseList();
                AppUtil.showItemDialog(getSupportFragmentManager(), opposes, new SelectDialogFragment.OnItemSelectedListner() {

                    @Override
                    public void onItemSelected(int position, String txt) {
                        opposeTv.setText(opposes.get(position));
                        form.refuse = position + 1;
                    }
                });
                break;
            case R.id.commun_back_date:
                AppUtil.showTimePickerDialog(getSupportFragmentManager(), new PickerDialogFragment.Callback<Integer>() {
                    @Override
                    public void onPickResult(Integer unix, String... result) {
                        form.callback_date = unix;
                        backDateTv.setText(result[0]);
                    }
                });
                break;
            case R.id.commun_date:
                AppUtil.showTimePickerDialog(getSupportFragmentManager(), new PickerDialogFragment.Callback<Integer>() {
                    @Override
                    public void onPickResult(Integer unix, String... result) {
                        form.communication_date = unix;
                        dateTv.setText(result[0]);
                    }
                });
                break;
            case R.id.commun_source:
                final ArrayList<String> source = AppUtil.getSourceList();
                AppUtil.showItemDialog(getSupportFragmentManager(), source, new SelectDialogFragment.OnItemSelectedListner() {

                    @Override
                    public void onItemSelected(int position, String txt) {
                        moneySourceTv.setText(source.get(position));
                        form.tuition_source = source.get(position);
                    }
                });
                break;
        }
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
                        form.student_id = studentInfo.id != 0 ? studentInfo.id : studentInfo.student_id;
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
