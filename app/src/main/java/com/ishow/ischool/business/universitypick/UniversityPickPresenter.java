package com.ishow.ischool.business.universitypick;

import com.ishow.ischool.bean.university.UniversityInfo;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.ArrayList;

/**
 * Created by wqf on 16/8/16.
 */
public class UniversityPickPresenter extends UniversityPickContract.Presenter {

    @Override
    public void getListUniversity(String cityName) {
        mModel.getListUniversity(cityName)
                .subscribe(new ApiObserver<ArrayList<UniversityInfo>>() {
                    @Override
                    public void onSuccess(ArrayList<UniversityInfo> universityInfos) {
                        mView.getListSuccess(universityInfos);
                    }

                    @Override
                    public void onError(String msg) {
                        mView.getListFail(msg);
                    }
                });
    }

    @Override
    public void searchUniversity(String universityName) {
        mModel.searchUniversity(universityName)
                .subscribe(new ApiObserver<ArrayList<UniversityInfo>>() {
                    @Override
                    public void onSuccess(ArrayList<UniversityInfo> universityInfos) {
                        mView.searchSuccess(universityInfos);
                    }

                    @Override
                    public void onError(String msg) {
                    }
                });
    }
}
