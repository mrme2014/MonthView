package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.saleprocess.SaleProcess;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
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
}
