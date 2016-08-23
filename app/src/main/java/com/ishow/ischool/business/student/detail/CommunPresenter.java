package com.ishow.ischool.business.student.detail;

import com.ishow.ischool.bean.market.CommunicationList;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by abel on 16/8/18.
 */
public class CommunPresenter extends CommunContract.Presenter {
    @Override
    void getCommunicationList(HashMap<String, String> params) {
        mModel.listCommunications(params).subscribe(new ApiObserver<CommunicationList>() {
            @Override
            public void onSuccess(CommunicationList communicationList) {
                mView.listCommunicationSuccess(communicationList);
            }

            @Override
            public void onError(String msg) {
                mView.listCommunicationFailed(msg);
            }
        });
    }

    public void editCommunication(final HashMap<String, String> params) {
        mModel.editCommunication(params).subscribe(new ApiObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                mView.onEditCommunicationSuccedd(params);
            }

            @Override
            public void onError(String msg) {
                mView.onEditCommunicationFailed(msg);
            }
        });
    }

    public void addCommunication(HashMap<String, String> params) {
        mModel.addCommunication(params).subscribe(new ApiObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                mView.onAddCommunicationSuccess();
            }

            @Override
            public void onError(String msg) {
                mView.onAddCommunicationFailed(msg);
            }
        });
    }
}
