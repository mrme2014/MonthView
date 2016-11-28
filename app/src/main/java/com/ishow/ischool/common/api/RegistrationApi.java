package com.ishow.ischool.common.api;

import com.google.gson.JsonElement;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.registrationform.RegistraResult;
import com.ishow.ischool.business.registrationform.PayType;

import java.util.HashMap;
import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by MrS on 2016/11/22.
 */

public interface RegistrationApi {
    /*付款前的数据获取(market.payment.gopay) 接口*/

    /**
     * id     学员id
     * <p>
     * status  报名状态(付费状态)：1.未报名；2.欠款；3.已报名；4.退款
     * <p>
     * action  操作类型。报名:apply,升学:upgrade,付款:pay
     * <p>
     * fields   marketPayListInfo,studentInfo,userInfo,payListInfo,basePriceInfo----->payListInfo
     */
    @FormUrlEncoded
    @POST("market/payment/gopay")
    Observable<ApiResult<RegistraResult>> getPaymentInfo(@Field("id") int id,
                                                               @Field("status") int status,
                                                               @Field("action") String action,
                                                               @Field("fields") String fields);

    @FormUrlEncoded
    @POST("market/payment/gopaymobileweb")
    Observable<ApiResult<RegistraResult>> getStudentApplyInfo(@Field("id") int id,
                                                         @Field("status") int status,
                                                         @Field("action") String action,
                                                         @Field("fields") String fields);



    /*学员付款(market.payment.pay) 接口*/

    /**
     * @param student_id      学员id
     * @param pay_method_json [{"method":"现金","method_id":2,"account_id":0,"account":"","balance":4500}]
     * @param action          action  操作类型。报名:apply,升学:upgrade,付款:pay
     * @param price           应交金额
     * @param actual_price   实际缴款
     * @param time            第一次缴费时间 或者  第一次缴费时间和第二次补款时间(pay_time   arrearage_time)
     * @return
     */
    @FormUrlEncoded
    @POST("market/payment/pay")
    Observable<ApiResult<JsonElement>> payAction(@Field("student_id") int student_id,
                                                 @Field("pay_method_json") String pay_method_json,
                                                 @Field("action") String action,
                                                 @Field("price") float price,
                                                 @Field("actual_price") float actual_price,
                                                 @Field("receipt_no") int receipt_no,
                                                 @Field("memo") String memo,
                                                 @Field("app_type") int app_type,
                                                 @Field("privilege_type")int privilege_type,
                                                 @Field("privilege_price") double cheap_price,
                                                 @FieldMap HashMap<String, Integer> time);

    /*系统.付款方式.获取(system.paymentmethod.get) 接口*/

    /**
     * 获取付款方式
     *
     * @return
     */
    @FormUrlEncoded
    @POST("system/paymentmethod/get")
    Observable<ApiResult<JsonElement>> getPayList();

    /*系统.课程优惠.获取优惠类型(system.preferentialcourse.gettype) 接口*/
    /**
     * 获取优惠类型
     *
     * @return
     */
    @FormUrlEncoded
    @POST("system/preferentialcourse/get")
    Observable<ApiResult<JsonElement>> getCheapTypeList(@Field("campusid")int campusid,@Field("resources_id")int resources_id);

    /*system.bankcardmanagement.get*/
    /**
     * 获取收款类型列表  ----银行卡
     *
     * @return
     */
    @FormUrlEncoded
    @POST("system/bankcardmanagement/get")
    Observable<ApiResult<List<PayType>>> getPayBrankList(@Field("campusid")int campusid,@Field("resources_id")int resources_id);


    /*支付宝：system.alipaymanagement.get*/
   // 获取收款类型列表  ----支付宝
    @FormUrlEncoded
    @POST("system/alipaymanagement/get")
    Observable<ApiResult<List<PayType>>> getPayAlipayList(@Field("campusid")int campusid, @Field("resources_id")int resources_id);
}
