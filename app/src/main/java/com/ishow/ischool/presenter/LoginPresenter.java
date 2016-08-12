package com.ishow.ischool.presenter;

import com.commonlib.util.LogUtil;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.common.api.Api;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.base.presenter.impl.BasePresenter;
import com.ishow.ischool.view.ILoginView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abel on 16/8/9.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    public LoginPresenter(ILoginView view) {
        super(view);
    }

    public void login(String username, String passwd) {

        Api.getUserApi().login(username, passwd, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        LogUtil.e(user.getUserInfo().toString());
                        view.loginSuccess(user);
                    }

                    @Override
                    public void onError(String msg) {
                        LogUtil.e(msg);
                        view.loginError(msg);

                    }
                });
    }
}
