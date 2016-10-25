package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.classes.ClassList;
import com.ishow.ischool.bean.student.StudentList;

import java.util.HashMap;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by mini on 16/10/21.
 */

public interface EducationApi {

    //班级列表(education.classes.lists) 接口
    @GET("/education/classes/lists")
    Observable<ApiResult<ClassList>> listClasses(
            @Query("resources_id") int resources_id,
            @QueryMap HashMap<String, String> params,
            @Query("pagesize") int pagesize,
            @Query("page") int page
    );

    //学生列表(education.classes.studentlists) 接口
    @GET("/education/classes/lists")
    Observable<ApiResult<StudentList>> listStudent(
            @Query("resources_id") int resources_id,
            @Query("classes_id") int classes_id
    );
}
