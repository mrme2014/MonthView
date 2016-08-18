package com.ishow.ischool.business.statisticslist;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.student.StudentStatisticsList;
import com.ishow.ischool.common.api.MarketApi;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/8/14.
 */
public class StatisticsListModel implements StatisticsListContract.Model {
    public Observable<ApiResult<StudentStatisticsList>> getList4StudentStatistics(int campusId) {
        return ApiFactory.getInstance().getApi(MarketApi.class).listStudentStatistics(campusId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public ArrayList<String> getCampus() {
        ArrayList<String> campuslist = new ArrayList<>();
        campuslist.add("1");
        campuslist.add("2");
        campuslist.add("3");
        campuslist.add("4");
        campuslist.add("5");
        campuslist.add("6");
        campuslist.add("7");
        return campuslist;
    }

    @Override
    public ArrayList<String> getPayStates() {
        ArrayList<String> paylist = new ArrayList<>();
        paylist.add("未报名");
        paylist.add("欠款");
        paylist.add("已报名");
        paylist.add("退款");
        return paylist;
    }
}
