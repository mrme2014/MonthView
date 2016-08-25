package com.ishow.ischool.business.communication.add;

import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by abel on 16/8/16.
 */
public class CommunicationAddPresenter extends CommunicationAddContract.Presenter {

    @Override
    void addCommunication(CommunicationAddActivity.CommunicationForm form) {
        HashMap<String, String> params = new HashMap<>();//HashMap<String, String>) ClazzUtil.getValue(form);
        params.put("student_id", form.student_id + "");
        params.put("status", form.status + "");
        params.put("type", form.type + "");
        params.put("content", form.content + "");
        params.put("result", form.result + "");
        params.put("refuse", form.refuse + "");
        params.put("belief", form.belief + "");
        params.put("tuition_source", form.tuition_source + "");
        params.put("communication_date", form.communication_date + "");
        params.put("callback_date", form.callback_date + "");
        params.put("campus_id", form.campus_id + "");
        params.put("resources_id", form.resources_id + "");

        mModel.addCommunication(params).subscribe(new ApiObserver<Object>() {
            @Override
            public void onSuccess(Object s) {
                mView.onAddSuccess();
            }

            @Override
            public void onError(String msg) {
                mView.onAddFailed(msg);
            }

            @Override
            protected boolean isAlive() {
                return mView != null && !mView.isActivityFinished();
            }
        });
    }

}
