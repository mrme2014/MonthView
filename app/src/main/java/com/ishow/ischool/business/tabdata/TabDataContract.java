package com.ishow.ischool.business.tabdata;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.bean.user.CampusInfo;

import java.util.ArrayList;
import java.util.TreeMap;

import rx.Observable;

/**
 * Created by wqf on 16/9/6.
 */
public interface TabDataContract {
    interface Model extends BaseModel {
        Observable<ApiResult<ArrayList<CampusInfo>>> getCampusList();

        Observable<ApiResult<SaleProcess>> getSaleProcessData(TreeMap<String, Integer> params);
    }

    interface View extends BaseView {
        void getListSuccess(ArrayList<CampusInfo> campusInfos);

        void getListFail(String msg);

        void getSaleSuccess(SaleProcess saleProcess);

        void getSaleFail(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void getCampusList();

        abstract void getSales(TreeMap<String,Integer> params);
    }
}
