package com.ishow.ischool.business.communication.edit;

import com.commonlib.core.BaseModel;
import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.common.api.CommunicationApi;

import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abel on 16/8/22.
 */
public class CommunEditModel implements BaseModel {

    public Observable<ApiResult<Object>> editCommun(HashMap<String, String> params) {
        return ApiFactory.getInstance().getApi(CommunicationApi.class)
                .editCommnunication(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
