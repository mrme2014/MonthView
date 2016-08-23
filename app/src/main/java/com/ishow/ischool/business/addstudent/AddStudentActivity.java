package com.ishow.ischool.business.addstudent;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import com.baoyz.actionsheet.ActionSheet;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.bean.university.UniversityInfo;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.business.universitypick.UniversityPickActivity;
import com.ishow.ischool.common.api.MarketApi;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.widget.custom.InputLinearLayout;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by wqf on 16/8/15.
 */
public class AddStudentActivity extends BaseActivity4Crm<AddStudentPresenter, AddStudentModel> implements AddStudentContract.View,
        InputLinearLayout.EidttextClick, TextWatcher {


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
    @BindView(R.id.item_referrer)
    InputLinearLayout referrerIL;
    @BindView(R.id.item_remark)
    InputLinearLayout remarkIL;

    private MenuItem submitMenu;
    private String nameStr, mobileStr, qqStr, universityStr, majorStr, campusStr, fromStr, referrerStr, notesStr;
    private int province_id, city_id, campus_id, university_id, source_id;
    private UniversityInfo mUniversityInfo;


    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_addstudent, R.string.add_student, R.menu.menu_addstudent, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        init();
        universityIL.setOnEidttextClick(this);
        campusIL.setOnEidttextClick(this);
        fromIL.setOnEidttextClick(this);
        referrerIL.setOnEidttextClick(this);
        referrerIL.setContent(mUser.userInfo.user_name);
    }

    @Override
    protected void setUpData() {
        mPresenter.getCampus(mUser.userInfo.campus_id);
    }

    private void init() {
        submitMenu = mToolbar.getMenu().findItem(R.id.submit);
        checkComplete();
        nameIL.getEdittext().addTextChangedListener(this);
        phoneIL.getEdittext().addTextChangedListener(this);
        qqIL.getEdittext().addTextChangedListener(this);
        universityIL.getEdittext().addTextChangedListener(this);
        majorIL.getEdittext().addTextChangedListener(this);
        campusIL.getEdittext().addTextChangedListener(this);
        fromIL.getEdittext().addTextChangedListener(this);
        referrerIL.getEdittext().addTextChangedListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submit:
                mPresenter.addStudent(nameStr, mobileStr, qqStr, province_id, city_id, campus_id, university_id, majorStr, source_id, notesStr);
                break;
        }
        return true;
    }

    void checkComplete() {
        nameStr = nameIL.getContent();
        mobileStr = phoneIL.getContent();
        qqStr = qqIL.getContent();
        universityStr = universityIL.getContent();
        majorStr = majorIL.getContent();
        campusStr = campusIL.getContent();
        fromStr = fromIL.getContent();
        referrerStr = referrerIL.getContent();
        notesStr = remarkIL.getContent();
        if (!TextUtils.isEmpty(nameStr) && !TextUtils.isEmpty(mobileStr) && !TextUtils.isEmpty(qqStr) && !TextUtils.isEmpty(universityStr)
                && !TextUtils.isEmpty(majorStr) && !TextUtils.isEmpty(campusStr) && !TextUtils.isEmpty(fromStr)) {
            if (referrerIL.getVisibility() == View.VISIBLE) {    // 转介绍时，推荐人必填
                if (!TextUtils.isEmpty(referrerStr)) {
                    submitMenu.setEnabled(true);
                } else {
                    submitMenu.setEnabled(false);
                }
            } else {
                submitMenu.setEnabled(true);
            }
        } else {
            submitMenu.setEnabled(false);
        }
    }

    @Override
    public void onEdittextClick(View edittext) {
        switch (edittext.getId()) {
            case R.id.item_university:
                startActivityForResult(new Intent(AddStudentActivity.this, UniversityPickActivity.class), UniversityPickActivity.REQUEST_CODE_PICK_UNIVERSITY);
                break;
            case R.id.item_from:
                ActionSheet.createBuilder(this, this.getSupportFragmentManager())
                        .setCancelButtonTitle(R.string.str_cancel)
                        .setOtherButtonTitles(getResources().getStringArray(R.array.source_strings))
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean b) {

                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int i) {
                                switch (i) {
                                    case 0:
                                        fromIL.setContent("校聊");
                                        source_id = MarketApi.TYPESOURCE3;
                                        if (referrerIL.getVisibility() != View.GONE) {
                                            referrerIL.setVisibility(View.GONE);
//                                            referrerIL.setContent("");
                                        }
                                        break;
                                    case 1:
                                        fromIL.setContent("晨读");
                                        source_id = MarketApi.TYPESOURCE1;
                                        if (referrerIL.getVisibility() != View.GONE) {
                                            referrerIL.setVisibility(View.GONE);
//                                            referrerIL.setContent("");
                                        }
                                        break;
                                    case 2:
                                        fromIL.setContent("转介绍");
                                        source_id = MarketApi.TYPESOURCE2;
                                        if (referrerIL.getVisibility() != View.VISIBLE) {
                                            referrerIL.setVisibility(View.VISIBLE);
                                        }
                                        break;
                                }
                            }
                        }).show();
                break;
            case R.id.item_referrer:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UniversityPickActivity.REQUEST_CODE_PICK_UNIVERSITY:
                    mUniversityInfo = data.getParcelableExtra(UniversityPickActivity.KEY_PICKED_UNIVERSITY);
                    universityIL.setContent(mUniversityInfo.name);
                    university_id = mUniversityInfo.id;
                    province_id = mUniversityInfo.prov_id;
                    city_id = mUniversityInfo.city_id;
                    break;
            }
        }
    }

    @Override
    public void getCampusSuccess(ArrayList<Campus> campus) {
        campusIL.setContent(campus.get(0).name);
        campus_id = campus.get(0).id;
    }

    @Override
    public void getCampusFail(String msg) {
        campus_id = mUser.userInfo.campus_id;
    }

    @Override
    public void addStudentSuccess(StudentInfo studentInfo) {
        finish();
        showToast("添加成功");
    }

    @Override
    public void addStudentFail(String msg) {
        showToast(msg);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        checkComplete();
    }
}
