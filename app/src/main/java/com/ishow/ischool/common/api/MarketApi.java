package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;

import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.bean.student.StudentStatisticsList;
import com.ishow.ischool.bean.university.SearchUniversityResult;
import com.ishow.ischool.bean.university.UniversityInfo;
import com.ishow.ischool.bean.user.Campus;

import java.util.ArrayList;
import java.util.HashMap;


import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by abel on 16/7/28.
 */
public interface MarketApi {

    //学员统计数据列表(market.StudentStatistics.lists) 接口
    @GET("/market/studentstatistics/lists")
    Observable<ApiResult<StudentStatisticsList>> listStudentStatistics(
            @Query("campus_id") int campus_id
//            @Query("option") String option,         //列表选择项的数据
//            @Query("campus_id") int campus_id,      //筛选的校区id
//            @Query("time_type") int time_type,     //筛选时间的类型（1.登记时间，2.上课时间）
//            @Query("start_time") int start_time,   //筛选的开始时间
//            @Query("end_time") int end_time,       //筛选的结束时间
//            @Query("pay_sate") int pay_sate)
    );

    //学员统计数据列表(market.StudentStatistics.lists) 接口
    @GET("/market/studentstatistics/lists")
    Observable<ApiResult<StudentStatisticsList>> listStudentStatistics(
            @QueryMap HashMap<String, String> params
    );


    int TYPE1 = 1;  // 晨读
    int TYPE2 = 2;  // 转介绍
    int TYPE3 = 3;  // 校聊
    //新学员登记
    @FormUrlEncoded
    @POST("/market/student/add")
    Observable<ApiResult<StudentInfo>> addStudent(
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("qq") String qq,
            @Field("province_id") int province_id,
            @Field("city_id") int city_id,
            @Field("campus_id") int campus_id,          //学员所在校区
            @Field("college_id") int college_id,        //学员所上的大学
            @Field("major") String major,
            @Field("source") int source_id,                //来源。1.晨读；2.转介绍；3.校聊
            @Field("referrer") int referrer_id,            //晨读讲师id或推荐人id或校聊专员id
            @Field("notes") String notes);

    //获取所有大学
    @GET("/system/university/getall")
    Observable<ApiResult<ArrayList<UniversityInfo>>> listUniversity(
            @Query("page") Integer page,
            @Query("pagesize") Integer pagesize);

    //根据省id或地区id 获取大学
    @GET("/system/university/getuniversity")
    Observable<ApiResult<ArrayList<UniversityInfo>>> getUniversity(
            @Query("city_name") String city_name,
            @Query("provinceid") Integer provinceid,
            @Query("cityid") Integer cityid);

    //搜索大学
    @GET("/system/university/search")
    Observable<ApiResult<SearchUniversityResult>> searchUniversity(
            @Query("name") String name,
            @Query("page") Integer page,
            @Query("pagesize") Integer pagesize);

    //根据校区id获取校区信息，id为0，获取所有校区
    @GET("/system/campus/get")
    Observable<ApiResult<ArrayList<Campus>>> getCampus(
            @Query("campus_id") int campus_id);
}
