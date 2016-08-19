package com.ishow.ischool.business.student.detail;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by abel on 16/8/18.
 */
public interface StudentDetailContract {
    interface Model extends BaseModel {

    }

    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void getStudent(HashMap<String, String> params);
    }
}
