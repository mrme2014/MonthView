package com.ishow.ischool.business.campusperformance.education;


import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.campusperformance.EducationMonthResult;

import rx.Observable;


/**
 * Created by wqf on 16/9/8.
 */
public interface Performance4EduContract {
    interface Model extends BaseModel {
        Observable<ApiResult<EducationMonthResult>> getEduMonthPerformance(String campusIds, int beginMonth, int endMonth);
    }

    interface View extends BaseView {
        void getListSuccess(EducationMonthResult educationMonthResult);
        void getListFail(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getEduMonthPerformance(String campusIds, int beginMonth, int endMonth);
    }
}
