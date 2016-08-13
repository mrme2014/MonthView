package com.ishow.ischool.activity;

import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.common.api.ApiObserver;


/**
 * Created by baixiaokang on 16/4/29.
 */
public class LoginPresenter extends LoginContract.Presenter {

    @Override
    public void login(String name, String pass) {
        mModel.login(name, pass)
                .subscribe(new ApiObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        mView.loginSuccess();
                    }

                    @Override
                    public void onError(String msg) {
//                        mView.();
                    }
                });
    }

//    @Override
//    public void sign(String name, String pass) {
//        mRxManager.add(mModel.sign(name, pass)
//                .subscribe(res -> mView.signSuccess(),
//                        e -> mView.showMsg("注册失败!")));
//    }
}
