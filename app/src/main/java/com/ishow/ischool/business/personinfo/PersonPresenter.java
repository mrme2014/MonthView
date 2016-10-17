package com.ishow.ischool.business.personinfo;

import com.commonlib.core.BasePresenter;
import com.google.gson.JsonElement;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.common.api.ApiObserver;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by MrS on 2016/8/15.
 */
public class PersonPresenter extends BasePresenter<PersonMode, PersonView> {
    /**
     * 上传 头像 之前先调用获取七牛token的接口
     *
     * @param user_id
     * @param filePath
     */
    public void uploadImg(final int user_id, final String filePath) {
        mModel.getQiNiuToken()
                .subscribe(new ApiObserver<JsonElement>() {
                    @Override
                    public void onSuccess(JsonElement s) {
                        try {
                            JSONObject object = new JSONObject(s.toString());
                            String token = object.getString("token");
                            uploadImg2QiNiu(user_id, filePath, token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (mView != null)
                                mView.onNetFailed(e == null ? "result error" : e.getMessage());
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (mView != null) mView.onNetFailed(msg);
                    }

                    @Override
                    protected boolean isAlive() {
                        return mView != null && !mView.isActivityFinished();
                    }
                });
    }

    public void uploadImg2QiNiu(final int user_id, String file_path, String token) {
        UploadManager uploadManager = new UploadManager();
        //以下api可以修改为图片地址 或者file  或者byte[]//xx/xxx/xx.jpg   1400000000 123.jpg
        uploadManager.put(file_path, new Date().getTime()+file_path.substring(file_path.lastIndexOf("/") + 1)  , token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info != null && info.statusCode == 200) {
                    pullLoadUserInfo(user_id);
                } else if (mView != null) mView.onNetFailed("result error!");
            }
        }, null);

    }

    private void pullLoadUserInfo(int user_id) {
        mModel.getAvatar(user_id).subscribe(new ApiObserver<User>() {
            @Override
            public void onSuccess(User user) {
                if (mView != null)
                    mView.updateInfo(R.string.per_net_token_sucess, user.avatar, null, 0);
                //if (mView != null) mView.onNetSucess(R.string.per_net_token_sucess);
            }

            @Override
            public void onError(String msg) {
                if (mView != null) mView.onNetFailed(msg);
            }
        });


    }

    public void submitInfo(int userId, final String qq, final int birthday) {
        mModel.submitInfo(userId, qq, birthday)
                .subscribe(new ApiObserver<JsonElement>() {
                    @Override
                    public void onSuccess(JsonElement s) {
                        if (mView != null)
                            mView.updateInfo(R.string.per_net_info_suces, null, qq, birthday);
                        // if (mView != null) mView.onNetSucess(R.string.per_net_info_suces);
                    }

                    @Override
                    public void onError(String msg) {
                        if (mView != null) mView.onNetFailed(msg);
                    }

                    @Override
                    protected boolean isAlive() {
                        if (mView != null)
                            return mView.isAlive();
                        return false;
                    }

                });
    }
}

