package com.ishow.ischool.business.forgetpwd;

import com.commonlib.core.BasePresenter;
import com.ishow.ischool.R;
import com.ishow.ischool.common.api.ApiObserver;

/**
 * Created by MrS on 2016/8/15.
 */
public class ForgetPresenter extends BasePresenter<ForgetPwdModel,ForgetPwdView> {

    public void sendSms(String mobile){
      mModel.getSmsCode(mobile).subscribe(new ApiObserver<String>() {

          @Override
          public void onSuccess(String s) {
              mView.onSmsSucess(R.string.send_sms_sucess);
          }

          @Override
          public void onError(String msg) {
              mView.onSmsFaild(R.string.send_sms_faild);
          }
      });
    }


    public void checkRandCode(String mobile ,String randCode){
        mModel.checkrandcode(mobile,randCode)
                .subscribe(new ApiObserver<String>() {
                    @Override
                    public void onSuccess(String s) {
                        mView.onNetSucess(-1);
                    }

                    @Override
                    public void onError(String msg) {
                        mView.onNetfaield(R.string.check_sms_faild);
                    }});
    }


    public void setPwd(String mobile ,String pwd){
        mModel.setpasswd(mobile,pwd).subscribe(new ApiObserver<String>() {

            @Override
            public void onSuccess(String s) {
                mView.onNetSucess(R.string.pwd_reset_sucess);
            }

            @Override
            public void onError(String msg) {
                mView.onNetSucess(R.string.pwd_reset_faild);
            }
        });
    }
}
