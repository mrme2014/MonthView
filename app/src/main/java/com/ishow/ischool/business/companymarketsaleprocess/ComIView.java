package com.ishow.ischool.business.companymarketsaleprocess;

import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.saleprocess.ComMarketProcess;
import com.ishow.ischool.bean.saleprocess.SaleTable1;

/**
 * Created by MrS on 2016/11/8.
 */

public interface ComIView extends BaseView {
    void getListSucess(SaleTable1 table1);
    void getChartSucess(ComMarketProcess process);
    void getListFailed(String msg);
}
