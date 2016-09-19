package com.ishow.ischool.business.salesprocess;


import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.saleprocess.SaleProcess;

import rx.Observable;


/**
 * Created by wqf on 16/8/14.
 */
public interface SalesProcessContract {
    interface Model extends BaseModel {
        Observable<ApiResult<SaleProcess>> getSaleProcessData(int campus_id, int position_id, int user_id, int type);
    }

    interface View extends BaseView {
        void getListSuccess(SaleProcess saleProcess);
        void getListFail(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getSaleProcessData(int campus_id, int position_id, int user_id, int type);
    }
}
