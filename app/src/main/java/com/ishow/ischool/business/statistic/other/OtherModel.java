package com.ishow.ischool.business.statistic.other;

import com.commonlib.core.BaseModel;
import com.commonlib.http.ApiFactory;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.statistics.OtherStatisticsTable;
import com.ishow.ischool.common.api.DataApi;

import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abel on 16/9/19.
 */
public class OtherModel implements BaseModel {

    Observable<ApiResult<OtherStatisticsTable>> getOtherStatistics(HashMap<String, String> params) {
//        params.put("resources_id", Resource.DATA_DATAANALYZE_BAZAAROTHER + "");
        return ApiFactory.getInstance().getApi(DataApi.class).getOtherStatistics(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
