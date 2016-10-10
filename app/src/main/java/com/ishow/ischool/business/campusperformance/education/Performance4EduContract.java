package com.ishow.ischool.business.campusperformance.education;


import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;


/**
 * Created by wqf on 16/9/8.
 */
public interface Performance4EduContract {
    interface Model extends BaseModel {
    }

    interface View extends BaseView {
    }

    abstract class Presenter extends BasePresenter<Model, View> {
    }
}
