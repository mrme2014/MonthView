package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.market.CommunicationList;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by abel on 16/8/16.
 */
public interface CommnicationApi {
    /**
     * keyword	String	0			电话或手机	0
     * student_status	Int	0			学生状态	0
     * refuse_point	Int	0			抗拒点	0
     * study_belief	Int	0			学习信念	0
     * begin_time	Int	0			开始时间	0
     * end_time	Int	0			结束时间	0
     * incharger	Int	0			经办人ID	0
     * student_id	Int	0			学生ID	0
     * option	String	0			相关的下拉选项多个用,号隔开	0
     * fields	String	0			获取对象多个对象用,号隔开	0
     * type	Int	0		1	沟通记录类型	0
     * list_type	Int	0		1	列表形式1是正常列表，2是根据学生分组列表	0
     *
     * @return
     */
    @GET("/market/communication/lists")
    Observable<ApiResult<CommunicationList>> listCommnunication(@QueryMap HashMap<String, String> params);


    /**
     * student_id	Int	1			学生ID	0
     * status	Int	1			学生状态	0
     * type	Int	1		1	沟通记录类型1是市场，2 是业务	0
     * content	String	1			沟通内容	0
     * result	String	1			沟通结果	0
     * refuse	String	1			抗拒点	0
     * belief	Int	1			学习信念 1：低，2：中，3：高	0
     * tuition_source	String	0			学费来源	0
     * communication_date	Int	1			沟通日期	0
     * callback_date	Int	0			回访时间	0
     * campus_id	Int	1			校区ID	0
     *
     * @return
     */
    @POST("/market/communication/add")
    @FormUrlEncoded
    Observable<ApiResult<String>> addCommnunication(@FieldMap HashMap<String, String> params);

    /**
     * status	Int	0			学生状态	0
     * type	Int	0			类型	0
     * content	String	0			沟通内容	0
     * result	String	0			沟通结果	0
     * refuse	String	0			抗拒点	0
     * belief	Int	0			学习信念 1：低，2：中，3：高'	0
     * tuition_source	String	0			学费来源	0
     * communication_date	Int	0			沟通日期	0
     * callback_date	Int	0			回访时间	0
     * campus_id	Int	0			校区ID	0
     * id	Int	1			记录ID	0
     *
     * @param params
     * @return
     */
    @POST("/market/communication/edit")
    @FormUrlEncoded
    Observable<ApiResult<String>> editCommnunication(@FieldMap HashMap<String, String> params);

    @GET("/market/communication/delete")
    Observable<ApiResult<String>> deleteCommnunication(@Query("id") int id);


    /**
     * communication_id	Int	1			沟通记录ID	0
     * fields	String	0				0
     *
     * @param communicationId
     * @param fields
     * @return
     */
    @GET("/market/communication/get")
    Observable<ApiResult<String>> getCommnunication(@Query("communication_id") int communicationId, @Query("fields") String fields);

}
