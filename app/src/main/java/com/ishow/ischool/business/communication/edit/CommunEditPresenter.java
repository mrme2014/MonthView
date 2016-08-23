package com.ishow.ischool.business.communication.edit;

import com.commonlib.core.BasePresenter;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by abel on 16/8/23.
 */
public class CommunEditPresenter extends BasePresenter<CommunEditModel, CommunicationEditActivity> {
    public void editCommunication(HashMap<String, String> p, final String text) {
        mModel.editCommun(p).subscribe(new ApiObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                mView.onEditSuccess(text);
            }

            @Override
            public void onError(String msg) {
                mView.onEditFailed(msg);
            }
        });
    }
}
