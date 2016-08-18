package com.ishow.ischool.business.student.detail;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;

/**
 * Created by abel on 16/8/18.
 */
public interface CommunContract {
    interface Model extends BaseModel {

    }

    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<Model, View> {

    }
}
