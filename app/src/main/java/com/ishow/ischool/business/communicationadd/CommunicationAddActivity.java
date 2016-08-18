package com.ishow.ischool.business.communicationadd;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import com.commonlib.widget.LabelEditText;
import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.business.student.pick.PickStudentActivity;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.SelectDialogFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class CommunicationAddActivity extends BaseActivity4Crm<CommunicationAddPresenter, CommunicationAddModel> implements CommunicationAddContract.View {

    private static final int REQUEST_PICK_STUDENT = 100;
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

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_communication_add, R.string.add_communication, R.menu.menu_communication_add, MODE_BACK);
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void onAddSuccess() {

    }

    @Override
    public void onAddFailed() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        return super.onMenuItemClick(item);
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
                    public void onItemSelected(int position) {
                        stateTv.setText(datas.get(position));
                    }
                });
                break;
            case R.id.commun_faith:
                final ArrayList<String> faiths = AppUtil.getBeliefList();
                showItemDialog(faiths, new SelectDialogFragment.OnItemSelectedListner() {

                    @Override
                    public void onItemSelected(int position) {
                        faithTv.setText(faiths.get(position));
                    }
                });
                break;
            case R.id.commun_oppose:
                final ArrayList<String> opposes = AppUtil.getRefuseList();
                showItemDialog(opposes, new SelectDialogFragment.OnItemSelectedListner() {

                    @Override
                    public void onItemSelected(int position) {
                        opposeTv.setText(opposes.get(position));
                    }
                });
                break;
            case R.id.commun_back_date:
                break;
            case R.id.commun_date:
                break;
        }
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
                    }
                    break;
            }
        }
    }

    private void updateStudentNameView() {
        studentNameTv.setText(studentInfo.name);
    }
}
