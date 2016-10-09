package com.ishow.ischool.business.tabdata;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.bean.user.CampusInfo;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Observable;

/**
 * Created by abel on 16/10/9.
 */

public interface DataTeachContract {
    interface Model extends BaseModel {

        Observable<ApiResult<SaleProcess>> getTeachingProcess(HashMap<String, String> params);
    }

    interface View extends BaseView {
        void getTeachingProcessSuccess(ArrayList<CampusInfo> campusInfos);

        void getTeachingProcessFail(String msg);

    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void getTeachingProcess(HashMap<String,String> params);

    }
}
