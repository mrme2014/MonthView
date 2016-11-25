package com.ishow.ischool.business.student.edit;

import com.commonlib.core.BaseModel;
import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.common.api.StudentApi;

import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abel on 16/8/22.
 */
public class EditModel implements BaseModel {

    public Observable<ApiResult<Object>> editStudent(HashMap<String, String> params) {
        return ApiFactory.getInstance().getApi(StudentApi.class)
                .editStudent(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
