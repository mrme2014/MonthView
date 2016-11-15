package com.ishow.ischool.common.api;

import android.net.ParseException;
import android.text.TextUtils;

import com.commonlib.core.util.GenericUtil;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.ishow.ischool.bean.ApiResult;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;

/**
 * Created by abel on 16/7/29.
 */
public abstract class ApiObserver<T> implements Observer<ApiResult<T>> {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    private static final String TAG = "ApiCallback";

    private Gson gson = new Gson();

    public ApiObserver() {
    }

    @Override
    public void onError(Throwable e) {
        if (!isAlive()) {
            return;
        }
        e.printStackTrace();
        Throwable throwable = e;
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }

        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {

                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                    onError("服务端开小差了");
                    break;
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                default:
                    //均视为网络错误
                    onError("网络异常");
                    break;
            }
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {

            //均视为解析错误
            onError("数据异常");
        } else if (e instanceof ConnectException) {
            //均视为网络错误
            onError("连接失败");
        } else if (e instanceof UnknownHostException
                || e instanceof SocketTimeoutException) {
            onError("网络连接异常");
        } else {
            //未知错误
            onError("发生未知错误");
        }

        onCompleted();
    }

    @Override
    public void onNext(ApiResult<T> body) {
        if (!isAlive()) {
            return;
        }

        if (body == null) {
            onError("response is null");
            return;
        } else {
            if (body.error_no == 0) {
                Type type = GenericUtil.getGenericClass(this);
                if (type != null) {
                    T result = body.getResultBean(type);
                    if (result == null) {
                        onError("data error");
                        return;
                    } else {
                        try {
                            onSuccess(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        onSuccess(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                onError(TextUtils.isEmpty(body.error_msg) ? body.error_no + "" : body.error_msg);
                return;
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
