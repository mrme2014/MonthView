package com.ishow.ischool.common.api;

import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.attribute.AttrStudent;

import java.util.HashMap;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by abel on 16/11/1.
 */

public interface Attribute {

    @GET("/attribute/option/get")
    Observable<ApiResult<AttrStudent>> getStudentList(@QueryMap HashMap<String, String> map);
}
