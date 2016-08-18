package com.ishow.ischool.business.communicationadd;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.common.api.CommunicationApi;

import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abel on 16/8/16.
 */
public class CommunicationAddModel implements CommunicationAddContract.Model {
    @Override
    public Observable<ApiResult<String>> addCommunication(HashMap<String, String> params) {

        return ApiFactory.getInstance().getApi(CommunicationApi.class)
                .addCommnunication(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
