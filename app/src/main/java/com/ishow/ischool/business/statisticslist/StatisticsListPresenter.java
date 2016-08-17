package com.ishow.ischool.business.statisticslist;

import com.ishow.ischool.bean.student.StudentStatisticsList;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by wqf on 16/8/14.
 */
public class StatisticsListPresenter extends StatisticsListContract.Presenter {

    @Override
    public void getList4StudentStatistics(HashMap<String, String> params) {
        mModel.getList4StudentStatistics(params)
                .subscribe(new ApiObserver<StudentStatisticsList>() {
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
