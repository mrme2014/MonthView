package com.ishow.ischool.business.salesprocess;

import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.common.api.ApiObserver;

/**
 * Created by wqf on 16/8/14.
 */
public class SalesProcessPresenter extends SalesProcessContract.Presenter {


    @Override
    public void getSaleProcessData(int campus_id, int position_id, int user_id, int type) {
        mModel.getSaleProcessData(campus_id,position_id,user_id,type).subscribe(new ApiObserver<SaleProcess>() {
            @Override
            public void onSuccess(SaleProcess saleProcess) {
                mView.getListSuccess(saleProcess);
            }

            @Override
            public void onError(String msg) {
                mView.getListFail(msg);
            }

            @Override
            protected boolean isAlive() {
                return  mView==null?false:mView.isActivityFinished();
            }
        });

    }
}
