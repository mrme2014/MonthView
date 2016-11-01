package com.ishow.ischool.business.student.edit;

import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.commonlib.core.BaseView;
import com.commonlib.util.KeyBoardUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.util.AppUtil;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

public class EditActivity extends BaseActivity4Crm<EditPresenter, EditModel> implements BaseView {

    public static final String P_TITLE = "title";
    public static final String P_TYPE = "type";
    public static final String P_TEXT = "text";
    public static final String P_STUDENT_ID = "student_id";
    public static final String P_LEN = "len";
    private String mTitle;
    private int mType;

    @BindView(R.id.edit_text)
    EditText mEditText;

    @BindView(R.id.edit_hint)
    TextView mEditHint;
    private String mText;
    private int mStudentId;
    private int mLen;

    @Override
    protected void initEnv() {
        super.initEnv();
        mTitle = getIntent().getStringExtra(P_TITLE);
        mType = getIntent().getIntExtra(P_TYPE, 0);
        mStudentId = getIntent().getIntExtra(P_STUDENT_ID, 0);
        mText = getIntent().getStringExtra(P_TEXT);
        mLen = getIntent().getIntExtra(P_LEN, 0);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_edit, mTitle, R.menu.menu_edit, MODE_BACK);
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {
        if (!TextUtils.isEmpty(mText)) {
            mEditText.setText(mText);
            Editable etext = mEditText.getText();
            Selection.setSelection(etext, etext.length());
        }
        if (mLen != 0) {
            InputFilter[] filters = {new InputFilter.LengthFilter(mLen)};
            mEditText.setFilters(filters);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        KeyBoardUtil.closeKeybord(mEditText, this);
        String text = mEditText.getText().toString();
        HashMap<String, String> params = AppUtil.getParamsHashMap(Resource.MARKET_STUDENT_EDIT);
        params.put("id", mStudentId + "");
        switch (mType) {

            case R.id.student_user_name: {
                params.put("name", text);
                break;
            }

            case R.id.student_english_name: {
                params.put("english_name", text);
                break;
            }
            case R.id.student_phone: {
                params.put("mobile", text);
                break;
            }
            case R.id.student_qq: {
                params.put("qq", text);
                break;
            }
            case R.id.student_birthday: {
                params.put("birthday", text);
                break;
            }
            case R.id.student_wechat: {
                params.put("wechat", text);
            }
            break;
            case R.id.student_specialty: {
                params.put("major", text);
                break;
            }
            case R.id.student_class: {
                params.put("grade", text);
                break;
            }
            case R.id.student_idcard: {
                if (!checkIdcard(text)) {
                    showToast(R.string.msg_idcard_input);
                    return false;
                }
                params.put("idcard", text);
                break;
            }
            case R.id.student_parent_phone_number: {
                if (!checkPhoneNumber(text)) {
                    showToast(R.string.msg_mobile_input);
                    return false;
                }
                params.put("parents_call", text);
                break;
            }
        }
        mPresenter.editStudent(params, text);
        return super.onMenuItemClick(item);
    }

    private boolean checkPhoneNumber(String text) {
        Pattern pattern = Pattern.compile("^(\\d{3,4}-\\d{8})|(\\d{13})$");
        Matcher matcher = pattern.matcher(text);
        boolean b = matcher.matches();
        return b;
    }

    private boolean checkIdcard(String text) {
        Pattern pattern = Pattern.compile("[0-9a-zA-Z]{16,18}");
        Matcher matcher = pattern.matcher(text);
        boolean b = matcher.matches();
        return b;
    }

    public void onEditSuccess(String text) {
        Intent intent = new Intent();
        intent.putExtra("data", text);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onEditFailed(String msg) {
        showToast(msg);
    }
}
