package com.ishow.ischool.business.communicationadd;

import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by abel on 16/8/16.
 */
public class CommunicationAddPresenter extends CommunicationAddContract.Presenter {

    @Override
    void addCommunication(CommunicationAddActivity.CommunicationForm form) {
        HashMap<String, String> params = new HashMap<>();
        mModel.addCommunication(params).subscribe(new ApiObserver<String>() {
            @Override
            public void onSuccess(String s) {
                mView.onAddSuccess();
            }

            @Override
            public void onError(String msg) {
                mView.onAddFailed();
            }
        });
    }

}
