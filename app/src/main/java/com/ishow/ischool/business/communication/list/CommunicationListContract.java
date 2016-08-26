package com.ishow.ischool.business.communication.list;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.market.CommunicationList;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by abel on 16/8/15.
 */
public interface CommunicationListContract {

    interface View extends BaseView {
        void listCommunicationSuccess(CommunicationList data);

        void listCommunicationFailed(String msg);

        boolean isAlive();
    }

    interface Model extends BaseModel {
        Observable<ApiResult<CommunicationList>> listCommunications(HashMap<String, String> params, int page);
    }

    abstract class Presenter extends BasePresenter<Model, View> {

    }
}
