package com.ishow.ischool.business.user.pick;

import com.commonlib.Conf;
import com.commonlib.http.ApiFactory;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.saleprocess.Marketposition;
import com.ishow.ischool.bean.saleprocess.Subordinate;
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
                .listUsers(Resource.USER_LISTS, keyword, Conf.DEFAULT_PAGESIZE_LISTVIEW, page, campusId, "*")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<Marketposition>> getPosition(int campusId) {
        return ApiFactory.getInstance().getApi(UserApi.class)
                .getPosition("Marketposition", campusId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<Subordinate>> getUserByPosition(int campusId, int position_id, String keywords, int page) {
        return ApiFactory.getInstance().getApi(UserApi.class)
                .getUserByPosition("Subordinate", campusId, position_id, keywords, Conf.DEFAULT_PAGESIZE_LISTVIEW, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
