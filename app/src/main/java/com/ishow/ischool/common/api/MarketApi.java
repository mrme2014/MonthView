package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.saleprocess.Marketposition;
import com.ishow.ischool.bean.saleprocess.Subordinate;
import com.ishow.ischool.bean.student.Student;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.bean.student.StudentList;
import com.ishow.ischool.bean.university.SearchUniversityResult;
import com.ishow.ischool.bean.university.UniversityInfo;
import com.ishow.ischool.bean.university.UniversityInfoListResult;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.bean.user.CampusInfo;

import java.util.ArrayList;
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
 * Created by abel on 16/7/28.
 */
public interface MarketApi {

    int TYPESOURCE_ALL = -1;        // 全部来源
    int TYPESOURCE_READING = 1;     // 晨读
    int TYPESOURCE_RECOMMEND = 2;   // 转介绍
    int TYPESOURCE_CHAT = 3;        // 校聊

    int TYPETIME_REGISTER = 1;          // 登记时间
    int TYPETIME_MATRICULATION = 2;     // 入学时间


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
            @Field("source") int source_id               //来源。1.晨读；2.转介绍；3.校聊
//            @Field("referrer") int referrer_id,            //晨读讲师id或推荐人id或校聊专员id
            );

    //获取所有大学
    @GET("/system/university/getall")
    Observable<ApiResult<ArrayList<UniversityInfo>>> listUniversity(
            @Query("page") Integer page,
            @Query("pagesize") Integer pagesize);

    //根据省id或地区id 获取大学
    @GET("/system/university/getuniversity")
    Observable<ApiResult<UniversityInfoListResult>> getUniversity(
            @Query("city_name") String city_name,
            @Query("provinceid") Integer provinceid,
            @Query("cityid") Integer cityid);

    //搜索大学
    @GET("/system/university/search")
    Observable<ApiResult<SearchUniversityResult>> searchUniversity(
            @Query("resources_id") int resources_id,
            @Query("name") String name,
            @Query("page") Integer page,
            @Query("pagesize") Integer pagesize);

    //根据校区id获取校区信息，id为0，获取所有校区
    @GET("/system/campus/get")
    Observable<ApiResult<ArrayList<Campus>>> getCampus(
            @Query("resources_id") int resources_id,
            @Query("campus_id") int campus_id);

    @GET("/market/student/get")
    Observable<ApiResult<Student>> getStudent(@QueryMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/market/student/edit")
    Observable<ApiResult<Object>> editStudent(@FieldMap HashMap<String, String> params);

    @GET("/system/campus/lists")
    Observable<ApiResult<ArrayList<CampusInfo>>> getCampusList();

    /*属性.选择项.查询(attribute.option.get) 接口*/
    @FormUrlEncoded
    @POST("/attribute/option/get")
    Observable<ApiResult<Marketposition>> getOption(@Field("option") String option,@Field("campus_id") int campus_id);

    /*属性.选择项.查询(attribute.option.get) 接口*/
    @FormUrlEncoded
    @POST("/attribute/option/get")
    Observable<ApiResult<Subordinate>> getOptionSubordinate(@Field("option") String option, @Field("campus_id") int campus_id, @Field("position_id") int position_id);

    /*属性.选择项.查询(attribute.option.get) 接口*/
    @FormUrlEncoded
    @POST("/attribute/option/get")
    Observable<ApiResult<Subordinate>> getOptionSubordinateKeywords(@Field("option") String option, @Field("campus_id") int campus_id, @Field("position_id") int position_id,@Field("keyword")String keyword);
}
