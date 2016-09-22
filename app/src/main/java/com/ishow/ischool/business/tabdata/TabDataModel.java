package com.ishow.ischool.business.tabdata;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.common.api.DataApi;
import com.ishow.ischool.common.api.MarketApi;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/9/6.
 */
public class TabDataModel implements TabDataContract.Model {

    public Observable<ApiResult<ArrayList<CampusInfo>>> getCampusList() {
        return ApiFactory.getInstance().getApi(MarketApi.class).getCampusList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<SaleProcess>> getSaleProcessData(HashMap<String, String> params) {
        return ApiFactory.getInstance().getApi(DataApi.class).getSaleProcessData(Resource.MARKET_PROCESS, Integer.parseInt(params.get("campus_id")),
                Integer.parseInt(params.get("position_id")), Integer.parseInt(params.get("user_id")), Integer.parseInt(params.get("type")))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
