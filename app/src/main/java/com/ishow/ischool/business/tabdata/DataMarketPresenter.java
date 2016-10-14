package com.ishow.ischool.business.tabdata;

import com.commonlib.core.BasePresenter;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by abel on 16/10/13.
 */

public class DataMarketPresenter extends BasePresenter<DataMarketModel, DataMarketFragment> {
    void getSales(HashMap<String, String> params) {
        mModel.getSaleProcessData(params).subscribe(new ApiObserver<SaleProcess>() {
            @Override
            public void onSuccess(SaleProcess saleProcess) {
                mView.getSaleSuccess(saleProcess);
            }

            @Override
            public void onError(String msg) {
                mView.getSaleFail(msg);
            }
        });
    }
}
