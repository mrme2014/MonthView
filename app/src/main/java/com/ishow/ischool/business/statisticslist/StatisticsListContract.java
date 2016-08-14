package com.ishow.ischool.business.statisticslist;


import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;


/**
 * Created by wqf on 16/8/14.
 */
public interface StatisticsListContract {
    interface Model extends BaseModel {
    }


    interface View extends BaseView {
    }

    abstract class Presenter extends BasePresenter<Model, View> {
    }
}
