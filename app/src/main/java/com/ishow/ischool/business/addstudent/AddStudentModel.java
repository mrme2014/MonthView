package com.ishow.ischool.business.addstudent;


import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.common.api.MarketApi;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/8/15.
 */
public class AddStudentModel implements AddStudentContract.Model {
    public Observable<ApiResult<ArrayList<Campus>>> getCampus(int campusId) {
        return ApiFactory.getInstance().getApi(MarketApi.class).getCampus(41, campusId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<StudentInfo>> addStudent(String name, String mobile, String qq, int province_id, int city_id, int campus_id, int college_id, String major, int source_id, String notes) {
        return ApiFactory.getInstance().getApi(MarketApi.class).addStudent(
                80, name, mobile, qq, province_id, city_id, campus_id, college_id, major, source_id, notes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
