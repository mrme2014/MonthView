package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.user.User;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by abel on 16/7/28.
 */
public interface UserApi {
    @FormUrlEncoded
    @POST("/system/user/login")
    Observable<ApiResult<User>> login(@Field("mobile") String mobile, @Field("passwd") String password, @Field("fields") String fields);

    //修改密码(system.user.editpwd) 接口
    @FormUrlEncoded
    @POST("/system/user/editpwd")
    Observable<ApiResult<String>> editpwd( @Field("user_id") int user_id, @Field("oldpasswd") String oldpasswd, @Field("newpasswd") String newpasswd);

    //找回密码(system.user.forgetpwd) 接口
    @FormUrlEncoded
    @POST("/system/user/forgetpwd")
    Observable<ApiResult> forgetPwd(@Field("mobile") String mobile, @Field("randcode") String randcode, @Field("passwd") String passwd);

    //退出登录(system.user.logout) 接口
    @FormUrlEncoded
    @POST("/system/user/logout")
    Observable<ApiResult<String>> logout();

    //修改信息(system.user.edit) 接口
    @FormUrlEncoded
    @POST("/system/user/edit")
    Observable<ApiResult> edit(@Field("user_id") int user_id,  @Field("birthday") int birthday, @Field("qq") String qq);


    //切换角色(system.user.change) 接口
    @FormUrlEncoded
    @POST("/system/user/change")
    Observable<ApiResult<String>> change();

    //获取七年上传token(system.qiniu.token) 接口
    @FormUrlEncoded
    @POST("/system/qiniu/token")
    Observable<ApiResult<String>> get_qiniui_token(@Field("type") int type);

    //APP找回密码第一步(system.user.checkrandcode) 接口
    @FormUrlEncoded
    @POST("/system/user/checkrandcode")
    Observable<ApiResult<String>> checkrandcode(@Field("mobile")String mobile,@Field("randcode")String randcode);

    //APP找回密码第二步，设置密码(system.user.setpasswd) 接口
    @FormUrlEncoded
    @POST("/system/user/setpasswd")
    Observable<ApiResult<String>> setpasswd(@Field("mobile")String mobile,@Field("passwd")String passwd);

    @FormUrlEncoded
    @POST("/system/sms/send")
    Observable<ApiResult<String>> sendSms(@Field("mobile") String mobile,@Field("type") int type);
}
