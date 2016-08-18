package com.ishow.ischool.business.personinfo;

import com.commonlib.core.BasePresenter;
import com.google.gson.JsonElement;
import com.ishow.ischool.R;
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
public class PersonPresenter extends BasePresenter<PersonMode,PersonView> {
    /**
     * 上传 头像 之前先调用获取七牛token的接口
     * @param filePath
     */
    public void uploadImg(final String filePath){
        mModel.getQiNiuToken()
                .subscribe(new ApiObserver<JsonElement>() {
                    @Override
                    public void onSuccess(JsonElement s) {
                            try {
                                JSONObject object = new JSONObject(s.toString());
                                String token = object.getString("token");
                                uploadImg(filePath,token);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mView.onNetFailed(e==null?"result error":e.getMessage());
                            }
                        }
                    @Override
                    public void onError(String msg) {
                        mView.onNetFailed(msg);
                    }
                });
    }

    public void uploadImg(String file_path,String token){
        UploadManager uploadManager = new UploadManager();
        //以下api可以修改为图片地址 或者file  或者byte[]//xx/xxx/xx.jpg
        uploadManager.put(file_path, file_path.substring(file_path.lastIndexOf("/")+1)+new Date().getTime(), token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                            if (info!=null&& info.statusCode==200){
                        mView.onNetSucess(R.string.per_net_token_sucess);
                    }else  mView.onNetFailed("result error!");
            }
        }, null);

    }

    public void submitInfo(int userId,String qq,int birthday){
        mModel.submitInfo(userId,qq,birthday)
                .subscribe(new ApiObserver<JsonElement>() {
                    @Override
                    public void onSuccess(JsonElement s) {
                        mView.onNetSucess(R.string.per_net_info_suces);
                    }
                    @Override
                    public void onError(String msg) {
                        mView.onNetFailed(msg);
                    }
                });
    }
}

