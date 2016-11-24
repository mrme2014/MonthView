package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.attribute.AttrStudent;
import com.ishow.ischool.bean.classes.TeacherList;
import com.ishow.ischool.bean.saleprocess.Marketposition;
import com.ishow.ischool.bean.saleprocess.Subordinate;
import com.ishow.ischool.bean.teachprocess.Educationposition;

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
 * Created by abel on 16/11/1.
 */

public interface AttributeApi {

    @GET("/attribute/option/get")
    Observable<ApiResult<AttrStudent>> getStudentList(@QueryMap HashMap<String, String> map);

    /*属性.选择项.查询(attribute.option.get) 接口*/
    @FormUrlEncoded
    @POST("/attribute/option/get")
//@Field("campus_id") int campus_id
    Observable<ApiResult<Marketposition>> getOption(@Field("option") String option, @FieldMap TreeMap<String, Integer> map);

    /*属性.选择项.查询(attribute.option.get) 接口*/
    @FormUrlEncoded
    @POST("/attribute/option/get")
//@Field("campus_id") int campus_id
    Observable<ApiResult<Educationposition>> getOptionEducation(@Field("option") String option, @FieldMap TreeMap<String, Integer> map);

    /*属性.选择项.查询(attribute.option.get) 接口*/
    @FormUrlEncoded
    @POST("/attribute/option/get")
//@Field("campus_id") int campus_id, @Field("position_id") int position_id
    Observable<ApiResult<Subordinate>> getOptionSubordinate(@Field("option") String option, @FieldMap TreeMap<String, Integer> map);

    /*属性.选择项.查询(attribute.option.get) 接口*/
    @FormUrlEncoded
    @POST("/attribute/option/get")
//@Field("campus_id") int campus_id, @Field("position_id") int position_id,
    Observable<ApiResult<Subordinate>> getOptionSubordinateKeywords(@Field("option") String option, @FieldMap TreeMap<String, Integer> map, @Field("keyword") String keyword);

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
