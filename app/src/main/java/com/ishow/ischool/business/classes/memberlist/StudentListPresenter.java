package com.ishow.ischool.business.classes.memberlist;

import com.ishow.ischool.bean.student.StudentList;
import com.ishow.ischool.common.api.ApiObserver;

/**
 * Created by wqf on 16/10/20.
 */
public class StudentListPresenter extends StudentListContract.Presenter {

    @Override
    public void getListStudent(int class_id) {
        mModel.getListStudent(class_id)
                .subscribe(new ApiObserver<StudentList>() {
                    @Override
                    public void onSuccess(StudentList studentList) {
                        mView.getListSuccess(studentList);
                    }

                    @Override
                    public void onError(String msg) {
                        mView.getListFail(msg);
                    }

                    @Override
                    protected boolean isAlive() {
                        return mView != null && !mView.isActivityFinished();
                    }
                });
    }
}
