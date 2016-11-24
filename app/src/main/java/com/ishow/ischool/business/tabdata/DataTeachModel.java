package com.ishow.ischool.business.tabdata;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.teachprocess.TeachProcess;
import com.ishow.ischool.common.api.StatisticsApi;

import java.util.TreeMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abel on 16/10/9.
 */

public class DataTeachModel implements DataTeachContract.Model {
    @Override
    public Observable<ApiResult<TeachProcess>> getTeachingProcess(TreeMap<String, Integer> params) {
        return ApiFactory.getInstance().getApi(StatisticsApi.class).getTeatProcess(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
