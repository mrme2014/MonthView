package com.ishow.ischool.business.student.edit;

import android.content.Intent;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.commonlib.core.BaseView;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resourse;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.util.AppUtil;

import java.util.HashMap;

import butterknife.BindView;

public class EditActivity extends BaseActivity4Crm<EditPresenter, EditModel> implements BaseView {

    public static final String P_TITLE = "title";
    public static final String P_TYPE = "type";
    public static final String P_TEXT = "text";
    public static final String P_STUDENT_ID = "student_id";
    private String mTitle;
    private int mType;

    @BindView(R.id.edit_text)
    EditText mEditText;

    @BindView(R.id.edit_hint)
    TextView mEditHint;
    private String mText;
    private int mStudentId;

    @Override
    protected void initEnv() {
        super.initEnv();
        mTitle = getIntent().getStringExtra(P_TITLE);
        mType = getIntent().getIntExtra(P_TYPE, 0);
        mText = getIntent().getStringExtra(P_TEXT);
        mStudentId = getIntent().getIntExtra(P_STUDENT_ID, 0);
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
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String text = mEditText.getText().toString();
        HashMap<String, String> params = AppUtil.getParamsHashMap(Resourse.STUDENT_EDIT);
        params.put("id", mStudentId + "");
        switch (mType) {
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
//            case R.id.student_school: {
//                params.put("birthday", text);
//            }
//            break;
            case R.id.student_specialty: {
                params.put("major", text);
                break;
            }
            case R.id.student_class: {
                params.put("grade", text);
                break;
            }
            case R.id.student_idcard: {
                params.put("idcard", text);
                break;
            }
        }
        mPresenter.editStudent(params, text);
        return super.onMenuItemClick(item);
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
