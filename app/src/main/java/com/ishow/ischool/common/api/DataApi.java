package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;

import com.ishow.ischool.bean.statistics.OtherStatisticsTable;

import java.util.HashMap;

import retrofit2.http.QueryMap;
import com.ishow.ischool.bean.campusperformance.SignAmountResult;
import com.ishow.ischool.bean.campusperformance.SignPerformanceResult;
import com.ishow.ischool.bean.saleprocess.SaleProcess;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;


 * Created by MrS on 2016/9/19.
 */
public interface DataApi {
    /**
     * 名称	类型	是否必须	示例值	默认值	描述	排序
     * campus_id	Int	0			校区ID 总部身份选择校区时，需要传	0
     * position_id	Int	0			职位ID	0
     * user_id	Int	0			指定看某个员工的	0
     * type	Int	0			7,30,90,180,365,999 对应的时间段	0
     *
     * @param resources_id
     * @param type
     * @return
     */
    //数据分析.市场.销售流程分析(statistics.market.process) 接口
    @FormUrlEncoded
    @POST("/statistics/market/process")
    Observable<ApiResult<SaleProcess>> getSaleProcessData(@Field("resources_id") int resources_id,
                                                          @Field("campus_id") int campus_id,
                                                          @Field("position_id") int position_id,
                                                          @Field("user_id") int user_id,
                                                          @Field("type") int type);

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

    //数据分析.市场.其他类别统计(statistics.market.other)
    @GET("/statistics/market/other")
    Observable<ApiResult<OtherStatisticsTable>> getOtherStatistics(@QueryMap HashMap<String, String> params);
}
