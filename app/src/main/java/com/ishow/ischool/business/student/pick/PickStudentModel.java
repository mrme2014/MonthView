package com.ishow.ischool.business.student.pick;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.application.Constans;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.student.StudentStatisticsList;
import com.ishow.ischool.common.api.MarketApi;

import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abel on 16/8/17.
 */
public class PickStudentModel implements PickStudentContract.Model {

    @Override
    public Observable<ApiResult<StudentStatisticsList>> getStudentStatisticsList(int campusId, HashMap<String, String> params, int page) {
        return ApiFactory.getInstance().getApi(MarketApi.class)
                .listStudentStatistics(campusId, params, Constans.DEFAULT_PAGESIZE, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
