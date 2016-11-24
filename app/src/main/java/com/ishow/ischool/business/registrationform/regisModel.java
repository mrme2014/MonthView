package com.ishow.ischool.business.registrationform;

import com.commonlib.core.BaseModel;
import com.commonlib.http.ApiFactory;
import com.ishow.ischool.common.api.RegistrationApi;

import org.json.JSONArray;

import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MrS on 2016/11/21.
 */

public class regisModel implements BaseModel {

    public Observable getPayInfoObservable(int id, int status, String action, String fields) {
        return ApiFactory.getInstance().getApi(RegistrationApi.class).getPaymentInfo(id, status, action, fields).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable payAction(int student_id, String pay_method_json, String action, float price, float actual_price, int receipt_no, String memo, HashMap<String, Integer> time) {
        return ApiFactory.getInstance().getApi(RegistrationApi.class).payAction(student_id,
                pay_method_json,
                action,
                price,
                actual_price,
                receipt_no,
                memo,
                1,
                time).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable getPayWayList(int campus_id){
        return ApiFactory.getInstance().getApi(RegistrationApi.class).getPayBrankList(campus_id,-1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable getPayWayListAlipay(int campus_id){
        return ApiFactory.getInstance().getApi(RegistrationApi.class).getPayAlipayList(campus_id,-1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable getCheapList(int campus_id){
        return ApiFactory.getInstance().getApi(RegistrationApi.class).getCheapTypeList(campus_id,-1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
