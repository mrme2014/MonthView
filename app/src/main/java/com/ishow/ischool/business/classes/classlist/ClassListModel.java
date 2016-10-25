package com.ishow.ischool.business.classes.classlist;

import com.commonlib.Conf;
import com.commonlib.http.ApiFactory;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.classes.ClassList;
import com.ishow.ischool.common.api.EducationApi;

import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/10/21.
 */
public class ClassListModel implements ClassListContract.Model {

    public Observable<ApiResult<ClassList>> listClasses(HashMap<String, String> params, int page) {
        return ApiFactory.getInstance().getApi(EducationApi.class)
                .listClasses(Resource.MARKET_STUDENT_STATISTICS, params, Conf.DEFAULT_PAGESIZE_LISTVIEW, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
