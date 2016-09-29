package com.ishow.ischool.business.salesprocess;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.saleprocess.Marketposition;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.bean.saleprocess.Subordinate;
import com.ishow.ischool.common.api.DataApi;
import com.ishow.ischool.common.api.MarketApi;

import java.util.TreeMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/8/14.
 */
public class SalesProcessModel implements SalesProcessContract.Model {

    public Observable<ApiResult<SaleProcess>> getSaleProcessData(TreeMap<String,Integer> map, int type) {
        return ApiFactory.getInstance().getApi(DataApi.class).getSaleProcessData(-1,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public Observable<ApiResult<Marketposition>> getOption(String option, TreeMap<String,Integer> map) {
        return ApiFactory.getInstance().getApi(MarketApi.class).getOption(option,map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<Subordinate>> getOptionSubordinate(String option,  TreeMap<String,Integer> map) {
        return ApiFactory.getInstance().getApi(MarketApi.class).getOptionSubordinate(option,map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<Subordinate>> getOptionSubordinateKeyWords(String option,  TreeMap<String,Integer> map,String keywords) {
        return ApiFactory.getInstance().getApi(MarketApi.class).getOptionSubordinateKeywords(option,map,keywords).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
