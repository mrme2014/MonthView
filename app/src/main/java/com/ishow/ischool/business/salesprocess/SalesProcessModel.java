package com.ishow.ischool.business.salesprocess;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.common.api.DataApi;

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

}
