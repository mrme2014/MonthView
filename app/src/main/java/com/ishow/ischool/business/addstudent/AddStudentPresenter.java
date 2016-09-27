package com.ishow.ischool.business.addstudent;

import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.ArrayList;

/**
 * Created by wqf on 16/8/15.
 */
public class AddStudentPresenter extends AddStudentContract.Presenter {
    @Override
    void getCampus(int campusId) {
        mModel.getCampus(campusId).subscribe(new ApiObserver<ArrayList<Campus>>() {
            @Override
            public void onSuccess(ArrayList<Campus> campus) {
                mView.getCampusSuccess(campus);
            }

            @Override
            public void onError(String msg) {
                mView.getCampusFail(msg);
            }

            @Override
            protected boolean isAlive() {
                return mView != null && !mView.isActivityFinished();
            }
        });
    }

    @Override
    void addStudent(String name, String mobile, String qq, String wechat, int province_id, int city_id, int campus_id, int college_id, String major, int source, int grade) {
        mModel.addStudent(name, mobile, qq, wechat, province_id, city_id, campus_id, college_id, major, source, grade)
                .subscribe(new ApiObserver<StudentInfo>() {
                    @Override
                    public void onSuccess(StudentInfo studentInfo) {
                        mView.addStudentSuccess(studentInfo);
                    }

                    @Override
                    public void onError(String msg) {
                        mView.addStudentFail(msg);
                    }

                    @Override
                    protected boolean isAlive() {
                        return mView != null && !mView.isActivityFinished();
                    }
                });
    }
}
