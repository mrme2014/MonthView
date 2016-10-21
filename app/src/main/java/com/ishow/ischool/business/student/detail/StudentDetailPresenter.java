package com.ishow.ischool.business.student.detail;

import com.commonlib.util.LogUtil;
import com.google.gson.JsonElement;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.student.Student;
import com.ishow.ischool.common.api.ApiObserver;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by abel on 16/8/18.
 */
public class StudentDetailPresenter extends StudentDetailContract.Presenter {
    @Override
    void getStudent(HashMap<String, String> params) {
        mModel.getStudent(params).subscribe(new ApiObserver<Student>() {
            @Override
            public void onSuccess(Student studentInfo) {
                mView.onGetStudentSuccess(studentInfo);
            }

            @Override
            public void onError(String msg) {
                mView.onGetStudentFailed(msg);
            }

            @Override
            protected boolean isAlive() {
                return mView != null && !mView.isActivityFinished();
            }
        });
    }

    @Override
    void editStudent(HashMap<String, String> params) {
        mModel.editStudent(params).subscribe(new ApiObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                mView.onEditStudentSuccess(o);
            }

            @Override
            public void onError(String msg) {
                mView.onEditStudentFailed(msg);
            }

            @Override
            protected boolean isAlive() {
                return mView != null && !mView.isActivityFinished();
            }
        });
    }

    /**
     * 上传 头像 之前先调用获取七牛token的接口
     *
     * @param user_id
     * @param filePath
     */
    public void uploadImg(final int user_id, final String filePath) {
        mModel.getQiNiuToken(user_id)
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
                                mView.onUploadAvatarFailed(e == null ? "result error" : e.getMessage());
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (mView != null) mView.onUploadAvatarFailed(msg);
                    }

                    @Override
                    protected boolean isAlive() {
                        return mView != null && !mView.isActivityFinished();
                    }
                });
    }

    private void uploadImg2QiNiu(final int user_id, String file_path, String token) {
        UploadManager uploadManager = new UploadManager();
        //以下api可以修改为图片地址 或者file  或者byte[]//xx/xxx/xx.jpg
        String key = file_path.substring(file_path.lastIndexOf("/") + 1) + new Date().getTime();
        LogUtil.d("key=" + key + " file_path=" + file_path);
        uploadManager.put(file_path, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info != null && info.statusCode == 200) {
                    getStudentAvatar(user_id);
                } else if (mView != null) {
                    mView.onUploadAvatarFailed("result error!");
                }
            }
        }, null);

    }

    private void getStudentAvatar(int user_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", user_id + "");
        params.put("resources_id", Resource.MARKET_STUDENT_STATISTICS + "");
        params.put("fields", "avatarInfo");
        mModel.getStudent(params).subscribe(new ApiObserver<Student>() {
            @Override
            public void onSuccess(Student user) {
                if (mView != null)
                    mView.onUploadAvatarSuccess(R.string.per_net_token_sucess, user.avatarInfo);
            }

            @Override
            public void onError(String msg) {
                if (mView != null) mView.onUploadAvatarFailed(msg);
            }
        });


    }
}
