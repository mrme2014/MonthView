package com.ishow.ischool.business.addstudent;

import android.view.View;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.widget.custom.InputLinearLayout;

import butterknife.BindView;

/**
 * Created by wqf on 16/8/15.
 */
public class AddStudentActivity extends BaseActivity4Crm<AddStudentPresenter, AddStudentModel> implements AddStudentContract.View, InputLinearLayout.EidttextClick {


    @BindView(R.id.item_name)
    InputLinearLayout name;
    @BindView(R.id.item_phone)
    InputLinearLayout phone;
    @BindView(R.id.item_qq)
    InputLinearLayout qq;
    @BindView(R.id.item_college)
    InputLinearLayout college;
    @BindView(R.id.item_major)
    InputLinearLayout major;
    @BindView(R.id.item_campus)
    InputLinearLayout campus;
    @BindView(R.id.item_from)
    InputLinearLayout from;
    @BindView(R.id.item_reference)
    InputLinearLayout reference;
    @BindView(R.id.item_remark)
    InputLinearLayout remark;


    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_addstudent, R.string.add_student, R.menu.menu_addstudent, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        college.setOnEidttextClick(this);
        campus.setOnEidttextClick(this);
        from.setOnEidttextClick(this);
        reference.setOnEidttextClick(this);
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void onEdittextClick(View edittext) {
        switch (edittext.getId()) {
            case R.id.item_college:
                showToast("就读学校");
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
}
