package com.ishow.ischool.business.companymarketsaleprocess;

import com.commonlib.core.BaseModel;
import com.commonlib.http.ApiFactory;
import com.ishow.ischool.common.api.StatisticsApi;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MrS on 2016/11/8.
 */

public class ComModel implements BaseModel {

    public Observable getComMarketSaleprocessTableData(int begin_time, int end_time) {
        return ApiFactory.getInstance().getApi(StatisticsApi.class).getComMarketSaleprocess(begin_time, end_time).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable getComMarketProcesschart(int begin_time, int end_time) {
        return ApiFactory.getInstance().getApi(StatisticsApi.class).getComMarketProcesschart(begin_time, end_time).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
