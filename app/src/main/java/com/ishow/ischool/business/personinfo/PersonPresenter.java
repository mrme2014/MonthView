package com.ishow.ischool.business.personinfo;

import com.commonlib.core.BasePresenter;
import com.ishow.ischool.R;
import com.ishow.ischool.common.api.ApiObserver;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MrS on 2016/8/15.
 */
public class PersonPresenter extends BasePresenter<PersonMode,PersonView> {
    /**
     * 上传 头像 之前先调用获取七牛token的接口
     * @param filePath
     */
    public void uploadImg(final String filePath){
        mModel.getQiNiuToken()
                .subscribe(new ApiObserver<String>() {
                    @Override
                    public void onSuccess(String s) {
                            try {
                                JSONObject object = new JSONObject(s);
                                String token = object.getString("token");
                                uploadImg(filePath,token);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mView.onNetFailed(R.string.per_net_token_failed);
                            }
                        }
                    @Override
                    public void onError(String msg) {
                        mView.onNetFailed(R.string.per_net_token_failed);
                    }
                });
    }

    public void uploadImg(String file_path,String token){
        UploadManager uploadManager = new UploadManager();
        //以下api可以修改为图片地址 或者file  或者byte[]//xx/xxx/xx.jpg
        uploadManager.put(file_path, file_path.substring(file_path.lastIndexOf("/")+1), token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                try {
                    if (response.getString("sucess") == "true"){
                        mView.onNetSucess(R.string.per_net_token_sucess);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mView.onNetFailed(R.string.per_net_token_failed);
                }
            }
        }, null);

    }

    public void submitInfo(int userId,String qq,int birthday){
        mModel.submitInfo(userId,qq,birthday)
                .subscribe(new ApiObserver<String>() {
                    @Override
                    public void onSuccess(String s) {
                        mView.onNetSucess(R.string.per_net_info_suces);
                    }
                    @Override
                    public void onError(String msg) {
                        mView.onNetSucess(R.string.per_net_info_failed);
                    }
                });
    }
}

