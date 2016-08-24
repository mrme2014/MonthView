package com.ishow.ischool.application;

import android.text.TextUtils;

import com.commonlib.application.BaseApplication;
import com.commonlib.http.ApiFactory;
import com.commonlib.util.LogUtil;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.user.Token;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.common.manager.TokenManager;
import com.ishow.ischool.common.manager.UserManager;
import com.ishow.ischool.util.AppUtil;
import com.zaaach.citypicker.utils.LocManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * Created by MrS on 2016/7/1.
 */

public class CrmApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        LocManager.getInstance().init(this);
        initUser();
        initApi();
    }

    private void initUser() {
        UserManager.getInstance().init(this);
        User mUser = UserManager.getInstance().get();
        UserManager.getInstance().initCampusPositions(mUser);
        if (mUser != null) {
            TokenManager.init(mUser.token);
        }
    }


    private void initApi() {
        ApiFactory.getInstance().build(getApplicationContext(), Env.SITE_URL, new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Token token = TokenManager.getToken();
                LogUtil.e(token == null ? "=======" : "token" + token.token);
                Request newRequest = chain.request();
                if (token != null && !TextUtils.isEmpty(token.token)) {
                    HttpUrl url = newRequest.url().newBuilder().addQueryParameter("token", token == null ? "" : token.token).build();
                    newRequest = newRequest.newBuilder().url(url).build();
                }
                newRequest.newBuilder().addHeader("app_version_small", "1")
                        .addHeader("api_version", "1.1")
                        .addHeader("app_version", "1.1")
                        .addHeader("app_os", "android")
                        .addHeader("app_type", "huawei")
                        .build();
                okhttp3.Response response = chain.proceed(newRequest);

                String bodyString = "";
                if (response.isSuccessful()) {
                    bodyString = response.body().string();
//                    LogUtil.d(this, "bodyString = " + bodyString);
                    if (bodyString != null) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(bodyString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (jsonObject != null) {
                            int error_no = jsonObject.optInt("error_no");
                            // 判断error_no
                            if (error_no == ApiResult.ERROR_TOKEN1 || error_no == ApiResult.ERROR_TOKEN2) {
                                AppUtil.reLogin(getInstance().getBaseContext());
                            }
                        }
                    }
                }
                okhttp3.Response r = response.newBuilder()
                        .body(ResponseBody.create(response.body().contentType(), bodyString))
                        .build();
                return r;
            }
        });
    }

    private HashMap<String, String> statisticsFilterParam;

    public HashMap<String, String> getFilerParam() {
        if (statisticsFilterParam == null) {
            statisticsFilterParam = new HashMap<String, String>();
        }
        return statisticsFilterParam;
    }

    public void setFilerParam(HashMap<String, String> filerParam) {
        this.statisticsFilterParam = filerParam;
    }

}
