package com.ishow.ischool.business.communicationadd;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by abel on 16/8/16.
 */
public interface CommunicationAddContract {

    interface View extends BaseView {
        void onAddSuccess();

        void onAddFailed(String msg);

    }

    interface Model extends BaseModel {
        Observable<ApiResult<String>> addCommunication(HashMap<String, String> params);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void addCommunication(CommunicationAddActivity.CommunicationForm form);
    }
}
