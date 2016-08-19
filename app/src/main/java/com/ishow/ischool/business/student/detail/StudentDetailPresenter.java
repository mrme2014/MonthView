package com.ishow.ischool.business.student.detail;

import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by abel on 16/8/18.
 */
public class StudentDetailPresenter extends StudentDetailContract.Presenter {
    @Override
    void getStudent(HashMap<String, String> params) {
        mModel.getStudent(params).subscribe(new ApiObserver<StudentInfo>() {
            @Override
            public void onSuccess(StudentInfo studentInfo) {
                mView.onGetStudentSuccess(studentInfo);
            }

            @Override
            public void onError(String msg) {
                mView.onGetStudentFailed(msg);
            }
        });
    }
}
