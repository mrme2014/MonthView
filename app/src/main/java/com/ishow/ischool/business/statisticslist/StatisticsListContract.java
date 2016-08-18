package com.ishow.ischool.business.statisticslist;


import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.student.StudentStatisticsList;

import java.util.ArrayList;
import java.util.Date;

import rx.Observable;


/**
 * Created by wqf on 16/8/14.
 */
public interface StatisticsListContract {
    interface Model extends BaseModel {
        Observable<ApiResult<StudentStatisticsList>> getList4StudentStatistics(int campusId);
        ArrayList<String> getCampus();
        ArrayList<String> getPayStates();
    }

    interface View extends BaseView {
        void getListSuccess(StudentStatisticsList studentStatisticsList);
        void getListFail(String msg);
        void onCampusPicked(String picked);
        void onStartTimePicked(Date picked);
        void onEndTimePicked(Date picked);
        void onPayStatePicked(String picked);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getList4StudentStatistics(int campusId);
        public abstract void showCampusPick(OptionsPickerView optionsPickerView);
        public abstract void showStartTimePick(TimePickerView timePickerView);
        public abstract void showEndTimePick(TimePickerView timePickerView);
        public abstract void showPayStatePick(OptionsPickerView optionsPickerView);
    }
}
