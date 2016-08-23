package com.ishow.ischool.business.tabbusiness;

import com.ishow.ischool.bean.system.CampusInfo;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.ArrayList;

/**
 * Created by wqf on 16/8/14.
 */
public class TabBusinessPresenter extends TabBusinessContract.Presenter {

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
                });
    }
}
