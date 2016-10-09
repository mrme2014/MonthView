package com.ishow.ischool.business.statistic.other;

import com.commonlib.core.BasePresenter;
import com.commonlib.util.LogUtil;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.statistics.OtherStatisticsTable;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by abel on 16/9/19.
 */
public class OtherPresenter extends BasePresenter<OtherModel, OtherStatisticActivity> {
    public void getOtherStatistics(HashMap<String, String> params, boolean isTeachData) {
        params.put("resources_id", isTeachData ? Resource.PERMISSION_DATA_TEACH_OTHER + "" : Resource.PERMISSION_DATA_OTHER + "");
        mModel.getOtherStatistics(params).subscribe(new ApiObserver<OtherStatisticsTable>() {
            @Override
            public void onSuccess(OtherStatisticsTable otherStatisticsTable) {
                mView.onGetSuccess(otherStatisticsTable);
                LogUtil.d("----mView.onGetSuccess(otherStatisticsTable)");
            }

            @Override
            public void onError(String msg) {
                mView.onGetFailed(msg);
            }
        });
    }
}
