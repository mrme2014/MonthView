package com.ishow.ischool.activity;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.common.api.Api;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baixiaokang on 16/5/2.
 */
public class LoginModel implements LoginContract.Model {
    public Observable<ApiResult<User>> login(String name, String pass) {
        return Api.getUserApi().login(name, pass, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
//                .compose()
//                .subscribe(new ApiObserver<User>() {
//                    @Override
//                    public void onSuccess(User user) {
//                        mView.loginSuccess();
//                    }
//
//                    @Override
//                    public void onError(String msg) {
////                        mView.();
//                    }
//                });
    }

//    @Override
//    public Observable<CreatedResult> sign(String name, String pass) {
//        return Api.getInstance().service
//                .createUser(new _User(name, pass))
//                .compose(RxSchedulers.io_main());
//    }
}
