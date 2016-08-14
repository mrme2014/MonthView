package com.ishow.ischool.business.login;

import com.commonlib.widget.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.common.api.UserApi;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 抽离出Model不仅各层更加分工明确便于Model的复用，而且大大简化了Presenter的代码量，让P层更简洁更专注。
 * Created by wqf on 16/8/13.
 */
public class LoginModel implements LoginContract.Model {
    public Observable<ApiResult<User>> login(String name, String pass) {
//        return Api.getUserApi().login(name, pass, "")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());

        return ApiFactory.getInstance().getApi(UserApi.class).login(name, pass, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
