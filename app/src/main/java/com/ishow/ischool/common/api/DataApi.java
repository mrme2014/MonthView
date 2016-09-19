package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.statistics.OtherStatisticsTable;

import java.util.HashMap;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by abel on 16/9/19.
 */
public interface DataApi {

    @GET("/statistics/market/other")
    Observable<ApiResult<OtherStatisticsTable>> getOtherStatistics(@QueryMap HashMap<String, String> params);
}
