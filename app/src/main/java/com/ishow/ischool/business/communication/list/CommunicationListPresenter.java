package com.ishow.ischool.business.communication.list;

import com.ishow.ischool.bean.market.CommunicationList;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by abel on 16/8/15.
 */
public class CommunicationListPresenter extends CommunicationListContract.Presenter {

    public void listCommunication(HashMap<String, String> params, int page) {
        mModel.listCommunications(params, page).subscribe(new ApiObserver<CommunicationList>() {

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
                return mView != null && !mView.isActivityFinished();
            }

        });
    }
}
