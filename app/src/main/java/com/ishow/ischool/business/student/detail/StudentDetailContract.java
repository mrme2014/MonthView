package com.ishow.ischool.business.student.detail;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.student.StudentInfo;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by abel on 16/8/18.
 */
public interface StudentDetailContract {
    interface Model extends BaseModel {
        Observable<ApiResult<StudentInfo>> getStudent(HashMap<String, String> params);
    }

    interface View extends BaseView {
        void onGetStudentSuccess(StudentInfo student);

        void onGetStudentFailed(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void getStudent(HashMap<String, String> params);
    }
}
