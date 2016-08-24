package com.ishow.ischool.business.addstudent;


import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.bean.user.Campus;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by wqf on 16/8/15.
 */
public interface AddStudentContract {
    interface Model extends BaseModel {
        Observable<ApiResult<ArrayList<Campus>>> getCampus(int campusId);
        Observable<ApiResult<StudentInfo>> addStudent(String name,
                                                      String mobile,
                                                      String qq,
                                                      String wechat,
                                                      int province_id,
                                                      int city_id,
                                                      int campus_id,
                                                      int college_id,
                                                      String major,
                                                      int source,
                                                      String notes);
    }


    interface View extends BaseView {
        void getCampusSuccess(ArrayList<Campus> campus);
        void getCampusFail(String msg);

        void addStudentSuccess(StudentInfo studentInfo);
        void addStudentFail(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void getCampus(int campusId);
        abstract void addStudent(String name,
                                 String mobile,
                                 String qq,
                                 String wechat,
                                 int province_id,
                                 int city_id,
                                 int campus_id,
                                 int college_id,
                                 String major,
                                 int source,
                                 String notes);
    }
}
