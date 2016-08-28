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
import com.ishow.ischool.bean.user.PositionInfo;
import com.ishow.ischool.bean.user.User;
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

        void onChangeSucess( User user);

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

            @Override
            protected boolean isAlive() {
                return mView != null && !mView.isActivityFinished();
            }
        });
    }

    public void change(int campuse_id, int position_id) {
        mView.showProgressbar(true);
        mModel.change(campuse_id, position_id).subscribe(new ApiObserver<User>() {
            @Override
            public void onSuccess(User user) {
                mView.onChangeSucess(user);
                mView.showProgressbar(false);
            }

            @Override
            public void onError(String msg) {
                mView.onChageFailed(msg);
                mView.showProgressbar(false);
            }

            @Override
            protected boolean isAlive() {
                return mView != null && !mView.isActivityFinished();
            }
        });
    }


    public void switchRole(final FragmentManager fm, final List<Campus> campus, final List<Position> positions, PositionInfo positionInfo) {
        if (campus == null || positions == null) {
            if (mView != null) mView.onChageFailed("data null");
            if (mView != null) mView.showProgressbar(false);
            return;
        }
        PickerDialogFragment.Builder builder = new PickerDialogFragment.Builder();
        builder.setBackgroundDark(true).setDialogTitle(R.string.switch_role).setDialogType(PickerDialogFragment.PICK_TYPE_OTHERS);

        //当 该账号只有一个校区时  由于不显示校区 而且wheelview要选中 当前角色的那个item  所要遍历positions 拿到哪一个职位的id 与当前已现实的id相符
        final int campusSize = campus.size();
        final int positionsSize = positions.size();
        if (campus != null && campusSize == 1) {
            for (int i = 0; i < positionsSize; i++) {
                //遍历职位列表中的 id 检查 是否与 当前已现实的职位id相符
                if (positions.get(i).id == positionInfo.id) {

                    builder.setDatas(i, 1, campus.get(0).positions);
                    break;
                    //如果遍历完了 都没有相符的 给个默认的了
                } else builder.setDatas(0, 1, campus.get(0).positions);
            }
        } else {
            int defalutCampus = 0;
            int defalutRole = 0;

            for (int i = 0; i < campusSize; i++) {
                if (campus.get(i).id == positionInfo.campusId) {
                    defalutCampus = i;
                    ArrayList<String> campPositions = campus.get(i).positions;
                    for (int j = 0; j < campPositions.size(); j++) {
                        if (TextUtils.equals(campPositions.get(j), positionInfo.title)) {
                            //LogUtil.e(campus.get(i).id + "---" + positionInfo.campusId + "-----" + campPositions.get(j) + "----" + positionInfo.title);
                            defalutRole = j;
                            break;
                        }
                    }
                    break;
                }
            }
            //LogUtil.e(defalutCampus + "---" + defalutRole);
            builder.setDatas(0, 2, getCampusArrayList(campus), campus.get(defalutCampus).positions).setDefalut(defalutCampus, defalutRole);
        }

        PickerDialogFragment fragment = builder.Build();
        //fragment.show(fm, "dialog");
        fragment.show(fm);
        fragment.addMultilinkPickCallback(new PickerDialogFragment.MultilinkPickCallback<int[]>() {
            @Override
            public ArrayList<String> endSelect(int colum, int selectPosition, String text) {
                //只有一列的返回空
                if (campus != null && campusSize <= 1)
                    return null;
                //有2列的 但是选中的是第二列 也就是职位列表的 不需要变化
                if (colum == 1)
                    return null;
                //更新 数据源
                return getPositionForCampus(campus, selectPosition);
            }

            @Override
            public void onPickResult(int[] integers, String... result) {
                Position selectPosition = getSelectPosition(campus, positions, integers[0], result[result.length - 1]);
                if (selectPosition != null) {
                    change(selectPosition.campus_id, selectPosition.id);
                }
            }
        });
    }

    private String getSelectCampus(List<Campus> campus, int selectCmapus) {
        if (campus == null)
            return null;
        Campus campus1 = null;
        if (campus.size() == 1) campus1 = campus.get(0);
        else campus1 = campus.get(selectCmapus);
        return campus1 == null ? null : campus1.name;
    }

    private Position getSelectPosition(List<Campus> campus, List<Position> position, int selectCmapus, String selectPos) {
        if (position == null || campus == null)
            return null;
        Campus campus1 = null;
        if (campus.size() == 1) campus1 = campus.get(0);
        else campus1 = campus.get(selectCmapus);
        for (int i = 0; i < position.size(); i++) {

            if (campus1.id == position.get(i).campus_id && TextUtils.equals(position.get(i).title,selectPos)){
                LogUtil.e(campus1.id+"--"+ position.get(i).campus_id+"--"+position.get(i).title+"---"+selectPos);
                return position.get(i);
            }

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


