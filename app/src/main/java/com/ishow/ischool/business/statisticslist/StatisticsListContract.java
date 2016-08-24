package com.ishow.ischool.business.statisticslist;


import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.student.StudentList;

import java.util.HashMap;

import rx.Observable;


/**
 * Created by wqf on 16/8/14.
 */
public interface StatisticsListContract {
    interface Model extends BaseModel {
        Observable<ApiResult<StudentList>> getList4StudentStatistics(HashMap<String, String> params, int page);
    }

    interface View extends BaseView {
        void getListSuccess(StudentList studentList);
        void getListFail(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getList4StudentStatistics(HashMap<String, String> params, int page);
    }
}
