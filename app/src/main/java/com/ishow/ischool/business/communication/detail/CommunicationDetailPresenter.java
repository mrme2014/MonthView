package com.ishow.ischool.business.communication.detail;

import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by wqf on 16/9/1.
 */
public class CommunicationDetailPresenter extends CommunicationDetailContract.Presenter {

    public void editCommunication(final HashMap<String, String> params) {
        mModel.editCommunication(params).subscribe(new ApiObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                mView.onEditCommunicationSucceed(params);
            }

            @Override
            public void onError(String msg) {
                mView.onEditCommunicationFailed(msg);
            }

            @Override
            protected boolean isAlive() {
                return mView != null && !mView.isActivityFinished();
            }
        });
    }
}
