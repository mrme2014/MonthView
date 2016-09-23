package com.ishow.ischool.business.salesprocess;


import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.saleprocess.Marketposition;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.bean.saleprocess.Subordinate;

import rx.Observable;


/**
 * Created by wqf on 16/8/14.
 */
public interface SalesProcessContract {
    interface Model extends BaseModel {
        Observable<ApiResult<SaleProcess>> getSaleProcessData(int campus_id, int position_id, int user_id, int type);

        Observable<ApiResult<Marketposition>> getOption(String option, int position_id);

        Observable<ApiResult<Subordinate>> getOptionSubordinate(String option, int campus_id, int position_id);

        Observable<ApiResult<Subordinate>> getOptionSubordinateKeyWords(String option, int campus_id, int position_id,String keywords);
    }

    interface View<T> extends BaseView {
        void getListSuccess(T result);
        void getListFail(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getSaleProcessData(int campus_id, int position_id, int user_id, int type);

        public abstract void getOption(String option,int position_id);

        public abstract void getOptionSubordinate(String option, int campus_id, int position_id);

        public abstract void getOptionSubordinateKeyWords(String option, int campus_id, int position_id,String keywords);
    }


}
