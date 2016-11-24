package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.bean.user.CampusInfo;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by abel on 16/11/22.
 */

public interface CampusApi {
    //根据校区id获取校区信息，id为0，获取所有校区
    @GET("/system/campus/get")
    Observable<ApiResult<ArrayList<Campus>>> getCampus(
            @Query("resources_id") int resources_id,
            @Query("campus_id") int campus_id);

    @GET("/system/campus/lists")
    Observable<ApiResult<ArrayList<CampusInfo>>> getCampusList();
}
