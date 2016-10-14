package com.ishow.ischool.business.tabdata;

import com.commonlib.core.BaseModel;
import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.common.api.DataApi;

import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abel on 16/10/13.
 */

public class DataMarketModel implements BaseModel {
    public Observable<ApiResult<SaleProcess>> getSaleProcessData(HashMap<String, String> params) {
        params.put("resources_id", "-1");

        return ApiFactory.getInstance().getApi(DataApi.class).getSaleProcessData(params).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
