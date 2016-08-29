package com.ishow.ischool.business.user.pick;

import com.commonlib.Conf;
import com.commonlib.http.ApiFactory;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.user.UserListResult;
import com.ishow.ischool.common.api.UserApi;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/8/18.
 */
public class UserPickModel implements UserPickContract.Model {

    @Override
    public Observable<ApiResult<UserListResult>> listUser(int campusId, String keyword, int page) {
        return ApiFactory.getInstance().getApi(UserApi.class)
                .listUsers(Resource.USER_LISTS, keyword, Conf.DEFAULT_PAGESIZE_LISTVIEW, page,campusId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
