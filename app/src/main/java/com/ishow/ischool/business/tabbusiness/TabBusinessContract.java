package com.ishow.ischool.business.tabbusiness;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;

/**
 * Created by wqf on 16/8/14.
 */
public interface TabBusinessContract {
    interface Model extends BaseModel {
    }

    interface View extends BaseView {
    }

    abstract class Presenter extends BasePresenter<Model, View> {
    }
}
