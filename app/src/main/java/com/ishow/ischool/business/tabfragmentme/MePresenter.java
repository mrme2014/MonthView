package com.ishow.ischool.business.tabfragmentme;

import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.commonlib.util.LogUtil;
import com.google.gson.JsonElement;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.bean.user.Position;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrS on 2016/8/16.
 */
public class MePresenter extends BasePresenter<MeModel, MePresenter.Iview> {


    interface Iview extends BaseView {
        void onNetSucess();

        void onNetFailed(String msg);

        void onChangeSucess(String selectTxt, Position position);

        void onChageFailed(String msg);

        void showProgressbar(boolean show);
    }

    public void logout() {
        mView.showProgressbar(true);
        mModel.logout().subscribe(new ApiObserver<JsonElement>() {
            @Override
            public void onSuccess(JsonElement s) {
                mView.onNetSucess();
                mView.showProgressbar(false);
            }

            @Override
            public void onError(String msg) {
                mView.onNetFailed(msg);
                mView.showProgressbar(false);
            }
        });
    }

    public void change(final Position selectPosition, final String txt, int campuse_id, int position_id) {
        mView.showProgressbar(true);
        mModel.change(campuse_id, position_id).subscribe(new ApiObserver<JsonElement>() {
            @Override
            public void onSuccess(JsonElement s) {
                mView.onChangeSucess(txt, selectPosition);
                mView.showProgressbar(false);
            }

            @Override
            public void onError(String msg) {
                mView.onChageFailed(msg);
                mView.showProgressbar(false);
            }
        });
    }


    public void switchRole(final FragmentManager fm, final List<Campus> campus, final List<Position> positions) {
        if (campus == null || positions == null) {
            mView.onChageFailed("data null");
            mView.showProgressbar(false);
            return;
        }
        PickerDialogFragment.Builder builder = new PickerDialogFragment.Builder();
        builder.setBackgroundDark(true).setDialogTitle(R.string.switch_role).setDialogType(PickerDialogFragment.PICK_TYPE_OTHERS);
        if (campus != null && campus.size() <= 1)
            builder.setDatas(0, 1, campus.get(0).positions);
        else builder.setDatas(0, 2, getCampusArrayList(campus), campus.get(0).positions);
        PickerDialogFragment fragment = builder.Build();
        fragment.show(fm, "dialog");
        fragment.addMultilinkPickCallback(new PickerDialogFragment.MultilinkPickCallback() {
            @Override
            public ArrayList<String> endSelect(int colum, int selectPosition, String text) {
                //只有一列的返回空
                if (campus != null && campus.size() <= 1)
                    return null;
                //有2列的 但是选中的是第二列 也就是职位列表的 不需要变化
                if (colum == 1)
                    return null;
                //更新 数据源
                return getPositionForCampus(campus, selectPosition);
            }

            @Override
            public void onPickResult(Object object, String... result) {
                Position selectPosition = getSelectPosition(positions, result[result.length - 1]);
                if (selectPosition != null) {
                    change(selectPosition, result[result.length - 1], selectPosition.campus_id, selectPosition.id);
                }
            }
        });
    }

    private Position getSelectPosition(List<Position> position, String selectTxt) {
        if (position == null)
            return null;
        for (int i = 0; i < position.size(); i++) {
            if (TextUtils.equals(selectTxt, position.get(i).title))
                return position.get(i);
        }
        return null;
    }

    private ArrayList<String> getPositionForCampus(List<Campus> campus, int index) {
        if (campus != null)
            return campus.get(index).positions;
        return null;
    }

    private ArrayList<String> getCampusArrayList(List<Campus> campus) {
        ArrayList<String> list = new ArrayList<>();
        if (campus != null)
            for (int i = 0; i < campus.size(); i++) {
                list.add(campus.get(i).name);
            }
        LogUtil.e(list.toString());
        return list;
    }
}


