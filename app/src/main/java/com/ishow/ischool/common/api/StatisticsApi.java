package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.campusperformance.EducationMonthResult;
import com.ishow.ischool.bean.campusperformance.MonthTableData;
import com.ishow.ischool.bean.campusperformance.SignAmountResult;
import com.ishow.ischool.bean.campusperformance.SignPerformanceResult;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.bean.saleprocess.SaleTable1;
import com.ishow.ischool.bean.statistics.EducationHome;
import com.ishow.ischool.bean.statistics.EducationSummary;
import com.ishow.ischool.bean.statistics.MarketHome;
import com.ishow.ischool.bean.statistics.OtherStatisticsTable;
import com.ishow.ischool.bean.statistics.Table;
import com.ishow.ischool.bean.teachprocess.TeachProcess;

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


/*
 * Created by MrS on 2016/9/19.
 */

public interface StatisticsApi {
    /**
     * 名称	类型	是否必须	示例值	默认值	描述	排序
     * campus_id	Int	0			校区ID 总部身份选择校区时，需要传	0
     * position_id	Int	0			职位ID	0
     * user_id	Int	0			指定看某个员工的	0
     * type	Int	0			7,30,90,180,365,999 对应的时间段	0
     *
     * @param resources_id
     * @return
     */
    //数据分析.市场.销售流程分析(statistics.market.process) 接口
    @FormUrlEncoded
    @POST("/statistics/market/process")
    Observable<ApiResult<SaleProcess>> getSaleProcessData(@Field("resources_id") int resources_id, @FieldMap TreeMap<String, Integer> map);

    @FormUrlEncoded
    @POST("/statistics/market/process")
    Observable<ApiResult<SaleProcess>> getSaleProcessData(
            @FieldMap HashMap<String, String> params);

    //数据分析.市场.各校区业绩对比(statistics.market.bazaarcontrast) 接口
    @GET("/statistics/market/bazaarcontrast")
    Observable<ApiResult<SignPerformanceResult>> getSignPerformance(
            @Query("campus_id") String campus_id,           //多个校区用逗号连接
            @Query("begin_month") Integer begin_month,      //开始月份201604
            @Query("end_month") Integer end_month,
            @Query("data_type") Integer data_type,          //数据类型 1:晨读 3:校聊
            @Query("option") String option                  //campusTotal:业绩对比,campusMonth:表格显示,signTotal:人员对比
    );

    //数据分析.市场.各校区业绩对比(statistics.market.bazaarcontrast) 接口
    @GET("/statistics/market/bazaarcontrast")
    Observable<ApiResult<SignAmountResult>> getSignAmount(
            @Query("from") int from,                        //请求来源,0:pc端
            @Query("campus_id") String campus_id,           //多个校区用逗号连接
            @Query("begin_month") Integer begin_month,      //开始月份201604
            @Query("end_month") Integer end_month,
            @Query("data_type") Integer data_type,          //数据类型 1:晨读 3:校聊
            @Query("option") String option                  //campusTotal:业绩对比,campusMonth:表格显示,signTotal:人员对比
    );

    //数据分析.市场.各校区业绩月份对比(statistics.market.campusmonth) 接口
    @GET("/statistics/market/campusmonth")
    Observable<ApiResult<MonthTableData>> getCampusMonth(
            @Query("from") int from,                        //请求来源,0:pc端
            @Query("campus_id") String campus_id,           //多个校区用逗号连接
            @Query("begin_month") Integer begin_month,      //开始月份201604
            @Query("end_month") Integer end_month
    );

    //数据分析.市场.其他类别统计(statistics.market.other)
    @GET("/statistics/market/other")
//    @GET("/statistics/education/teachingprocessanalysis")
    Observable<ApiResult<OtherStatisticsTable>> getOtherStatistics(@QueryMap HashMap<String, String> params);


    //  -----------------------------   教学   ------------------------------  //


    //数据分析.教务教学.各校区业绩对比(statistics.education.bazaarcontrast)
    @GET("/statistics/education/bazaarcontrast")
    Observable<ApiResult<EducationMonthResult>> getEducationMonth(
            @Query("campus_id") String campus_id,           //多个校区用逗号连接
            @Query("begin_month") Integer begin_month,      //开始月份201604
            @Query("end_month") Integer end_month,
            @Query("option") String option                  //educationMonth
    );

    /**
     * start_time	Int	0			开始时间	0
     * end_time	Int	0			结束时间	0
     * position_id	Int	0			职位id	0
     * user_id	Int	0			用户id	0
     *
     * @param params
     * @return
     */
    //数据分析.教务教学.教学流程分析(statistics.education.teachingprocessanalysis) 接口
    @GET("/statistics/education/teachingprocessanalysis")
    Observable<ApiResult<TeachProcess>> getTeatProcess(@QueryMap TreeMap<String, Integer> params);

/*<<<<<<< HEAD
    //数据分析.教务教学.教学流程分析(statistics.education.teachingprocessanalysis) 接口
    @GET("/statistics/education/teachingprocessanalysis")
    Observable<ApiResult<TeachProcess>> getTeatProcess4Old(@QueryMap TreeMap<String, Integer> params);
=======*/
    //数据分析.教学.首页.集团教学流程概况(statistics.education.processanalysishome) 接口
    @GET("/statistics/education/processanalysishome")
    Observable<ApiResult<TeachProcess>> getTeachProcess4Home(@QueryMap TreeMap<String, Integer> params);

    @GET("/statistics/market/home")
    Observable<ApiResult<MarketHome>> getMarketHomeData(@QueryMap HashMap<String, Integer> params);

    @GET("/statistics/education/home")
    Observable<ApiResult<EducationHome>> getEducationHomeData(@QueryMap HashMap<String, Integer> params);

    //    @HEAD("Cache-Control public, max-age=60")
    @GET("/statistics/market/summary")
    Observable<ApiResult<Table>> getHomeMarketSummary(@QueryMap HashMap<String, Integer> params);

    @GET("/statistics/education/summary")
    Observable<ApiResult<EducationSummary>> getHomeEducationSummary(@QueryMap HashMap<String, Integer> params);

    /*数据分析.市场.流程表格(statistics.market.processhome) 接口*/
    @FormUrlEncoded
    @POST("statistics/market/processhome")
    Observable<ApiResult<SaleTable1>>  getComMarketSaleprocess(@Field("begin_time")int begin_time, @Field("end_time")int end_time);

    /*数据分析.市场.流程图表(statistics.market.processchart) 接口*/
    @FormUrlEncoded
    @POST("statistics/market/processchart")
    Observable<ApiResult<SaleTable1>>  getComMarketProcesschart(@Field("begin_time")int begin_time, @Field("end_time")int end_time);
}
