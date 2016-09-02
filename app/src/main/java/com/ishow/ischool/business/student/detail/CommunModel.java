package com.ishow.ischool.business.student.detail;

import com.commonlib.Conf;
import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.market.CommunicationList;
import com.ishow.ischool.common.api.CommunicationApi;

import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abel on 16/8/18.
 */
public class CommunModel implements CommunContract.Model {
    @Override
    public Observable<ApiResult<CommunicationList>> listCommunications(HashMap<String, String> params, int page) {
        return ApiFactory.getInstance().getApi(CommunicationApi.class)
                .listCommnunication(params, Conf.DEFAULT_PAGESIZE_LISTVIEW, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<Object>> addCommunication(HashMap<String, String> params) {
        return ApiFactory.getInstance().getApi(CommunicationApi.class)
                .addCommnunication(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
