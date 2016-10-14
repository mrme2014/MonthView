package com.ishow.ischool.business.user.pick;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.saleprocess.Marketposition;
import com.ishow.ischool.bean.saleprocess.Subordinate;
import com.ishow.ischool.bean.user.UserListResult;

import rx.Observable;

/**
 * Created by wqf on 16/8/18.
 */
public interface UserPickContract {
    interface Model extends BaseModel {
        Observable<ApiResult<UserListResult>> listUser(int campusId, String keyword, int page);
        Observable<ApiResult<Marketposition>> getPosition(int campusId);
        Observable<ApiResult<Subordinate>> getUserByPosition(int campusId, int position_id, String keywords, int page);
    }

    interface View<T> extends BaseView {
        void getListSuccess(T result);
        void getListFail(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void listUser(int campusId,String keyword, int page);
        public abstract void getPosition(int campusId);
        public abstract void getUserByPosition(int campusId, int position_id, String keywords, int page);
    }
}
