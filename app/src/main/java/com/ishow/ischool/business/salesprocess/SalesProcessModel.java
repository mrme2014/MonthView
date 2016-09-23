package com.ishow.ischool.business.salesprocess;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.saleprocess.Marketposition;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.bean.saleprocess.Subordinate;
import com.ishow.ischool.common.api.DataApi;
import com.ishow.ischool.common.api.MarketApi;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/8/14.
 */
public class SalesProcessModel implements SalesProcessContract.Model {

    public Observable<ApiResult<SaleProcess>> getSaleProcessData(int campus_id, int position_id, int user_id, int type) {
        return ApiFactory.getInstance().getApi(DataApi.class).getSaleProcessData(-1,campus_id,position_id,user_id,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public Observable<ApiResult<Marketposition>> getOption(String option, int position_id) {
        return ApiFactory.getInstance().getApi(MarketApi.class).getOption(option,position_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<Subordinate>> getOptionSubordinate(String option, int campus_id, int position_id) {
        return ApiFactory.getInstance().getApi(MarketApi.class).getOptionSubordinate(option,campus_id,position_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<Subordinate>> getOptionSubordinateKeyWords(String option, int campus_id, int position_id,String keywords) {
        return ApiFactory.getInstance().getApi(MarketApi.class).getOptionSubordinateKeywords(option,campus_id,position_id,keywords).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
