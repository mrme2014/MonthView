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
       return ApiFactory.getInstance().getApi(UserApi.class).logout(-1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable change(int position_id){
        return ApiFactory.getInstance().getApi(UserApi.class).change(-1,position_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
