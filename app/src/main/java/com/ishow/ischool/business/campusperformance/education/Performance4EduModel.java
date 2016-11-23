package com.ishow.ischool.business.campusperformance.education;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.campusperformance.EducationMonthResult;
import com.ishow.ischool.common.api.StatisticsApi;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/9/8.
 */
public class Performance4EduModel implements Performance4EduContract.Model {

    public Observable<ApiResult<EducationMonthResult>> getEduMonthPerformance(String campusIds, int beginMonth, int endMonth) {
        return ApiFactory.getInstance().getApi(StatisticsApi.class).getEducationMonth(campusIds, beginMonth, endMonth, "educationMonth")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
