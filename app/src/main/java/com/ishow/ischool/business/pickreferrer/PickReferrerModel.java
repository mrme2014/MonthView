package com.ishow.ischool.business.pickreferrer;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.application.Constans;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.user.UserListResult;
import com.ishow.ischool.common.api.UserApi;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/8/18.
 */
public class PickReferrerModel implements PickReferrerContract.Model {

    @Override
    public Observable<ApiResult<UserListResult>> getReferrers(int page) {
        return ApiFactory.getInstance().getApi(UserApi.class)
                .listUsers(Constans.DEFAULT_PAGESIZE, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
