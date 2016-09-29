package com.ishow.ischool.business.tabdata;

import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by wqf on 16/9/6.
 */
public class TabDataPresenter extends TabDataContract.Presenter {

    @Override
    void getCampusList() {
        mModel.getCampusList()
                .subscribe(new ApiObserver<ArrayList<CampusInfo>>() {
                    @Override
                    public void onSuccess(ArrayList<CampusInfo> campusInfos) {
                        mView.getListSuccess(campusInfos);
                    }

                    @Override
                    public void onError(String msg) {
                        mView.getListFail(msg);
                    }

                    @Override
                    protected boolean isAlive() {
                        return mView != null && !mView.isActivityFinished();
                    }
                });
    }

    @Override
    void getSales(TreeMap<String, Integer> params) {
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
