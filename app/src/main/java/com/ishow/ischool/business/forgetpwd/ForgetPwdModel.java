package com.ishow.ischool.business.forgetpwd;

import com.commonlib.core.BaseModel;
import com.commonlib.http.ApiFactory;
import com.ishow.ischool.common.api.UserApi;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MrS on 2016/8/15.
 */
public class ForgetPwdModel implements BaseModel {
    public Observable getSmsCode(String mobile){
      return  ApiFactory.getInstance().getApi(UserApi.class).sendSms(mobile,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable checkrandcode(String mobile,String randCode){
        return   ApiFactory.getInstance().getApi(UserApi.class).checkrandcode(mobile,randCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable setpasswd(String mobile,String pwd){
        return   ApiFactory.getInstance().getApi(UserApi.class).setpasswd(mobile,pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
