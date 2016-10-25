package com.ishow.ischool.business.classes.classlist;


import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.classes.ClassList;

import java.util.HashMap;

import rx.Observable;


/**
 * Created by wqf on 16/10/21.
 */
public interface ClassListContract {
    interface Model extends BaseModel {
        Observable<ApiResult<ClassList>> listClasses(HashMap<String, String> params, int page);
    }

    interface View extends BaseView {
        void getListSuccess(ClassList classList);
        void getListFail(String msg);
    }

   abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getListClasses(HashMap<String, String> params, int page);
    }
}
