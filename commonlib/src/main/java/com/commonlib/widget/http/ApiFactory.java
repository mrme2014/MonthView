package com.commonlib.widget.http;

import android.content.Context;

import com.commonlib.BuildConfig;
import com.commonlib.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wqf on 16/8/13.
 */
public class ApiFactory {

    private volatile static ApiFactory apiFactory;

    private Retrofit builder;
    private OkHttpClient okHttpClient;

    public static ApiFactory getInstance() {
        if (apiFactory == null) {
            synchronized (ApiFactory.class) {
                if (apiFactory == null) {
                    apiFactory = new ApiFactory();
                }
            }
        }
        return apiFactory;
    }

    private ApiFactory() {

    }


    //  ------- retrofit2.0 -------
    public void build(Context context, String serverUrl, Interceptor interceptor) {

        HttpLoggingInterceptor httpLoggingInterceptor = null;
        if (BuildConfig.DEBUG) {
            // 添加注释
            httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    LogUtil.dd("OkHttp", message);
                }
            });
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS);

        setCache(context, okHttpBuilder);
        okHttpClient = okHttpBuilder.build();

        builder = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

    }

    public <T> T getApi(Class<T> clazz) {
        return builder.create(clazz);
    }

    private void setCache(Context context, OkHttpClient.Builder okHttpBuilder) {
        File cacheFile = new File(context.getExternalCacheDir(), "responses");
        Cache cache = new Cache(cacheFile, 20 * 1024 * 1024);
        okHttpBuilder.cache(cache);
    }

    /**
     *
     */
    public void clearHttpCache() {
        try {
            okHttpClient.cache().evictAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
