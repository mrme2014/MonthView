package com.ishow.ischool.business.student.detail;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.student.Student;
import com.ishow.ischool.common.api.MarketApi;
import com.ishow.ischool.common.api.StudentApi;
import com.ishow.ischool.common.api.UserApi;

import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abel on 16/8/18.
 */
public class StudentDetailModel implements StudentDetailContract.Model {
    @Override
    public Observable<ApiResult<Student>> getStudent(HashMap<String, String> params) {
        return ApiFactory.getInstance().getApi(StudentApi.class)
                .getStudent(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<Object>> editStudent(HashMap<String, String> params) {
        return ApiFactory.getInstance().getApi(StudentApi.class)
                .editStudent(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable getQiNiuToken(int user_id) {
        return ApiFactory.getInstance().getApi(UserApi.class).getQiniuiToken(-1, 5, user_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable getAvatar(int user_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Resource.RESOURCE_ID, Resource.MARKET_STUDENT_STATISTICS + "");
        params.put("id", user_id + "");
        return ApiFactory.getInstance().getApi(StudentApi.class)
                .getStudent(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
