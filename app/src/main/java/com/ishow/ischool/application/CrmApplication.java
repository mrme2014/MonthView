package com.ishow.ischool.application;

import android.text.TextUtils;

import com.commonlib.application.BaseApplication;
import com.commonlib.http.ApiFactory;
import com.commonlib.util.DeviceUtils;
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
import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.callback.UpdateCheckCB;
import org.lzh.framework.updatepluginlib.callback.UpdateDownloadCB;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.model.UpdateParser;

import java.io.File;
import java.io.IOException;

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
        updateConfig();
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
        ApiFactory.getInstance().build(getApplicationContext(), Env.getSiteUrl(), new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Token token = TokenManager.getToken();
                Request newRequest = chain.request();
                if (token != null && !TextUtils.isEmpty(token.token)) {
                    HttpUrl url = newRequest.url().newBuilder().addQueryParameter("token", token == null ? "" : token.token).build();
                    newRequest = newRequest.newBuilder().url(url).build();
                }
                newRequest.newBuilder().addHeader("app_version_small", "0")
                        .addHeader("api_version", "V2.0")
                        .addHeader("app_version", DeviceUtils.getAppVersionCode(getBaseContext()) + "")
                        .addHeader("app_os", "Android")
                        .addHeader("app_type", "1")
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
                            String error_msg = jsonObject.optString("error_msg");
                            // 判断error_no
                            if (error_no == ApiResult.ERROR_TOKEN1 || error_no == ApiResult.ERROR_TOKEN2) {
                                AppUtil.reLoginShowToast(getInstance().getBaseContext());
                                //ToastUtils.showToast(getApplicationContext(),error_msg);
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


    public void updateConfig() {
//        final String testApkFile = "http://apk.hiapk.com/web/api.do?qt=8051&id=721";
        // Application中对Config进行配置
        final String TAG = "UpdateConfig";
        UpdateConfig.getConfig()
                .url(Env.getSiteUrl() + "attribute/app/init?device_type=1&app_type=1")// 随便模拟的一个网络接口。
                .jsonParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) {
                        // 此处模拟一个Update对象，传入接口返回的原始数据进去保存。
                        // 若用户需要自定义的时候。对于有额外参数。可从中获取并定制。
                        Update update = new Update(response);
                        if (!TextUtils.isEmpty(response)) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (jsonObject != null) {
                                JSONObject obj = jsonObject.optJSONObject("result").optJSONObject("update");
                                // 此apk包的更新时间
                                update.setUpdateTime(System.currentTimeMillis());
                                // 此apk包的下载地址
//                                update.setUpdateUrl(testApkFile);       // 测试用
                                update.setUpdateUrl(obj.optString("url"));
                                // 此apk包的版本号
                                update.setVersionCode(obj.optInt("code"));
                                // 此apk包的版本名称
                                update.setVersionName(obj.optString("new_version"));
                                // 此apk包的更新内容
                                update.setUpdateContent(obj.optString("content"));
                                boolean isForce;
                                String serverVersionName = obj.optString("update_version");
                                String localVersionName = DeviceUtils.getVersionName(getApplicationContext());
                                isForce = (Float.parseFloat(localVersionName) < Float.parseFloat(serverVersionName)) ? true : false;
                                // 此apk包是否为强制更新
                                update.setForced(isForce);
                                // 是否显示忽略此次版本更新
                                update.setIgnore(!isForce);
                            }
                        }
                        return update;
                    }
                })
                // 此参数可不配置。配置在此作为全局的更新接口回调通知
                .checkCB(new UpdateCheckCB() {
                    @Override
                    public void hasUpdate(Update update) {
                        LogUtil.d(TAG, "有新版本APK更新的回调");
                    }

                    @Override
                    public void noUpdate() {
                        LogUtil.d(TAG, "没有新版本的回调");
                    }

                    @Override
                    public void onCheckError(int code, String errorMsg) {
                        LogUtil.d(TAG, "更新检查错误的回调:code = " + code + "; errorMsg = " + errorMsg);
                    }

                    @Override
                    public void onUserCancel() {
                        LogUtil.d(TAG, "用户取消更新时的回调");
                    }

                    @Override
                    public void onCheckIgnore(Update update) {
                        LogUtil.d(TAG, "用户点击忽略此版本更新时的回调");
                    }
                })
                .downloadCB(new UpdateDownloadCB() {
                    @Override
                    public void onUpdateStart() {
                        LogUtil.d(TAG, "下载开始");
                    }

                    @Override
                    public void onUpdateComplete(File file) {
                        LogUtil.d(TAG, "下载完成");
                    }

                    @Override
                    public void onUpdateProgress(long current, long total) {
                        LogUtil.d(TAG, "下载进度:total = " + total + "; current = " + current);
                    }

                    @Override
                    public void onUpdateError(int code, String errorMsg) {
                        LogUtil.d(TAG, "下载apk错误:code = " + code + "; errorMsg = " + errorMsg);
                    }
                });
//                .updateDialogCreator(new DialogCreator() {
//                    @Override
//                    public Dialog create(final Update update, final Activity context) {
        // 此处为检查出有新版本需要更新时的回调。运行于主线程，在此进行更新Dialog的创建
        // 对于用户自定义的Dialog。用户可自行在此更新update中的数据对Dialog进行展示。
        // 在用户需要立即更新时。调用此类中的sendDownloadRequest(update,activity);
        // 在用户需要取消更新时。调用此类中的sendUserCancel();
        // 在用户需要忽略此版本更新时。调用此类中的sendCheckIgnore(update);
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//                        builder.setTitle(com.commonlib.R.string.str_update_title);
//                        builder.setMessage(update.getUpdateContent());
//                        builder.setNegativeButton(com.commonlib.R.string.str_cancel, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                sendUserCancel();
//                            }
//                        });
//                        builder.setPositiveButton(com.commonlib.R.string.str_ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                sendDownloadRequest(update, context);
//                            }
//                        });
//                        builder.show();
//                        return builder.create();
//                    }
//                });
    }

}
