package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.student.StudentStatisticsList;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by abel on 16/7/28.
 */
public interface MarketApi {

    //学员统计数据列表(market.StudentStatistics.lists) 接口
    @GET("/market/studentstatistics/lists")
    Observable<ApiResult<StudentStatisticsList>> listStudentStatistics(
//            @Query("option") String option,         //列表选择项的数据
//            @Query("campus_id") int campus_id,      //筛选的校区id
//            @Query("time_type") int time_type,     //筛选时间的类型（1.登记时间，2.上课时间）
//            @Query("start_time") int start_time,   //筛选的开始时间
//            @Query("end_time") int end_time,       //筛选的结束时间
//            @Query("pay_sate") int pay_sate)
    );
}
