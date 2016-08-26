package com.ishow.ischool.business.addstudent;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import com.commonlib.util.KeyBoardUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Cons;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.bean.university.UniversityInfo;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.business.universitypick.UniversityPickActivity;
import com.ishow.ischool.common.api.MarketApi;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.rxbus.RxBus;
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
    @BindView(R.id.item_weixin)
    InputLinearLayout weixinIL;
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
//    @BindView(R.id.item_remark)
//    InputLinearLayout remarkIL;

    private MenuItem submitMenu;
    private String nameStr, mobileStr, qqStr, weixinStr, universityStr, majorStr, campusStr, fromStr, referrerStr;
    private int province_id, city_id, campus_id, university_id, source_id;
    private UniversityInfo mUniversityInfo;


    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_addstudent, R.string.add_student, R.menu.menu_addstudent, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        init();
        nameIL.getEdittext().setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
        majorIL.getEdittext().setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
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
        weixinIL.getEdittext().addTextChangedListener(this);
        universityIL.getEdittext().addTextChangedListener(this);
        majorIL.getEdittext().addTextChangedListener(this);
        campusIL.getEdittext().addTextChangedListener(this);
        fromIL.getEdittext().addTextChangedListener(this);
        referrerIL.getEdittext().addTextChangedListener(this);

        // 来源方式权限限定
        int curPositionId = mUser.positionInfo.id;
        if (curPositionId == Cons.Position.Chendujiangshi.ordinal()) {
            fromIL.setDisable();
            fromIL.setContent("晨读");
            source_id = MarketApi.TYPESOURCE_READING;
        } else if (curPositionId == Cons.Position.Xiaoliaozhuanyuan.ordinal()) {
            fromIL.setDisable();
            fromIL.setContent("校聊");
            source_id = MarketApi.TYPESOURCE_CHAT;
        } else {
            fromIL.setDisable();
            fromIL.setContent("转介绍");
            source_id = MarketApi.TYPESOURCE_RECOMMEND;
            referrerIL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submit:
                KeyBoardUtil.closeKeybord(nameIL.getEdittext(), AddStudentActivity.this);
                mPresenter.addStudent(nameStr, mobileStr, qqStr, weixinStr, province_id, city_id, campus_id, university_id, majorStr, source_id);
                break;
        }
        return true;
    }

    void checkComplete() {
        nameStr = nameIL.getContent();
        mobileStr = phoneIL.getContent();
        qqStr = qqIL.getContent();
        weixinStr = weixinIL.getContent();
        universityStr = universityIL.getContent();
        majorStr = majorIL.getContent();
        campusStr = campusIL.getContent();
        fromStr = fromIL.getContent();
        referrerStr = referrerIL.getContent();
        if (!TextUtils.isEmpty(nameStr) && !TextUtils.isEmpty(mobileStr) && (!TextUtils.isEmpty(qqStr) || !TextUtils.isEmpty(weixinStr))
                && !TextUtils.isEmpty(universityStr) && !TextUtils.isEmpty(majorStr) && !TextUtils.isEmpty(campusStr) && !TextUtils.isEmpty(fromStr)) {
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

    boolean checkEmpty() {
        nameStr = nameIL.getContent();
        mobileStr = phoneIL.getContent();
        qqStr = qqIL.getContent();
        weixinStr = weixinIL.getContent();
        universityStr = universityIL.getContent();
        majorStr = majorIL.getContent();
        if (TextUtils.isEmpty(nameStr) && TextUtils.isEmpty(mobileStr) && TextUtils.isEmpty(qqStr)
                && TextUtils.isEmpty(weixinStr) && TextUtils.isEmpty(universityStr) && TextUtils.isEmpty(majorStr)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onEdittextClick(View edittext) {
        switch (edittext.getId()) {
            case R.id.item_university:
                startActivityForResult(new Intent(AddStudentActivity.this, UniversityPickActivity.class), UniversityPickActivity.REQUEST_CODE_PICK_UNIVERSITY);
                break;
            case R.id.item_from:
//                final String[] sources;
//                if (mUser.positionInfo.id == Cons.Position.Xiaoyuanjingli.ordinal() ||
//                        mUser.positionInfo.id == Cons.Position.Shichangzhuguan.ordinal()) {
//                    sources = new String[]{"晨读", "转介绍"};
//                } else if (mUser.positionInfo.id == Cons.Position.Xiaoliaozhuguan.ordinal()) {
//                    sources = new String[]{"校聊", "转介绍"};
//                } else {
//                    sources = getResources().getStringArray(R.array.source_strings);
//                }
//                ActionSheet.createBuilder(this, this.getSupportFragmentManager())
//                        .setCancelButtonTitle(R.string.str_cancel)
//                        .setOtherButtonTitles(sources)
//                        .setCancelableOnTouchOutside(true)
//                        .setListener(new ActionSheet.ActionSheetListener() {
//                            @Override
//                            public void onDismiss(ActionSheet actionSheet, boolean b) {
//
//                            }
//
//                            @Override
//                            public void onOtherButtonClick(ActionSheet actionSheet, int i) {
//                                fromIL.setContent(sources[i]);
//                                setSourceId(sources[i]);
//                            }
//                        }).show();
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
        showToast("添加成功");
        RxBus.getDefault().post(studentInfo);
        finish();
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

    @Override
    public void onBackPressed() {
        if (!checkEmpty()) {
            exitConfirm();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNavigationBtnClicked() {
        if (!checkEmpty()) {
            exitConfirm();
        } else {
            super.onNavigationBtnClicked();
        }
    }

    void exitConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //        builder.setTitle("Material Design Dialog");
        builder.setMessage("返回后本次输入信息会被清空，\n确定返回？");
        builder.setNegativeButton(R.string.str_cancel, null);
        builder.setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }
}
