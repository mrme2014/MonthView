package com.ishow.ischool.business.classattention;

import com.commonlib.core.BaseModel;
import com.commonlib.http.ApiFactory;
import com.ishow.ischool.common.api.ClassesApi;

import java.util.TreeMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MrS on 2016/10/20.
 */

public class ClazModle implements BaseModel {

    public Observable getStudentList(int claz_id){
        /*     .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());*/
        return ApiFactory.getInstance().getApi(ClassesApi.class).studentlists(-1,0,claz_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable getCkeckinList(TreeMap<String,Integer> map){
        return ApiFactory.getInstance().getApi(ClassesApi.class).checkinlists(-1,map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable Ckeckin(String params,int claz_id,int date_unix){
        return ApiFactory.getInstance().getApi(ClassesApi.class).classCheckIn(params,claz_id,date_unix).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
