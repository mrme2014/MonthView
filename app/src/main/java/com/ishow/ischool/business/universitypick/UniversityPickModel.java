package com.ishow.ischool.business.universitypick;


import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.university.SearchUniversityResult;
import com.ishow.ischool.bean.university.UniversityInfoListResult;
import com.ishow.ischool.common.api.UniversityApi;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/8/16.
 */
public class UniversityPickModel implements UniversityPickContract.Model {
    public Observable<ApiResult<UniversityInfoListResult>> getListUniversity(String cityName) {
        return ApiFactory.getInstance().getApi(UniversityApi.class).getUniversity(cityName, null, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<SearchUniversityResult>> searchUniversity(String universityName, int page) {
        return ApiFactory.getInstance().getApi(UniversityApi.class).searchUniversity(-1, universityName, page, 10000)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
