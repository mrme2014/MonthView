package com.ishow.ischool.business.student.edit;

import com.commonlib.core.BasePresenter;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by abel on 16/8/22.
 */
public class EditPresenter extends BasePresenter<EditModel, EditActivity> {
    public void editStudent(HashMap<String, String> params, final String text) {

        mModel.editStudent(params).subscribe(new ApiObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                mView.onEditSuccess(text);
            }

            @Override
            public void onError(String msg) {
                mView.onEditFailed(msg);
            }

            @Override
            protected boolean isAlive() {
                return mView != null && !mView.isActivityFinished();
            }
        });
    }
}
