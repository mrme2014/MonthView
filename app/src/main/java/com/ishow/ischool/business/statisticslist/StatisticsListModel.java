package com.ishow.ischool.business.statisticslist;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.student.StudentStatisticsList;
import com.ishow.ischool.common.api.MarketApi;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/8/14.
 */
public class StatisticsListModel implements StatisticsListContract.Model {
    public Observable<ApiResult<StudentStatisticsList>> getList4StudentStatistics(int campusId) {
        return ApiFactory.getInstance().getApi(MarketApi.class).listStudentStatistics(campusId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
