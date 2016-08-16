package com.ishow.ischool.business.addstudent;

import android.content.Intent;
import android.view.View;

import com.ishow.ischool.R;
import com.ishow.ischool.business.universitypick.UniversityPickActivity;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.widget.custom.InputLinearLayout;

import butterknife.BindView;

/**
 * Created by wqf on 16/8/15.
 */
public class AddStudentActivity extends BaseActivity4Crm<AddStudentPresenter, AddStudentModel> implements AddStudentContract.View, InputLinearLayout.EidttextClick {


    @BindView(R.id.item_name)
    InputLinearLayout nameIL;
    @BindView(R.id.item_phone)
    InputLinearLayout phoneIL;
    @BindView(R.id.item_qq)
    InputLinearLayout qqIL;
    @BindView(R.id.item_university)
    InputLinearLayout universityIL;
    @BindView(R.id.item_major)
    InputLinearLayout majorIL;
    @BindView(R.id.item_campus)
    InputLinearLayout campusIL;
    @BindView(R.id.item_from)
    InputLinearLayout fromIL;
    @BindView(R.id.item_reference)
    InputLinearLayout referenceIL;
    @BindView(R.id.item_remark)
    InputLinearLayout remarkIL;
    private String mUniversity = "";


    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_addstudent, R.string.add_student, R.menu.menu_addstudent, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        universityIL.setOnEidttextClick(this);
        campusIL.setOnEidttextClick(this);
        fromIL.setOnEidttextClick(this);
        referenceIL.setOnEidttextClick(this);
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void onEdittextClick(View edittext) {
        switch (edittext.getId()) {
            case R.id.item_university:
                startActivityForResult(new Intent(AddStudentActivity.this, UniversityPickActivity.class), UniversityPickActivity.REQUEST_CODE_PICK_UNIVERSITY);
                break;
            case R.id.item_campus:
                showToast("所属校区");
                break;
            case R.id.item_from:
                showToast("来源");
                break;
            case R.id.item_reference:
                showToast("推荐人");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UniversityPickActivity.REQUEST_CODE_PICK_UNIVERSITY:
                    String university = data.getStringExtra(UniversityPickActivity.KEY_PICKED_UNIVERSITY);
                    if ((!mUniversity.equals(university))) {
                        mUniversity = university;
                        universityIL.setContent(mUniversity);
                    }
                    break;
            }
        }
    }
}
