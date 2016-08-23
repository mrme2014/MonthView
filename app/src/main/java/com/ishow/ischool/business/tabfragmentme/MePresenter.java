package com.ishow.ischool.business.tabfragmentme;

import android.text.TextUtils;

import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.google.gson.JsonElement;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.bean.user.Position;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrS on 2016/8/16.
 */
public class MePresenter extends BasePresenter<MeModel, MePresenter.Iview> {


    interface Iview extends BaseView {
        void onNetSucess();

        void onNetFailed(String msg);

        void onChangeSucess();

        void onChageFailed(String msg);
    }

    public void logout() {
        mModel.logout().subscribe(new ApiObserver<JsonElement>() {
            @Override
            public void onSuccess(JsonElement s) {
                mView.onNetSucess();
            }

            @Override
            public void onError(String msg) {
                mView.onNetFailed(msg);
            }
        });
    }

    public void change(int campuse_id, int position_id) {
        mModel.change(campuse_id, position_id).subscribe(new ApiObserver<JsonElement>() {
            @Override
            public void onSuccess(JsonElement s) {
                mView.onChangeSucess();
            }

            @Override
            public void onError(String msg) {
                mView.onChageFailed(msg);
            }
        });
    }

    public Position getSelectPosition(List<Position> position, String selectTxt) {
        if (position == null)
            return null;
        for (int i = 0; i < position.size(); i++) {
            if (TextUtils.equals(selectTxt, position.get(i).title))
                return position.get(i);
        }
        return null;
    }

    public ArrayList<String> getPositionForCampus(List<Campus> campus, int index) {
        if (campus != null)
            return campus.get(index).positions;
        return null;
    }

    public ArrayList<String> getCampusArrayList(List<Campus> campus) {
        ArrayList<String> list = new ArrayList<>();
        if (campus != null)
            for (int i = 0; i < campus.size(); i++) {
                list.add(campus.get(0).name);
            }
        return list;
    }
}


