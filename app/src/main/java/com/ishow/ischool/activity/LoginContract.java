package com.ishow.ischool.activity;


import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.user.User;

import rx.Observable;


/**
 * Created by baixiaokang on 16/4/29.
 */
public interface LoginContract {
    interface Model extends BaseModel {
        Observable<ApiResult<User>> login(String name, String pass);
//        Observable<CreatedResult> sign(String name, String pass);
    }


    interface View extends BaseView {
        void loginSuccess();
//        void signSuccess();
//        void showMsg(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void login(String name, String pass);
//        public abstract void sign(String name, String pass);
    }
}
