package com.ishow.ischool.business.login;

import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.manager.TokenManager;
import com.ishow.ischool.common.manager.UserManager;



/**
 * Created by wqf on 16/8/13.
 */
public class LoginPresenter extends LoginContract.Presenter {

    @Override
    void login(String name, String pass) {
        mModel.login(name, pass)
                .subscribe(new ApiObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if (user != null && user.getToken() != null) {
                            UserManager.getInstance().save(user);
                            TokenManager.init(user.getToken());
                        }
                        mView.loginSuccess();
                        TokenManager.init(user.getToken());
                    }

                    @Override
                    public void onError(String msg) {
                        mView.loginError(msg);
                    }
                });
    }

}
