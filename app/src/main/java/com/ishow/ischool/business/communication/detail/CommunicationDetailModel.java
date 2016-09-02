package com.ishow.ischool.business.communication.detail;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.common.api.CommunicationApi;

import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/9/1.
 */
public class CommunicationDetailModel implements CommunicationDetailContract.Model {

    @Override
    public Observable<ApiResult<Object>> editCommunication(HashMap<String, String> params) {
        return ApiFactory.getInstance().getApi(CommunicationApi.class)
                .editCommnunication(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
