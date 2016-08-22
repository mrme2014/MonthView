package com.ishow.ischool.business.student.detail;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.market.CommunicationList;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by abel on 16/8/18.
 */
public interface CommunContract {

    interface View extends BaseView {

        void listCommunicationSuccess(CommunicationList data);

        void listCommunicationFailed(String msg);

    }

    interface Model extends BaseModel {
        Observable<ApiResult<CommunicationList>> listCommunications(HashMap<String, String> params);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void getCommunicationList(HashMap<String, String> params);
    }
}
