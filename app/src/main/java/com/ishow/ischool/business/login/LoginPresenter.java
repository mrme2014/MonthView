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
    public void login(String name, String pass) {
        mModel.login(name, pass, "*")
                .subscribe(new ApiObserver<User>() {
                    @Override
                    public void onSuccess(User user) {

                        if (user != null && user.token != null) {

                            //UserManager.getInstance().save(user);

                            UserManager.getInstance().initCampusPositions(user);

                            TokenManager.init(user.token);
                        }
                        mView.loginSuccess();
                        TokenManager.init(user.token);
                    }

                    @Override
                    public void onError(String msg) {
                        mView.loginError(msg);
                    }
                });
    }

}
