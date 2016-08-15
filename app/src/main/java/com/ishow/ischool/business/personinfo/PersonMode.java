package com.ishow.ischool.business.personinfo;

import com.commonlib.core.BaseModel;
import com.ishow.ischool.common.api.Api;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MrS on 2016/8/15.
 */
public class PersonMode implements BaseModel {
    public Observable getQiNiuToken(){
       return Api.getUserApi().get_qiniui_token(1);
    }

    public Observable submitInfo(int userId,String qq,int birthday){
        return Api.getUserApi().edit(userId,birthday,qq).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
