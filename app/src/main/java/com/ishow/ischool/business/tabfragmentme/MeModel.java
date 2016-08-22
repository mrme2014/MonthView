package com.ishow.ischool.business.tabfragmentme;

import com.commonlib.core.BaseModel;
import com.commonlib.http.ApiFactory;
import com.ishow.ischool.common.api.UserApi;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MrS on 2016/8/16.
 */
public class MeModel implements BaseModel {
    public Observable logout(){
       return ApiFactory.getInstance().getApi(UserApi.class).logout(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
