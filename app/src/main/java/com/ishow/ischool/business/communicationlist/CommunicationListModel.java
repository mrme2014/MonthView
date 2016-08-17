package com.ishow.ischool.business.communicationlist;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.market.CommunicationList;
import com.ishow.ischool.common.api.CommnicationApi;

import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abel on 16/8/15.
 */
public class CommunicationListModel implements CommunicationListContract.Model {

    public Observable<ApiResult<CommunicationList>> listCommunications(HashMap<String, String> params) {
        return ApiFactory.getInstance().getApi(CommnicationApi.class)
                .listCommnunication(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
