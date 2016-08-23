package com.ishow.ischool.business.student.detail;

import com.ishow.ischool.bean.student.Student;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by abel on 16/8/18.
 */
public class StudentDetailPresenter extends StudentDetailContract.Presenter {
    @Override
    void getStudent(HashMap<String, String> params) {
        mModel.getStudent(params).subscribe(new ApiObserver<Student>() {
            @Override
            public void onSuccess(Student studentInfo) {
                mView.onGetStudentSuccess(studentInfo);
            }

            @Override
            public void onError(String msg) {
                mView.onGetStudentFailed(msg);
            }
        });
    }

    @Override
    void editStudent(HashMap<String, String> params) {
        mModel.editStudent(params).subscribe(new ApiObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                mView.onEditStudentSuccess(o);
            }

            @Override
            public void onError(String msg) {
                mView.onEditStudentFailed(msg);
            }
        });
    }
}
