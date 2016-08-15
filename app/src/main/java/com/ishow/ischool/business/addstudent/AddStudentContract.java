package com.ishow.ischool.business.addstudent;


import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;

/**
 * Created by wqf on 16/8/15.
 */
public interface AddStudentContract {
    interface Model extends BaseModel {
    }


    interface View extends BaseView {
    }

    abstract class Presenter extends BasePresenter<Model, View> {
    }
}
