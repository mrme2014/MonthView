package com.ishow.ischool.business.classmaneger.studentlist;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.student.StudentList;
import com.ishow.ischool.common.api.ClassesApi;
import com.ishow.ischool.common.api.EducationApi;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/10/20.
 */
public class StudentListModel implements StudentListContract.Model {

    public Observable<ApiResult<StudentList>> getListStudent(int class_id) {
        return ApiFactory.getInstance().getApi(ClassesApi.class).listStudent(Resource.EDUCATION_CLASSMANAGEMENT_STUDENTDETAIL, 1, class_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

}
