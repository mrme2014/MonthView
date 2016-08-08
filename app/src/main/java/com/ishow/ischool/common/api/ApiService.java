package com.ishow.ischool.common.api;


/**
 * Created by MrS on 2016/7/15.
 */
public interface ApiService {

//    @FormUrlEncoded
//    @POST("/system/user/login")
//    Observable<ApiResult<UserResult>> login(@Field("mobile") String phone, @Field("passwd") String password, @Field("fields") String fields);
//
//    //修改密码(system.user.editpwd) 接口
//    @FormUrlEncoded
//    @POST("/system/user/editpwd")
//    Observable<ApiResult> editpwd(@Field("token") String token, @Field("user_id") int user_id, @Field("oldpasswd") String oldpasswd, @Field("newpasswd") String newpasswd);
//
//    //找回密码(system.user.forgetpwd) 接口
//    @FormUrlEncoded
//    @POST("/system/user/forgetpwd")
//    Observable<ApiResult> forgetPwd(@Field("mobile") String mobile, @Field("randcode") String randcode, @Field("passwd") String passwd);
//
//    //退出登录(system.user.logout) 接口
//    @FormUrlEncoded
//    @POST("/system/user/logout")
//    Observable<ApiResult> logout(@Field("token") String token);
//
//    //获取七年上传token(system.qiniu.token) 接口
//    @FormUrlEncoded
//    @POST("/system/qiniu/token")
//    Observable<ApiResult> get_qiniui_token(@Field("token") String token, @Field("type") int type);
//
//    //修改信息(system.user.edit) 接口
//    @FormUrlEncoded
//    @POST("/system/user/edit")
//    Observable<ApiResult> edit(@Field("token") String token, @Field("user_id") int user_id, @Field("mobile") String mobile, @Field("user_name") String user_name, @Field("birthday") int birthday, @Field("job") String job, @Field("qq") String qq);
//
//    //发送短信(system.sms.send) 接口
//    @FormUrlEncoded
//    @POST("/system/sms/send")
//    Observable<ApiResult> send(@Field("mobile") String mobile, @Field("ip") String ip, @Field("type") int type);
//
//
//    //系统.大学.获取省(system.university.getprovince) 接口
//    @FormUrlEncoded
//    @POST("/system/university/getprovince")
//    Observable<Province> getprovince(@Field("token") String token);
//
//    //系统.大学.获取地区(system.university.getcitybyprovinceid) 接口
//    @FormUrlEncoded
//    @POST("/system/university/getcitybyprovinceid")
////a48b424ddf6ea378b97a4d7ce0b5806c
//    Observable<Province> getcitybyprovinceid(@Field("token") String token, @Field("provinceid") int provinceid);
//
//    //系统.大学.获取大学(	system.university.getuniversity) 接口
//    @FormUrlEncoded
//    @POST("/system/university/getuniversity")
//    Observable<Province> getuniversity(@Field("provinceid") int provinceid, @Field("cityid") int cityid);
//
//    //系统.校区.获取(system.campus.get) 接口
//    @FormUrlEncoded
//    @POST("/system/campus/get")
//    Observable<Province> getCampus(@Field("token") String token, @Field("campusid") int campusid);
//
//
//    //获取公开课本周
//    @FormUrlEncoded
//    @POST("/market/openclass/lists")
//    Observable<OpenClassInfo> getOpenClass(@Field("token") String token, @Field("fields") String fields, @FieldMap() TreeMap<String, Integer> paramMap);
//
//    //获取公开课本周
//    @FormUrlEncoded
//    @POST("/market/openclass/lists")
//    Observable<OpenClassInfo> getWeekOpenClass(@Field("token") String token, @Field("page") int page, @Field("fileds") String fileds);
//
//    @FormUrlEncoded
//    @POST("/market/openclass/lists")
//        //获取公开课历史纪录
//    Observable<OpenClassInfo> getHistoryOpenClass(@Field("token") String token, @Field("page") int page, @Field("begin_time") int begin_time);
//
//
//    //市场.公开课.添加(market.openclass.create) 接口  创建公开课
//    @FormUrlEncoded
//    @POST("/market/openclass/create")
//    Observable<ApiResult> creatpenClass(@Field("token") String token,
//                                        @Field("campus_id") int campus_id,
//                                        @Field("saler_id") int saler_id,
//                                        @Field("position") String className,
//                                        @Field("begin_time") int begin_time,
//                                        @Field("end_time") int end_time,
//                                        @Field("time_type") int time_type);
//
//    // 新学员登记(market.student.add) 接口
//    @FormUrlEncoded
//    @POST("market/student/add")
//    Observable<ApiResult<AddStudentResult>> addNewStudent(@Field("token") String token, @Field("province_id") int province_id,
//                                                          @Field("city_id") int city_id,
//                                                          @Field("name") String name,
//                                                          @Field("mobile") String mobile,
//                                                          @Field("qq") String qq,
//                                                          @Field("major") String major,
//                                                          @Field("campus_id") int campus_id,
//                                                          @Field("college_id") int college_id,
//                                                          @Field("source") int source,
//                                                          @Field("notes") String notes,
//                                                          @Field("referrer") int referrer,
//                                                          @Field("campus_manager") int campus_manager,
//                                                          @Field("charge") int charge);
//
//    //学员信息编辑(market.student.edit) 接口
//    @FormUrlEncoded
//    @POST("/market/student/edit")
//    Observable<ApiResult> editStudentInfo(
//            @Field("token") String token,
//            @Field("name") String name,
//            @Field("english_name") String english_name,
//            @Field("idcard") String idcard,
//            @Field("mobile") String mobile,
//            @Field("qq") String qq,
//            @Field("birthday") String birthday,//1981-05-05
//            @Field("province_id") int province_id,
//            @Field("college_id") int college_id,
//            @Field("major") String major,
//            @Field("grade") String grade,
//            @Field("id") int id);// 1  学员表中的唯一id
//
//    //获取学员信息(market.student.getStudentInfo) 接口
//    @FormUrlEncoded
//    @POST("/market/student/getstudentinfo")
//    Observable<ApiResult<StudentInfo>> get_StudentInfo(@Field("token") String token, @Field("id") int id);
//
//
//    //学员统计数据列表(market.StudentStatistics.lists) 接口
//    @FormUrlEncoded
//    @POST("/market/studentstatistics/lists")
////28f1d43fd49b5bd168918216302b5ded
//    Observable<ApiResult> get_studentStatistics_lists(@Field("token") String token, @Field("campus_id") int campus_id, @Field("time_type") int time_type);

}
