package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.classes.ClassList;
import com.ishow.ischool.bean.classes.TeacherList;
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
    @GET("/education/classes/studentlists")
    Observable<ApiResult<StudentList>> listStudent(
            @Query("resources_id") int resources_id,
            @Query("allIn") int allIn,      //0:当前还在班级里的学员，1:所有在班级里呆过的学员，包括已经转班和调班的学员
            @Query("classes_id") int classes_id
    );

    /*属性.选择项.查询(attribute.option.get) 接口*/  // 获取课程顾问列表和教师列表
    @GET("/attribute/option/get")
    Observable<ApiResult<TeacherList>> getTeacherList(
            @Query("option") String option,
            @Query("campus_id") Integer campus_id,
            @Query("course_type") Integer course_type,
            @Query("keyword") String keyword,
            @Query("pagesize") int pagesize,
            @Query("page") int page);
}
