package com.ishow.ischool.business.forgetpwd;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.ishow.ischool.R;
import com.ishow.ischool.application.Resourse;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by MrS on 2016/8/12.
 */
public class ForgetPwdActivity1 extends BaseActivity4Crm<ForgetPresenter, ForgetPwdModel> implements TextWatcher, ForgetPwdView {

    @BindView(R.id.getcode_phone)
    EditText getcodePhone;
    @BindView(R.id.getcode_smscode)
    EditText getcodeSmscode;
    @BindView(R.id.submit_tv)
    Button submitTv;
    @BindView(R.id.getcode_btn)
    Button getcodeBtn;

    private int call = 0;
    private Subscription subscribe;
    private boolean btnCodeClick;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_editpwd_getcode, R.string.eidt_pwd_title_getcode, -1, 0);
    }

    @Override
    protected void setUpView() {
        setBtnEnable(submitTv, false);
        setBtnEnable(getcodeBtn, false);
        getcodePhone.addTextChangedListener(this);
        getcodeSmscode.addTextChangedListener(this);
    }

    @Override
    protected void setUpData() {

    }

    @OnClick(R.id.submit_tv)
    public void onSubmit() {
        btnCodeClick = true;
        hideSoftPanel(submitTv);
        handProgressbar(true);
        mPresenter.checkRandCode(getcodePhone.getText().toString(), getcodeSmscode.getText().toString());
    }

    @OnClick(R.id.getcode_btn)
    public void onGetCode() {
        btnCodeClick = false;
        hideSoftPanel(submitTv);
        handProgressbar(true);
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
            setBtnEnable(submitTv, true);
        } else setBtnEnable(submitTv, false);

        //因为 call是用来计时的  当他为了0的时候 说明 没有在线程当中。
        if (!isEmpty(getcodePhone)&&call==0) {
            setBtnEnable(getcodeBtn, true);
        } else setBtnEnable(getcodeBtn, false);
    }

    private void setBtnEnable(Button btn, boolean b) {
        btn.setEnabled(b);
        btn.setClickable(b);
        btn.setAlpha(b ? 1.0f : 0.5f);
    }

    @Override
    public void onNetSucess(int msg) {
        handProgressbar(false);
        if (msg!=-1)showToast(msg);
        if (!btnCodeClick) {

            setBtnEnable(getcodeBtn, false);
            subscribe = Observable.interval(0, 1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            call++;
                            getcodeBtn.setText(String.format(getString(R.string.time_left_to_try_again), 60 - call));
                            if (call >= 60) {
                                subscribe.unsubscribe();
                                getcodeBtn.setText(getString(R.string.get_sms_code));
                                btnCodeClick = false;
                                call = 0;
                                setBtnEnable(getcodeBtn, true);
                            }
                        }
                    });
        } else {
            Intent intent = new Intent(this,ForgetPwdActivity2.class);
            intent.putExtra("mobile",getcodePhone.getText().toString());
            JumpManager.jumpActivity(this, intent, Resourse.NO_NEED_CHECK);
        }
    }

    @Override
    public void onNetfaield(String msg) {
        handProgressbar(false);
        showToast(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null && !subscribe.isUnsubscribed())
            subscribe.unsubscribe();
    }
}
