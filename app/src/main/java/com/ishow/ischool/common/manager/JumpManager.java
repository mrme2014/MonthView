package com.ishow.ischool.common.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.student.StudentRelationInfo;
import com.ishow.ischool.bean.user.User;
import com.zaaach.citypicker.utils.ToastUtils;

import java.util.List;

/**
 * Created by abel on 16/8/12.
 */
public class JumpManager {

    private List<Integer> myResources;

    public static void jumpActivity(Context from, Class to, int permission) {
        if (!checkUserPermision(from, permission))
            return;

        Intent intent = new Intent(from, to);
        from.startActivity(intent);
    }

    public static void jumpActivity(Context from, Intent intent, int permission) {
        if (!checkUserPermision(from, permission)) {
            return;
        }
        from.startActivity(intent);
    }


    public static boolean checkUserPermision(Context context, int permission) {
        return checkUserPermision(context, permission, true);
    }

    /**
     * 权限检查
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkUserPermision(Context context, int permission, boolean showToast) {
        if (permission == Resource.NO_NEED_CHECK)
            return true;
        List<Integer> resurces = UserManager.getInstance().getResurces();
        if (resurces == null) {
            if (showToast) {
                ToastUtils.showToast(context, R.string.no_permission);
            }
            return false;
        }
        for (int i = 0; i < resurces.size(); i++) {
            if (resurces.get(i) == permission)
                return true;
        }
        if (showToast) {
            ToastUtils.showToast(context, R.string.no_permission);
        }
        return false;
    }


    public static boolean checkRelationPermision(Context context, StudentRelationInfo relation) {
        return checkRelationPermision(context, relation, false);
    }

    /**
     * 相关的人才能操作
     *
     * @param context
     * @param relation
     * @return
     */
    public static boolean checkRelationPermision(Context context, StudentRelationInfo relation, boolean showToast) {
        if (relation == null) {
            if (showToast) {
                ToastUtils.showToast(context, R.string.no_permission);
            }
            return false;
        }
        User user = UserManager.getInstance().get();
        int uid = user.userInfo.user_id;
        if (uid == relation.advisor_id || uid == relation.referrer_id || uid == relation.campus_manager_id
                || uid == relation.charge_id || uid == relation.saler_id || uid == relation.student_id
                || uid == relation.guider_id || uid == relation.school_chat_attache_id || uid == relation.school_chat_charge_id) {
            return true;
        }
        if (showToast) {
            ToastUtils.showToast(context, R.string.no_permission);
        }
        return false;
    }


    public static void jumpActivityForResult(Activity from, Class to, int requestCode, int permission) {
        if (!checkUserPermision(from, permission)) {
            return;
        }
        Intent intent = new Intent(from, to);
        from.startActivityForResult(intent, requestCode);
    }

    public static void jumpActivityForResult(Activity from, Intent intent, int requestCode, int permission) {
        if (!checkUserPermision(from, permission)) {
            return;
        }
        from.startActivityForResult(intent, requestCode);
    }


    public static void jumpActivityForResult(Fragment from, Intent intent, int requestCode, int permission) {
        if (!checkUserPermision(from.getActivity(), permission)) {
            return;
        }
        from.startActivityForResult(intent, requestCode);
    }

}
