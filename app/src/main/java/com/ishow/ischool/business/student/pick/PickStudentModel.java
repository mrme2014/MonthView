package com.ishow.ischool.business.student.pick;

import com.commonlib.Conf;
import com.commonlib.http.ApiFactory;
import com.ishow.ischool.application.Resourse;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.student.StudentList;
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
    public Observable<ApiResult<StudentList>> getStudentStatisticsList(HashMap<String, String> params, int page) {
        return ApiFactory.getInstance().getApi(MarketApi.class)
                .listStudentStatistics(Resourse.STUDENT_LIST, params, Conf.DEFAULT_PAGESIZE_LISTVIEW, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
