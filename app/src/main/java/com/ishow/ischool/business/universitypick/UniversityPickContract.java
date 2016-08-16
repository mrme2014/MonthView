package com.ishow.ischool.business.universitypick;


import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.university.UniversityInfo;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by wqf on 16/8/16.
 */
public interface UniversityPickContract {
    interface Model extends BaseModel {
        Observable<ApiResult<ArrayList<UniversityInfo>>> getListUniversity(String cityName);
        Observable<ApiResult<ArrayList<UniversityInfo>>> searchUniversity(String universityName);
    }


    interface View extends BaseView {
        void getListSuccess(ArrayList<UniversityInfo> universityInfos);
        void getListFail(String msg);
        void searchSuccess(ArrayList<UniversityInfo> universityInfos);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getListUniversity(String cityName);
        public abstract void searchUniversity(String universityName);
    }
}
