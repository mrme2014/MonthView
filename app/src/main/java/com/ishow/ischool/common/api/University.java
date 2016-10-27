package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.university.Address;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by abel on 16/10/25.
 */

public interface University {
    @GET("/system/university/getprovince")
    Observable<ApiResult<ArrayList<Address>>> getProvince();


    @GET("/system/university/getcitybyprovinceid")
    Observable<ApiResult<ArrayList<Address>>> getCityByProvinceId(@Query("provinceid") int provinceid);

}
