package com.ishow.ischool.business.communicationadd;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by abel on 16/8/16.
 */
public interface CommunicationAddContract {

    interface View extends BaseView {
        void onAddSuccess();

        void onAddFailed();

    }

    interface Model extends BaseModel {
        ApiObserver<String> addCommunication(HashMap<String, String> params);
    }

    abstract class Presenter extends BasePresenter<Model, View> {

    }
}
