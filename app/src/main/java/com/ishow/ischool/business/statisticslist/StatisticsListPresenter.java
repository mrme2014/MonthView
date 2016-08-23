package com.ishow.ischool.business.statisticslist;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.ishow.ischool.bean.student.StudentList;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by wqf on 16/8/14.
 */
public class StatisticsListPresenter extends StatisticsListContract.Presenter {

    @Override
    public void getList4StudentStatistics(int campusId, HashMap<String, String> params, int page) {
        mModel.getList4StudentStatistics(campusId, params, page)
                .subscribe(new ApiObserver<StudentList>() {
                    @Override
                    public void onSuccess(StudentList studentStatisticsList) {
                        mView.getListSuccess(studentStatisticsList);
                    }

                    @Override
                    public void onError(String msg) {
                        mView.getListFail(msg);
                    }
                });
    }

    @Override
    public void showCampusPick(OptionsPickerView opv) {
        final ArrayList<String> campuslist = mModel.getCampus();
        opv.setPicker(campuslist);
//        opv.setTitle("选择校区");
        opv.setCyclic(false);
        opv.setSelectOptions(0);
        opv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                mView.onCampusPicked(campuslist.get(options1));
            }
        });
        opv.show();
    }

    @Override
    public void showStartTimePick(TimePickerView tpv) {
        //控制时间范围
//        Calendar calendar = Calendar.getInstance();
//        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));   //要在setTime 之前才有效果哦
        tpv.setTime(new Date());
        tpv.setCyclic(false);
        tpv.setCancelable(true);
        //时间选择后回调
        tpv.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                mView.onStartTimePicked(date);
            }
        });
        tpv.show();
    }

    @Override
    public void showEndTimePick(TimePickerView tpv) {
        tpv.setTime(new Date());
        tpv.setCyclic(false);
        tpv.setCancelable(true);
        //时间选择后回调
        tpv.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                mView.onEndTimePicked(date);
            }
        });
        tpv.show();
    }

    @Override
    public void showPayStatePick(OptionsPickerView opv) {
        final ArrayList<String> payStates = mModel.getPayStates();
        opv.setPicker(payStates);
//        opv.setTitle("报名情况");
        opv.setCyclic(false);
        opv.setSelectOptions(0);
        opv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                mView.onPayStatePicked(options1, payStates.get(options1));
            }
        });
        opv.show();
    }
}
