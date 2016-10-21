package com.ishow.ischool.business.classattention;

import com.commonlib.core.BasePresenter;
import com.google.gson.JsonElement;
import com.ishow.ischool.bean.classattend.ClazCheckTable;
import com.ishow.ischool.bean.classattend.ClazStudentList;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.TreeMap;

/**
 * Created by MrS on 2016/10/20.
 */

public class ClazPresenter extends BasePresenter<ClazModle,ClazView> {
    public void getStudentList(int claz_id){
        mModel.getStudentList(claz_id).subscribe(new ApiObserver<ClazStudentList>() {
            @Override
            public void onSuccess(ClazStudentList clazStudentList) {
                mView.getResutSucess(clazStudentList);
            }

            @Override
            public void onError(String msg) {
                mView.getResultEorre(msg);
            }
            @Override
            protected boolean isAlive() {
                return mView!=null&&!mView.isActivityFinished();
            }
        });
    }

    public void getCheckInList(TreeMap<String,Integer> map){
        mModel.getCkeckinList(map).subscribe(new ApiObserver<ClazCheckTable>() {
            @Override
            public void onSuccess(ClazCheckTable clazCheckTable) {
                mView.getResutSucess(clazCheckTable);
            }

            @Override
            public void onError(String msg) {
                mView.getResultEorre(msg);
            }
            @Override
            protected boolean isAlive() {
                return mView!=null&&!mView.isActivityFinished();
            }
        });
    }


    public void checkIn(String params,int claz_id,int date_unix){
        mModel.Ckeckin(params,claz_id,date_unix).subscribe(new ApiObserver<JsonElement>() {
            @Override
            public void onSuccess(JsonElement clazCheckTable) {
                mView.CheckInSucess("签到成功");
            }

            @Override
            public void onError(String msg) {
                mView.getResultEorre(msg);
            }

            @Override
            protected boolean isAlive() {
                return mView!=null&&!mView.isActivityFinished();
            }
        });
    }
}
