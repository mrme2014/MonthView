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
public class ForgetPwdActivity2 extends BaseActivity4Crm<ForgetPresenter, ForgetPwdModel> implements TextWatcher {
    @BindView(R.id.new_pwd)
    EditText newPwd;
    @BindView(R.id.new_pwd_again)
    EditText newPwdAgain;
    @BindView(R.id.submit_tv)
    Button submitTv;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_editpwd_newpwd, R.string.eidt_pwd_title_newpwd, -1, 0);
    }

    @Override
    protected void setUpView() {
        setBtnEnable(submitTv,false);
        newPwd.addTextChangedListener(this);
        newPwdAgain.addTextChangedListener(this);
    }

    @Override
    protected void setUpData() {

    }

    private void setBtnEnable(Button btn,boolean b) {
        btn.setEnabled(b);
        btn.setClickable(b);
        btn.setAlpha(b ? 1.0f : 0.5f);
    }

    private boolean isEmpty(EditText editext) {
        return TextUtils.equals(editext.getText().toString().trim(), "");
    }

    @OnClick(R.id.submit_tv)
    public void onClick() {
     //   mPresenter.setPwd();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!isEmpty(newPwd)&&isEmpty(newPwdAgain)){
            setBtnEnable(submitTv,true);
        }else setBtnEnable(submitTv,false);
    }
}
