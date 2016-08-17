package com.ishow.ischool.business.forgetpwd;

import com.commonlib.core.BasePresenter;
import com.google.gson.JsonElement;
import com.ishow.ischool.R;
import com.ishow.ischool.common.api.ApiObserver;

/**
 * Created by MrS on 2016/8/15.
 */
public class ForgetPresenter extends BasePresenter<ForgetPwdModel,ForgetPwdView> {

    public void sendSms(String mobile){
      mModel.getSmsCode(mobile).subscribe(new ApiObserver<JsonElement>() {

          @Override
          public void onSuccess(JsonElement s) {
              mView.onNetSucess(R.string.send_sms_sucess);
          }

          @Override
          public void onError(String msg) {
              mView.onNetfaield(msg);
          }
      });
    }


    public void checkRandCode(String mobile ,String randCode){
        mModel.checkrandcode(mobile,randCode)
                .subscribe(new ApiObserver<JsonElement>() {
                    @Override
                    public void onSuccess(JsonElement s) {
                        mView.onNetSucess(-1);
                    }

                    @Override
                    public void onError(String msg) {
                        mView.onNetfaield(msg);
                    }});
    }


    public void setPwd(String mobile ,String pwd){
        mModel.setpasswd(mobile,pwd).subscribe(new ApiObserver<JsonElement>() {

            @Override
            public void onSuccess(JsonElement s) {
                mView.onNetSucess(R.string.pwd_reset_sucess);
            }

            @Override
            public void onError(String msg) {
                mView.onNetfaield(msg);
            }
        });
    }
}
