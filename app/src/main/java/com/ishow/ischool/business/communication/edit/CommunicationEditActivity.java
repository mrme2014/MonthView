package com.ishow.ischool.business.communication.edit;

import android.content.Intent;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.commonlib.core.BaseView;
import com.commonlib.util.KeyBoardUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Cons;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.util.AppUtil;

import java.util.HashMap;

import butterknife.BindView;

public class CommunicationEditActivity extends BaseActivity4Crm<CommunEditPresenter, CommunEditModel> implements BaseView {


    public static final String P_TITLE = "title";
    public static final String P_TYPE = "type";
    public static final String P_TEXT = "text";
    public static final String P_ID = "id";
    public static final String P_LEN = "len";
    private String mTitle;
    private int mType;

    @BindView(R.id.edit_text)
    EditText mEditText;

    @BindView(R.id.edit_hint)
    TextView mEditHint;
    private String mText;
    private int mId;
    private int mLen;

    @Override
    protected void initEnv() {
        super.initEnv();
        mTitle = getIntent().getStringExtra(P_TITLE);
        mType = getIntent().getIntExtra(P_TYPE, 0);
        mText = getIntent().getStringExtra(P_TEXT);
        mId = getIntent().getIntExtra(P_ID, 0);
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
            Selection.setSelection(mEditText.getText(), mText.length());
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
        HashMap<String, String> params = AppUtil.getParamsHashMap(Resource.SHARE_COMMUNICATION_EDITM);
        params.put("id", mId + "");
        switch (mType) {
            case Cons.Communication.source: {
                params.put("tuition_source", text);
                break;
            }
        }
        mPresenter.editCommunication(params, text);
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
