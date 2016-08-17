package com.ishow.ischool.business.communicationlist;

import com.ishow.ischool.bean.market.CommunicationList;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by abel on 16/8/15.
 */
public class CommunicationListPresenter extends CommunicationListContract.Presenter {

    public void listCommunication(HashMap<String, String> params) {
        mModel.listCommunications(params).subscribe(new ApiObserver<CommunicationList>() {

            @Override
            public void onSuccess(CommunicationList data) {
                mView.listCommunicationSuccess(data);
            }

            @Override
            public void onError(String msg) {
                mView.listCommunicationFailed(msg);
            }

            @Override
            protected boolean isAlive() {
                return mView.isAlive();
            }
        });
    }
}
