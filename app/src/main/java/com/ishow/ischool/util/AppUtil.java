package com.ishow.ischool.util;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.SparseArray;

import com.ishow.ischool.R;
import com.ishow.ischool.business.login.LoginActivity;
import com.ishow.ischool.common.manager.TokenManager;
import com.ishow.ischool.common.manager.UserManager;
import com.ishow.ischool.widget.custom.SelectDialogFragment;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by abel on 16/8/16.
 */
public class AppUtil {
    /**
     * 重新登录
     *
     * @param context
     */
    public static void reLogin(Context context) {
        UserManager.getInstance().clear();
        TokenManager.clear();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 获得字符 后两个
     *
     * @param name
     * @return
     */
    public static String getLast2Text(String name) {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        int len = name.length();
        return len > 2 ? name.substring(len - 2, len) : name;
    }

    public static ArrayList<String> getStateList() {
        ArrayList<String> stateList = new ArrayList<>();
        stateList.add("晨读");
        stateList.add("公开课");
        stateList.add("报名中");
        stateList.add("升学（中）");
        stateList.add("升学（高）");
        stateList.add("升学（影）");
        stateList.add("其它");
        return stateList;
    }

    public static String getStateById(int id) {
        SparseArray<String> stateList = new SparseArray<>();
        stateList.put(1, "晨读");
        stateList.put(2, "公开课");
        stateList.put(3, "报名中");
        stateList.put(4, "升学（中）");
        stateList.put(5, "升学（高）");
        stateList.put(6, "升学（影）");
        stateList.put(7, "其它");

        return stateList.get(id);
    }

    public static ArrayList<String> getRefuseList() {
        ArrayList<String> stateList = new ArrayList<>();
        stateList.add("钱");
        stateList.add("时间");
        stateList.add("距离");
        stateList.add("英语重要性");
        stateList.add("学习方法");
        stateList.add("自学");
        stateList.add("父母不同意");
        stateList.add("其它");
        stateList.add("无");

        return stateList;
    }

    public static String getRefuseById(int id) {
        SparseArray<String> stateList = new SparseArray<>();
        stateList.put(1, "钱");
        stateList.put(2, "时间");
        stateList.put(3, "距离");
        stateList.put(4, "英语重要性");
        stateList.put(5, "学习方法");
        stateList.put(6, "自学");
        stateList.put(7, "父母不同意");
        stateList.put(8, "其它");
        stateList.put(9, "无");

        return stateList.get(id);
    }

    public static ArrayList<String> getBeliefList() {
        ArrayList<String> stateList = new ArrayList<>();
        stateList.add("低");
        stateList.add("中");
        stateList.add("高");

        return stateList;
    }

    public static String getBeliefById(int id) {
        SparseArray<String> stateList = new SparseArray<>();
        stateList.put(1, "低");
        stateList.put(2, "中");
        stateList.put(3, "高");

        return stateList.get(id);
    }

    public static ArrayList<String> getPayState() {
        ArrayList<String> stateList = new ArrayList<>();
        stateList.add("未报名");
        stateList.add("欠款");
        stateList.add("已报名");
        stateList.add("退款");

        return stateList;
    }


    public static ArrayList<String> getFilterTimeType() {
        ArrayList<String> timeTypeList = new ArrayList<>();
        timeTypeList.add("登记时间");
        timeTypeList.add("上课时间");
        return timeTypeList;
    }

    public static HashMap<String, String> getParamsHashMap(int resourcesId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("resources_id", String.valueOf(resourcesId));
        return params;
    }

    public static PickerDialogFragment showTimePickerDialog(FragmentManager fragmentManager, PickerDialogFragment.Callback callback) {
        return showTimePickerDialog(fragmentManager, R.string.choose_date, callback);
    }

    public static PickerDialogFragment showTimePickerDialog(FragmentManager fragmentManager, int title, PickerDialogFragment.Callback callback) {

        PickerDialogFragment.Builder builder1 = new PickerDialogFragment.Builder();
        builder1.setBackgroundDark(true).setDialogType(PickerDialogFragment.PICK_TYPE_DATE).setDialogTitle(title);
        PickerDialogFragment dialogFragment = builder1.Build();
        dialogFragment.show(fragmentManager, "dialog");
        dialogFragment.addCallback(callback);
        return dialogFragment;
    }

    public static SelectDialogFragment showItemDialog(FragmentManager fragmentManager, ArrayList<String> stateList,
                                                      SelectDialogFragment.OnItemSelectedListner onItemselectListner) {
        SelectDialogFragment.Builder builder = new SelectDialogFragment.Builder();
        SelectDialogFragment dialog = builder.setMessage(stateList).setOnItemselectListner(onItemselectListner)
                .Build();
        dialog.show(fragmentManager);
        return dialog;
    }

    /**
     * @return
     */
    public static ArrayList<String> getGradeList() {
        ArrayList<String> grades = new ArrayList<>();
        grades.add("大一");
        grades.add("大二");
        grades.add("大三");
        grades.add("大四");
        grades.add("研究生");
        grades.add("博士");
        return grades;
    }
}
