package com.ishow.ischool.business.statisticslist;

import com.ishow.ischool.bean.student.StudentList;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by wqf on 16/8/14.
 */
public class StatisticsListPresenter extends StatisticsListContract.Presenter {

    @Override
    public void getList4StudentStatistics(HashMap<String, String> params, int page) {
        params.put("fields", "studentInfo,avatarInfo");
        mModel.getList4StudentStatistics(params, page)
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
