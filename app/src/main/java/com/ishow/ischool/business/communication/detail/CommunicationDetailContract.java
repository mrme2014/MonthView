package com.ishow.ischool.business.communication.detail;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by wqf on 16/9/1.
 */
public interface CommunicationDetailContract {
    interface View extends BaseView {
        void onEditCommunicationFailed(String msg);
        void onEditCommunicationSucceed(HashMap<String, String> params);
    }

    interface Model extends BaseModel {
        Observable<ApiResult<Object>> editCommunication(HashMap<String, String> params);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void editCommunication(HashMap<String, String> params);
    }
}
