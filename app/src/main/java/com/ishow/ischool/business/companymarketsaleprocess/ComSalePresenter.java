package com.ishow.ischool.business.companymarketsaleprocess;

import com.commonlib.core.BasePresenter;
import com.ishow.ischool.bean.saleprocess.ComMarketProcess;
import com.ishow.ischool.bean.saleprocess.SaleTable1;
import com.ishow.ischool.common.api.ApiObserver;

/**
 * Created by MrS on 2016/11/8.
 */

public class ComSalePresenter extends BasePresenter<ComModel,ComIView> {

    public void getComMarketSaleprocessTableData(int begin_time, int end_time) {
        mModel.getComMarketSaleprocessTableData(begin_time,end_time).subscribe(new ApiObserver<SaleTable1>() {
            @Override
            public void onSuccess(SaleTable1 saleTable) {
                mView.getListSucess(saleTable);
            }

            @Override
            public void onError(String msg) {
                mView.getListFailed(msg);
            }
        });
    }



    public void getComMarketSaleprocesssChart(int begin_time, int end_time) {
        mModel.getComMarketProcesschart(begin_time,end_time).subscribe(new ApiObserver<ComMarketProcess>() {
            @Override
            public void onSuccess(ComMarketProcess process) {
                mView.getChartSucess(process);
            }

            @Override
            public void onError(String msg) {
                mView.getListFailed(msg);
            }
        });
    }
}
