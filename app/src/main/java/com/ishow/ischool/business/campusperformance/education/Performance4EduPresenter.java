package com.ishow.ischool.business.campusperformance.education;

import com.ishow.ischool.bean.campusperformance.EducationMonthResult;
import com.ishow.ischool.common.api.ApiObserver;

/**
 * Created by wqf on 16/9/8.
 */
public class Performance4EduPresenter extends Performance4EduContract.Presenter {

    @Override
    public void getEduMonthPerformance(String campusIds, int beginMonth, int endMonth) {
        mModel.getEduMonthPerformance(campusIds, beginMonth, endMonth)
                .subscribe(new ApiObserver<EducationMonthResult>() {
                    @Override
                    public void onSuccess(EducationMonthResult educationMonthResult) {
                        mView.getListSuccess(educationMonthResult);
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
