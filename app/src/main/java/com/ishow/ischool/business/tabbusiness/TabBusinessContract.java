package com.ishow.ischool.business.tabbusiness;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.user.CampusInfo;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by wqf on 16/8/14.
 */
public interface TabBusinessContract {
    interface Model extends BaseModel {
        Observable<ApiResult<ArrayList<CampusInfo>>> getCampusList();
    }

    interface View extends BaseView {
        void getListSuccess(ArrayList<CampusInfo> campusInfos);
        void getListFail(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void getCampusList();
    }
}
