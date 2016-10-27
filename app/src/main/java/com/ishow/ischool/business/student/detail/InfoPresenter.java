package com.ishow.ischool.business.student.detail;

import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;

import java.util.HashMap;

/**
 * Created by abel on 16/8/18.
 */
public class InfoPresenter extends InfoContract.Presenter {
    @Override
    void editStudent(final HashMap<String, String> params) {
        mModel.editStudent(params).subscribe(new ApiObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                mView.onEditStudentSuccess(params);
            }

            @Override
            public void onError(String msg) {
                mView.onEditStudentFailed(msg);
            }
        });
    }

    public void pickHometown() {

    }
}
