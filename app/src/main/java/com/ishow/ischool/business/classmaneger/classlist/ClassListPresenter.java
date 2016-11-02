package com.ishow.ischool.business.classmaneger.classlist;

import com.ishow.ischool.bean.classes.ClassList;
import com.ishow.ischool.bean.classes.TeacherList;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by wqf on 16/10/21.
 */
public class ClassListPresenter extends ClassListContract.Presenter {

    @Override
    public void getListClasses(HashMap<String, String> params, int page) {
        mModel.listClasses(params, page)
                .subscribe(new ApiObserver<ClassList>() {
                    @Override
                    public void onSuccess(ClassList classList) {
                        mView.getListSuccess(classList);
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

    @Override
    public void listTeacher(Integer campusId, Integer courseType, String option, String keyword, int page) {
        mModel.listTeacher(campusId, courseType, option, keyword, page)
                .subscribe(new ApiObserver<TeacherList>() {
                    @Override
                    public void onSuccess(TeacherList teacherInfos) {
                        mView.getListSuccess(teacherInfos);
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
