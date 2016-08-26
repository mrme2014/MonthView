package com.ishow.ischool.common.api;

import com.google.gson.JsonElement;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.bean.user.UserListResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by abel on 16/7/28.
 */
public interface UserApi {
    @FormUrlEncoded
    @POST("/system/user/login")
    Observable<ApiResult<User>> login(@Field("resources_id")int resources_id,@Field("mobile") String mobile, @Field("passwd") String password, @Field("fields") String fields);

    //修改密码(system.user.editpwd) 接口
    @FormUrlEncoded
    @POST("/system/user/editpwd")
    Observable<ApiResult<JsonElement>> editpwd(@Field("resources_id")int resources_id, @Field("user_id") int user_id, @Field("oldpasswd") String oldpasswd, @Field("newpasswd") String newpasswd);

    //找回密码(system.user.forgetpwd) 接口
    @FormUrlEncoded
    @POST("/system/user/forgetpwd")
    Observable<ApiResult<JsonElement>> forgetPwd(@Field("resources_id")int resources_id,@Field("mobile") String mobile, @Field("randcode") String randcode, @Field("passwd") String passwd);

    //退出登录(system.user.logout) 接口
    @FormUrlEncoded
    @POST("/system/user/logout")
    Observable<ApiResult<JsonElement>> logout(@Field("resources_id")int resources_id);

    //修改信息(system.user.edit) 接口
    @FormUrlEncoded
    @POST("/system/user/edit")
    Observable<ApiResult> edit(@Field("resources_id") int resources_id, @Field("user_id") int user_id, @Field("birthday") int birthday, @Field("qq") String qq);


    //切换角色(system.user.change) 接口
    @FormUrlEncoded
    @POST("/system/user/change")
    Observable<ApiResult<User>> change(@Field("resources_id")int resources_id,@Field("campus_id")int campus_id,@Field("position_id")int position_id);

    //获取七年上传token(system.qiniu.token) 接口
    @FormUrlEncoded
    @POST("/system/qiniu/token")
    Observable<ApiResult<JsonElement>> get_qiniui_token(@Field("resources_id")int resources_id,@Field("type") int type);

    //APP找回密码第一步(system.user.checkrandcode) 接口
    @FormUrlEncoded
    @POST("/system/user/checkrandcode")
    Observable<ApiResult<JsonElement>> checkrandcode(@Field("resources_id")int resources_id,@Field("mobile")String mobile,@Field("randcode")String randcode);


    //APP找回密码第二步，设置密码(system.user.setpasswd) 接口
    @FormUrlEncoded
    @POST("/system/user/setpasswd")
    Observable<ApiResult<JsonElement>> setpasswd(@Field("resources_id")int resources_id,@Field("mobile")String mobile,@Field("passwd")String passwd);

    @FormUrlEncoded
    @POST("/system/sms/send")
    Observable<ApiResult<JsonElement>> sendSms(@Field("resources_id")int resources_id,@Field("mobile") String mobile,@Field("type") int type);

    //用户详细(system.user.get) 接口
    @FormUrlEncoded
    @POST("/system/user/get")
    Observable<ApiResult<User>> getUser(@Field("resources_id")int resources_id, @Field("user_id") int user_id, @Field("fields")String fields);

    @GET("/system/user/lists")
    Observable<ApiResult<UserListResult>> listUsers(
            @Query("resources_id") int resources_id,
            @Query("keyword") String keyword,
            @Query("pagesize") int pagesize,
            @Query("page") int page);
}
