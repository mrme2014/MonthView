package com.ishow.ischool.common.api;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.ishow.ischool.bean.ApiResult;

import java.lang.reflect.Type;

import rx.Observer;

/**
 * Created by abel on 16/7/29.
 */
public abstract class ApiObserver<T> implements Observer<ApiResult<T>> {

    private static final String TAG = "ApiCallback";

    private Gson gson = new Gson();
    private Class<T> clazz;
    private Type type;

    public ApiObserver() {
    }

    public ApiObserver(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ApiObserver(Type type) {
        this.type = type;
    }

    @Override
    public void onError(Throwable e) {
        onError(e.getMessage());
        onCompleted();
    }

    @Override
    public void onNext(ApiResult<T> body) {
        if (isAlive()) {
            return;
        }

        if (body == null) {
            onError("data error");
        } else {
            if (body == null) {
                onError("response is null");
            } else {
                if (body.error_no == 0) {
                    if (clazz == null && type == null) {
                        onSuccess(null);
                    } else if (clazz != null) {
                        T result = body.getResultBean(clazz);
                        if (result == null) {
                            onError("data error");
                        } else {
                            onSuccess(result);
                        }
                    } else if (type != null) {
                        T result = body.getResultBean(type);
                        if (result == null) {
                            onError("data error");
                        } else {
                            onSuccess(result);
                        }
                    }
                } else if (body.error_no >= 808) {
                    onError(TextUtils.isEmpty(body.error_msg) ? body.error_no + "" : body.error_msg);
                } else {
                    onError(TextUtils.isEmpty(body.error_msg) ? body.error_no + "" : body.error_msg);
                }
            }
        }
        onCompleted();
    }

    public abstract void onSuccess(T t);

    public abstract void onError(String msg);

    @Override
    public void onCompleted() {

    }

    /**
     * 检查是否继续，比如activiy已经结束
     *
     * @return
     */
    protected boolean isAlive() {
        return true;
    }
}
