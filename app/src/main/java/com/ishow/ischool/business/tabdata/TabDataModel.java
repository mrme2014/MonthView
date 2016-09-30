package com.ishow.ischool.business.tabdata;

import com.commonlib.http.ApiFactory;
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
        params.put("resources_id", "-1");

        return ApiFactory.getInstance().getApi(DataApi.class).getSaleProcessData(params) .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
