package com.ishow.ischool.business.editpwd;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.commonlib.application.ActivityStackManager;
import com.commonlib.http.ApiFactory;
import com.google.gson.JsonElement;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.bean.user.UserInfo;
import com.ishow.ischool.business.login.LoginActivity;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.UserApi;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.common.manager.UserManager;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MrS on 2016/8/12.
 */
public class EditPwdActivity extends BaseActivity4Crm implements TextWatcher {
    @BindView(R.id.old_pwd)
    EditText oldPwd;
    @BindView(R.id.new_pwd)
    EditText newPwd;
    @BindView(R.id.new_pwd_again)
    EditText newPwdAgain;
    @BindView(R.id.submit_tv)
    Button submitTv;
    private User user;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_editpwd, R.string.edit_pwd, -1, 0);
    }

    @Override
    protected void setUpView() {
        oldPwd.addTextChangedListener(this);
        newPwd.addTextChangedListener(this);
        newPwdAgain.addTextChangedListener(this);
        setBtnEnable(false);
    }

    private void setBtnEnable(boolean b) {
        submitTv.setEnabled(b);
        submitTv.setClickable(b);
        submitTv.setAlpha(b?1.0f:0.5f);
    }

    @Override
    protected void setUpData() {
         user = UserManager.getInstance().get();
    }


    @OnClick(R.id.submit_tv)
    public void onClick() {
        if (user==null)
            return;
        UserInfo userInfo = user.userInfo;
        if (userInfo==null)
            return;
        if (!TextUtils.equals(newPwd.getText().toString(),newPwdAgain.getText().toString())){
            showToast(R.string.twice_pwd_not_equal);
            return;
        }
        handProgressbar(true);
        hideSoftPanel(submitTv);
        ApiFactory.getInstance().getApi(UserApi.class).editpwd(-1,userInfo.user_id,oldPwd.getText().toString(),newPwd.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>() {
                    @Override
                    public void onSuccess(JsonElement s) {
                        JumpManager.jumpActivity(EditPwdActivity.this, LoginActivity.class);
                        ActivityStackManager.getInstance().clear();
                        EditPwdActivity.this.finish();
                        handProgressbar(false);
                    }

                    @Override
                    public void onError(String msg) {
                        handProgressbar(false);
                        showToast(msg);
                        showSoftPanel(submitTv);
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    private boolean isEmpty(EditText editext) {
        return TextUtils.equals(editext.getText().toString().trim(),"");
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!isEmpty(oldPwd)&&!isEmpty(newPwd)&&!isEmpty(newPwdAgain)){
            setBtnEnable(true);
        }else setBtnEnable(false);
    }

}
