package com.ishow.ischool.business.student.pick;

import com.ishow.ischool.bean.student.StudentStatisticsList;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by abel on 16/8/17.
 */
public class PickStudentPresenter extends PickStudentContract.Presenter {
    @Override
    public void getStudentStatisticsList(HashMap<String, String> params) {
        mModel.getStudentStatisticsList(params).subscribe(new ApiObserver<StudentStatisticsList>() {
            @Override
            public void onSuccess(StudentStatisticsList studentStatisticsList) {
                mView.getListSuccess(studentStatisticsList);
            }

            @Override
            public void onError(String msg) {
                mView.getListFail(msg);
            }
        });
    }
}
