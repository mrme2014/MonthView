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
    Observable<ApiResult<User>> login(@Field("mobile") String phone, @Field("passwd") String password, @Field("fields") String fields);

    //修改密码(system.user.editpwd) 接口
    @FormUrlEncoded
    @POST("/system/user/editpwd")
    Observable<ApiResult> editpwd(@Field("token") String token, @Field("user_id") int user_id, @Field("oldpasswd") String oldpasswd, @Field("newpasswd") String newpasswd);

    //找回密码(system.user.forgetpwd) 接口
    @FormUrlEncoded
    @POST("/system/user/forgetpwd")
    Observable<ApiResult> forgetPwd(@Field("mobile") String mobile, @Field("randcode") String randcode, @Field("passwd") String passwd);

    //退出登录(system.user.logout) 接口
    @FormUrlEncoded
    @POST("/system/user/logout")
    Observable<ApiResult> logout(@Field("token") String token);

    //修改信息(system.user.edit) 接口
    @FormUrlEncoded
    @POST("/system/user/edit")
    Observable<ApiResult> edit(@Field("token") String token, @Field("user_id") int user_id, @Field("mobile") String mobile, @Field("user_name") String user_name, @Field("birthday") int birthday, @Field("job") String job, @Field("qq") String qq);

}
