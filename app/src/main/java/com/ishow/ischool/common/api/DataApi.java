package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.campusperformance.SignAmountResult;
import com.ishow.ischool.bean.campusperformance.SignPerformanceResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wqf on 16/9/19.
 */
public interface DataApi {

    //数据分析.市场.各校区业绩对比(statistics.market.bazaarcontrast) 接口
    @GET("/statistics/market/bazaarcontrast")
    Observable<ApiResult<SignPerformanceResult>> getSignPerformance(
            @Query("from") int from,                        //请求来源,0:pc端
            @Query("campus_id") String campus_id,           //多个校区用逗号连接
            @Query("begin_month") Integer begin_month,      //开始月份201604
            @Query("end_month") Integer end_month,
            @Query("data_type") Integer data_type,          //数据类型 1:晨读 2:校聊
            @Query("option") String option                  //campusTotal:业绩对比,campusMonth:表格显示,signTotal:人员对比
    );

    //数据分析.市场.各校区业绩对比(statistics.market.bazaarcontrast) 接口
    @GET("/statistics/market/bazaarcontrast")
    Observable<ApiResult<SignAmountResult>> getSignAmount(
            @Query("from") int from,                        //请求来源,0:pc端
            @Query("campus_id") String campus_id,           //多个校区用逗号连接
            @Query("begin_month") Integer begin_month,      //开始月份201604
            @Query("end_month") Integer end_month,
            @Query("data_type") Integer data_type,          //数据类型 1:晨读 2:校聊
            @Query("option") String option                  //campusTotal:业绩对比,campusMonth:表格显示,signTotal:人员对比
    );
}
