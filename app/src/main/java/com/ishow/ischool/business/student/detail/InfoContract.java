package com.ishow.ischool.business.student.detail;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by abel on 16/8/18.
 */
public interface InfoContract {
    interface Model extends BaseModel {
        Observable<ApiResult<Object>> editStudent(HashMap<String, String> params);
    }

    interface View extends BaseView {
        void onEditStudentSuccess(HashMap<String, String> student);

        void onEditStudentFailed(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void editStudent(HashMap<String, String> params);
    }
}
