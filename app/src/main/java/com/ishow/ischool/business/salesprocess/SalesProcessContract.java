package com.ishow.ischool.business.salesprocess;


import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;


/**
 * Created by wqf on 16/8/14.
 */
public interface SalesProcessContract {
    interface Model extends BaseModel {
//        Observable<ApiResult<StudentList>> getList4StudentStatistics(HashMap<String, String> params, int page);
    }

    interface View extends BaseView {
//        void getListSuccess(StudentList studentList);
//        void getListFail(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
//        public abstract void getList4StudentStatistics(HashMap<String, String> params, int page);
    }
}
