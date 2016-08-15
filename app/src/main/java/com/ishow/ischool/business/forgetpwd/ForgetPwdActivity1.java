package com.ishow.ischool.business.forgetpwd;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.BaseActivity4Crm;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by MrS on 2016/8/12.
 */
public class ForgetPwdActivity1 extends BaseActivity4Crm<ForgetPresenter,ForgetPwdModel> implements TextWatcher {

    @BindView(R.id.getcode_phone)
    EditText getcodePhone;
    @BindView(R.id.getcode_smscode)
    EditText getcodeSmscode;
    @BindView(R.id.submit_tv)
    Button submitTv;
    @BindView(R.id.getcode_btn)
    Button getcodeBtn;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_editpwd_getcode, R.string.eidt_pwd_title_getcode, -1, 0);
    }

    @Override
    protected void setUpView() {
        setBtnEnable(submitTv,false);
        setBtnEnable(getcodeBtn,false);
        getcodePhone.addTextChangedListener(this);
        getcodeSmscode.addTextChangedListener(this);
    }

    @Override
    protected void setUpData() {

    }

    @OnClick(R.id.submit_tv)
    public void onSubmit() {

    }

    @OnClick(R.id.getcode_btn)
    public void onGetCode() {
        mPresenter.sendSms(getcodePhone.getText().toString());

    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    private boolean isEmpty(EditText editext) {
        return TextUtils.equals(editext.getText().toString().trim(), "");
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!isEmpty(getcodePhone) && !isEmpty(getcodeSmscode)) {
            setBtnEnable(submitTv,true);
        } else setBtnEnable(submitTv,false);

        if (!isEmpty(getcodePhone)){
            setBtnEnable(getcodeBtn,true);
        }else setBtnEnable(getcodeBtn,false);
    }

    private void setBtnEnable(Button btn,boolean b) {
        btn.setEnabled(b);
        btn.setClickable(b);
        btn.setAlpha(b ? 1.0f : 0.5f);
    }
}
