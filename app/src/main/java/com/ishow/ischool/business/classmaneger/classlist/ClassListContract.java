package com.ishow.ischool.business.classmaneger.classlist;


import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.classes.ClassList;
import com.ishow.ischool.bean.classes.TeacherList;

import java.util.HashMap;

import rx.Observable;


/**
 * Created by wqf on 16/10/21.
 */
public interface ClassListContract {
    interface Model extends BaseModel {
        Observable<ApiResult<ClassList>> listClasses(HashMap<String, String> params, int page);

        Observable<ApiResult<TeacherList>> listTeacher(Integer campusId, String option, String keyword, int page);
    }

    interface View<T> extends BaseView {
        void getListSuccess(T classList);

        void getListFail(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getListClasses(HashMap<String, String> params, int page);

        public abstract void listTeacher(Integer campusId, String option, String keyword, int page);
    }
}
