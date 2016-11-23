package com.ishow.ischool.common.api;

import com.google.gson.JsonElement;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.classattend.ClazCheckTable;
import com.ishow.ischool.bean.classattend.ClazStudentList;
import com.ishow.ischool.bean.classes.ClassList;
import com.ishow.ischool.bean.student.StudentList;

import java.util.HashMap;
import java.util.TreeMap;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by abel on 16/11/22.
 */

public interface ClassesApi {
    /*班级管理-学生签到(education.classes.checkin) 接口*/
    @FormUrlEncoded
    @POST("education/classes/checkin")
    Observable<ApiResult<JsonElement>> classCheckIn(@Field("param") String param, @Field("classes_id") int classes_id, @Field("classes_date") int classes_date);

    /* 班级管理-签到列表(education.classes.checkinlists) 接口*/
    @FormUrlEncoded
    @POST("education/classes/checkinlists")
    Observable<ApiResult<ClazCheckTable>> checkinlists(@Field("resources_id") int resources_id, @FieldMap TreeMap<String, Integer> map);


    /*班级管理-学生列表(education.classes.studentlists) 接口*/
    @FormUrlEncoded
    @POST("education/classes/studentlists")
    Observable<ApiResult<ClazStudentList>> studentlists(@Field("resources_id") int resources_id, @Field("allIn") int allIn, @Field("classes_id") int classes_id);

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

}
