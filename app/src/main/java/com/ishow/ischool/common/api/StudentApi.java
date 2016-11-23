package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.course.CourseRecord;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.bean.student.StudentList;

import java.util.HashMap;

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

public interface StudentApi {

    int TYPESOURCE_ALL = -1;        // 全部来源
    int TYPESOURCE_READING = 1;     // 晨读
    int TYPESOURCE_RECOMMEND = 2;   // 转介绍
    int TYPESOURCE_CHAT = 3;        // 校聊

    //学员统计数据列表(market.StudentStatistics.lists) 接口（params是筛选条件）
    @GET("/market/studentstatistics/lists")
    Observable<ApiResult<StudentList>> listStudentStatistics(
            @Query("resources_id") int resources_id,
            @QueryMap HashMap<String, String> params,
            @Query("pagesize") int pagesize,
            @Query("page") int page
    );

    //新学员登记
    @FormUrlEncoded
    @POST("/market/student/add")
    Observable<ApiResult<StudentInfo>> addStudent(
            @Field("resources_id") int resources_id,
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("qq") String qq,
            @Field("wechat") String wechat,
            @Field("province_id") int province_id,
            @Field("city_id") int city_id,
            @Field("campus_id") int campus_id,          //学员所在校区
            @Field("college_id") int college_id,        //学员所上的大学
            @Field("major") String major,
            @Field("source") int source_id,               //来源。1.晨读；2.转介绍；3.校聊
//            @Field("referrer") int referrer_id,            //晨读讲师id或推荐人id或校聊专员id
            @Field("entering_school_year") int grade
    );

    @GET("/market/student/get")
    Observable<ApiResult<com.ishow.ischool.bean.student.Student>> getStudent(@QueryMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/market/student/edit")
    Observable<ApiResult<Object>> editStudent(@FieldMap HashMap<String, String> params);

    @GET("/market/studentstatistics/courserecord")
    Observable<ApiResult<CourseRecord>> getCourseRecord(@QueryMap HashMap<String, Integer> params);

}
