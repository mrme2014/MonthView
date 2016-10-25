package com.ishow.ischool.business.classmaneger.studentlist;


import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.student.StudentList;

import rx.Observable;


/**
 * Created by wqf on 16/10/20.
 */
public interface StudentListContract {
    interface Model extends BaseModel {
        Observable<ApiResult<StudentList>> getListStudent(int class_id);
    }

    interface View extends BaseView {
        void getListSuccess(StudentList studentList);
        void getListFail(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getListStudent(int class_id);
    }
}
