package com.ishow.ischool.business.tabdata;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.saleprocess.SaleProcess;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by abel on 16/10/9.
 */

public class DataTeachModel implements DataTeachContract.Model {
    @Override
    public Observable<ApiResult<SaleProcess>> getTeachingProcess(HashMap<String, String> params) {
        return null;
    }
}
