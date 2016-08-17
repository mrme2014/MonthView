package com.ishow.ischool.business.login;


import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.user.User;

import rx.Observable;


/**
 * 官方MVP实例，通过协议类XXXContract来对View和Presenter的接口进行内部继承。是对BaseView和BasePresenter的进一步封装，
 * 所以我们实现的View和Presenter也只需要继承XXXContract中的对应内部接口就行。
 * Created by wqf on 16/8/13.
 */
public interface LoginContract {
    interface Model extends BaseModel {
        Observable<ApiResult<User>> login(String name, String pass,String fields);
    }
    interface View extends BaseView {
        void loginSuccess();
        void loginError(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void login(String name, String pass);
    }
}
