package com.ishow.ischool.business.user.pick;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.user.UserListResult;

import rx.Observable;

/**
 * Created by wqf on 16/8/18.
 */
public interface UserPickContract {
    interface Model extends BaseModel {
        Observable<ApiResult<UserListResult>> listUser(int campusId, String keyword, int page);
    }

    interface View extends BaseView {
        void getListSuccess(UserListResult userListResult);

        void getListFail(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void listUser(int campusId,String keyword, int page);
    }
}
