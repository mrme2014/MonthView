package com.ishow.ischool.business.registrationform;

import com.commonlib.core.BasePresenter;
import com.commonlib.util.LogUtil;
import com.google.gson.JsonElement;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.registrationform.RegistraResult;
import com.ishow.ischool.common.api.ApiObserver;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;

/**
 * Created by MrS on 2016/11/21.
 */

public class regisPresenter extends BasePresenter<regisModel, regisView> {

    public void getPayInfo(int id, int status, String action, String fields) {
        mModel.getPayInfoObservable(id, status, action, fields).subscribe(new ApiObserver<RegistraResult>() {

            @Override
            public void onSuccess(RegistraResult RegistraResult) {
                mView.getRegistraInfo(RegistraResult);
            }

            @Override
            public void onError(String msg) {
                mView.getRegistraError(msg);
            }
        });
    }

    public void payAction(int studentid, JSONArray pay_method_json, String action, float price, float actual_price, int receipt_no, String memo, HashMap<String, Integer> time) {
        mModel.payAction(studentid, pay_method_json, action, price, actual_price, receipt_no, memo, time).subscribe(new ApiObserver<JsonElement>() {

            @Override
            public void onSuccess(JsonElement jsonElement) {
                mView.payActionSucess("保存成功");
            }

            @Override
            public void onError(String msg) {
                mView.getRegistraError(msg);
            }
        });
    }

    public void getCheapTypeList(int campuse_id) {
        mModel.getCheapList(campuse_id).subscribe(new ApiObserver<String>() {

            @Override
            public void onSuccess(String s) {
                LogUtil.e(s);
            }

            @Override
            public void onError(String msg) {
                mView.getRegistraError(msg);
            }
        });
    }

    public void getPayWayList(int campus_id) {
        Observable observable = mModel.getPayWayList(campus_id);
        Observable observable1 = mModel.getPayWayListAlipay(campus_id);
        Observable.zip(observable, observable1, new Func2<ApiResult<List<PayType>>, ApiResult<List<PayType>>, List<ApiResult<List<PayType>>>>() {
            @Override
            public List<ApiResult<List<PayType>>> call(ApiResult<List<PayType>> o, ApiResult<List<PayType>> o2) {
                List<ApiResult<List<PayType>>> list = new ArrayList();
                list.add(o);
                list.add(o2);
                return list;
            }
        }).subscribe(new Subscriber<List<ApiResult<List<PayType>>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.getRegistraError(e == null ? "data errror" : e.getMessage());
            }

            @Override
            public void onNext(List<ApiResult<List<PayType>>> o) {
            }
        });
    }
}
