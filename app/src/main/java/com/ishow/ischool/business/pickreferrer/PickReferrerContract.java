package com.ishow.ischool.business.pickreferrer;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.user.UserListResult;

import rx.Observable;

/**
 * Created by wqf on 16/8/18.
 */
public interface PickReferrerContract {
    interface Model extends BaseModel {
        Observable<ApiResult<UserListResult>> getReferrers(String keyword, int page);
    }

    interface View extends BaseView {
        void getListSuccess(UserListResult userListResult);
        void getListFail(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getReferrers(String keyword, int page);
    }
}
