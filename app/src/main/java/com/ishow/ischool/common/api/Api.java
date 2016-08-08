package com.ishow.ischool.common.api;

import com.ishow.ischool.application.Env;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MrS on 2016/7/15.
 */
public class Api {
    private static ApiService apiService;

    private static Retrofit retrofit;
    private static MarketApi marketApi;
    private static UserApi userApi;

    private static void init() {
        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(true);
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request()
                            .newBuilder().addHeader("app_version_small", "1")
                            .addHeader("api_version", "1.1")
                            .addHeader("app_version", "1.1")
                            .addHeader("app_os", "android")
                            .addHeader("app_type", "huawei")
                            .build();
                    return chain.proceed(request);
                }
            });
            OkHttpClient client = builder.build();

            Retrofit.Builder builder1 = new Retrofit.Builder();
            builder1.baseUrl(Env.SITE_URL);
            builder1.client(client);
            builder1.addConverterFactory(GsonConverterFactory.create());
            builder1.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
            retrofit = builder1.build();
        }

    }

    /**
     * @deprecated 建议将api分来
     */
    public static ApiService getInstance() {
        if (apiService == null) {
            synchronized (Api.class) {
                if (apiService == null) {
                    init();
                    apiService = retrofit.create(ApiService.class);
                }
            }
        }
        return apiService;
    }


    public static MarketApi getMarketApi() {
        if (marketApi == null) {
            synchronized (Api.class) {
                init();
                marketApi = retrofit.create(MarketApi.class);

            }
        }
        return marketApi;
    }

    public static UserApi getUserApi() {
        if (userApi == null) {
            synchronized (Api.class) {
                init();
                userApi = retrofit.create(UserApi.class);

            }
        }
        return userApi;
    }
}