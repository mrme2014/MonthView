package com.ishow.ischool.business.student.add;


import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.common.api.CampusApi;
import com.ishow.ischool.common.api.ClassesApi;
import com.ishow.ischool.common.api.MarketApi;
import com.ishow.ischool.common.api.StudentApi;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/8/15.
 */
public class AddStudentModel implements AddStudentContract.Model {
    public Observable<ApiResult<ArrayList<Campus>>> getCampus(int campusId) {
        return ApiFactory.getInstance().getApi(CampusApi.class).getCampus(41, campusId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<StudentInfo>> addStudent(String name, String mobile, String qq, String wechat, int province_id, int city_id, int campus_id, int college_id, String major, int source_id, int grade) {
        return ApiFactory.getInstance().getApi(StudentApi.class).addStudent(
                80, name, mobile, qq, wechat, province_id, city_id, campus_id, college_id, major, source_id, grade)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
