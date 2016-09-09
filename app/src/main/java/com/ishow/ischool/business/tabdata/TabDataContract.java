package com.ishow.ischool.business.tabdata;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;

/**
 * Created by wqf on 16/9/6.
 */
public interface TabDataContract {
    interface Model extends BaseModel {
//        Observable<ApiResult<ArrayList<CampusInfo>>> getCampusList();
    }

    interface View extends BaseView {
//        void getListSuccess(ArrayList<CampusInfo> campusInfos);
//        void getListFail(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
//        abstract void getCampusList();
    }
}
