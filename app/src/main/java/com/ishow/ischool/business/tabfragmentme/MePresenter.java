package com.ishow.ischool.business.tabfragmentme;

import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.google.gson.JsonElement;
import com.ishow.ischool.common.api.ApiObserver;

/**
 * Created by MrS on 2016/8/16.
 */
public class MePresenter extends BasePresenter<MeModel,MePresenter.Iview>{
   public void  logout(){
       mModel.logout().subscribe(new ApiObserver<JsonElement>() {
                   @Override
                   public void onSuccess(JsonElement s) {
                       mView.onNetSucess();
                   }

                   @Override
                   public void onError(String msg) {
                       mView.onNetFailed(msg);
                   }
               });
   }

    interface Iview extends BaseView{
        void onNetSucess();
        void onNetFailed(String msg);
    }
}