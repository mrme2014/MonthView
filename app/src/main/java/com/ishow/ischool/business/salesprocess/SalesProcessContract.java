package com.ishow.ischool.business.salesprocess;


import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.saleprocess.Marketposition;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.bean.saleprocess.Subordinate;
import com.ishow.ischool.bean.teachprocess.Educationposition;

import java.util.TreeMap;

import rx.Observable;


/**
 * Created by wqf on 16/8/14.
 */
public interface SalesProcessContract {
    interface Model extends BaseModel {
        Observable<ApiResult<SaleProcess>> getSaleProcessData(TreeMap<String,Integer> map, int type);

        Observable<ApiResult<Marketposition>> getOption(String option, TreeMap<String,Integer> map);

        Observable<ApiResult<Educationposition>> getOptionEducation(String option, TreeMap<String,Integer> map);

        Observable<ApiResult<Subordinate>> getOptionSubordinate(String option, TreeMap<String,Integer> map);

        Observable<ApiResult<Subordinate>> getOptionSubordinateKeyWords(String option,TreeMap<String,Integer> map,String keywords);
    }

    interface View<T> extends BaseView {
        void getListSuccess(T result);
        void getListFail(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getSaleProcessData(TreeMap<String,Integer> map, int type);

        public abstract void getOption(String option,TreeMap<String,Integer> map);

        public abstract void getOptionEducation(String option,TreeMap<String,Integer> map);

        public abstract void getOptionSubordinate(String option, TreeMap<String,Integer> map);

        public abstract void getOptionSubordinateKeyWords(String option, TreeMap<String,Integer> map,String keywords);
    }


}
