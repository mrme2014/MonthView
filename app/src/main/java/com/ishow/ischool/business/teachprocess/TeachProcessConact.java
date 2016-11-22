package com.ishow.ischool.business.teachprocess;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.teachprocess.TeachProcess;

import java.util.TreeMap;

import rx.Observable;

/**
 * Created by MrS on 2016/10/9.
 */

public interface TeachProcessConact {
    interface Model extends BaseModel {
        /**
         * start_time	Int	0			开始时间	0
         * end_time	Int	0			结束时间	0
         * position_id	Int	0			职位id	0
         * user_id	Int	0			用户id	0
         *
         * @param map
         * @return
         */
        Observable<ApiResult<TeachProcess>> getTeachProcessData4Home(TreeMap<String, Integer> map);

        Observable<ApiResult<TeachProcess>> getTeachProcessData4Self(TreeMap<String, Integer> map);
    }

    interface View extends BaseView {
        public void getListSucess(TeachProcess process);

        public void getListFaild(String error);
    }

    abstract class Presenter extends BasePresenter<TeachModel, View> {
        public abstract void getTeachProcessData4Home(TreeMap<String, Integer> map);

        public abstract void getTeachProcessData(TreeMap<String, Integer> map);
    }
}
