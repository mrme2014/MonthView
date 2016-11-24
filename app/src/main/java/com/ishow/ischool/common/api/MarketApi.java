package com.ishow.ischool.common.api;

/**
 * Created by abel on 16/7/28.
 */
@Deprecated
public interface MarketApi {

    /*  <T>Observable<ApiResult<Class<T>>>*/

    int TYPESOURCE_ALL = -1;        // 全部来源
    int TYPESOURCE_READING = 1;     // 晨读
    int TYPESOURCE_RECOMMEND = 2;   // 转介绍
    int TYPESOURCE_CHAT = 3;        // 校聊

    int TYPETIME_REGISTER = 1;          // 登记时间
    int TYPETIME_MATRICULATION = 2;     // 入学时间


}
