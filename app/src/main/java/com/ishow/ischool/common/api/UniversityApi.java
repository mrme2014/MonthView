package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.university.Address;
import com.ishow.ischool.bean.university.SearchUniversityResult;
import com.ishow.ischool.bean.university.UniversityInfo;
import com.ishow.ischool.bean.university.UniversityInfoListResult;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by abel on 16/10/25.
 */

public interface UniversityApi {
    @GET("/system/university/getprovince")
    Observable<ApiResult<ArrayList<Address>>> getProvince();


    @GET("/system/university/getcitybyprovinceid")
    Observable<ApiResult<ArrayList<Address>>> getCityByProvinceId(@Query("provinceid") int provinceid);

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
}
