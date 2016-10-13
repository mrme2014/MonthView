package com.ishow.ischool.util;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.SparseArray;

import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.business.login.LoginActivity;
import com.ishow.ischool.common.manager.TokenManager;
import com.ishow.ischool.common.manager.UserManager;
import com.ishow.ischool.fragment.SelectDialogFragment;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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

    public static void reLoginShowToast(Context context) {
        UserManager.getInstance().clear();
        TokenManager.clear();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("invalidate_token", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 获得字符 后两个
     *
     * @param name
     * @return
     */
    public static String getFirstChar(String name) {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        return name.substring(0, 1);
    }

    /**
     * 1 => '晨读/校聊', 2 => '邀约中', 3 => '公开课', 4 => '报名中', 5 => '上课', 6 => '升学中', 7 => '停课'
     *
     * @return
     */
    public static ArrayList<String> getStateList() {
        ArrayList<String> stateList = new ArrayList<>();
        stateList.add("晨读/校聊");
        stateList.add("邀约中");
        stateList.add("公开课");
        stateList.add("报名中");
        stateList.add("上课");
        stateList.add("升学中");
        stateList.add("停课");
        return stateList;
    }

    public static String getStateById(int id) {
        SparseArray<String> stateList = new SparseArray<>();
        stateList.put(1, "晨读/校聊");
        stateList.put(2, "邀约中");
        stateList.put(3, "公开课");
        stateList.put(4, "报名中");
        stateList.put(5, "上课");
        stateList.put(6, "升学中");
        stateList.put(7, "停课");

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
        stateList.add("高");
        stateList.add("中");
        stateList.add("低");

        return stateList;
    }

    public static String getBeliefById(int id) {
        SparseArray<String> stateList = new SparseArray<>();
        stateList.put(1, "高");
        stateList.put(2, "中");
        stateList.put(3, "低");

        return stateList.get(id);
    }

    public static ArrayList<String> getPayState() {
        ArrayList<String> stateList = new ArrayList<>();
        stateList.add("未报名");
        stateList.add("欠款");
        stateList.add("全款");
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
        grades.add("其他");
        return grades;
    }

    public static String getGradeById(int grade) {
        SparseArray<String> grades = new SparseArray<>();
        grades.put(1, "大一");
        grades.put(2, "大二");
        grades.put(3, "大三");
        grades.put(4, "大四");
        grades.put(5, "研究生");
        grades.put(6, "博士");
        grades.put(7, "其他");
        return grades.get(grade);
    }

    public static ArrayList<String> getB10Year() {
        ArrayList<String> years = new ArrayList<>();
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 10; i++) {
            years.add(String.valueOf(curYear - i));
        }
        return years;
    }

    public static long getLastMonthStart() {
        Calendar c = Calendar.getInstance();
        c.roll(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime().getTime() / 1000;
    }

    public static long getLastMonthEnd() {
        Calendar c = Calendar.getInstance();
        c.roll(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime().getTime() / 1000;
    }

    public static long getMonthStart(String year, String month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(year));
        c.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis() / 1000;
    }

    public static long getMonthEnd(String year, String month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(year));
        c.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTimeInMillis() / 1000;
    }

    public static int getYear(long second) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(second * 1000);
        return c.get(Calendar.YEAR);
    }

    public static int getMonth(long second) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(second * 1000);
        return c.get(Calendar.MONTH) + 1;
    }

    public static int getTodayEndMislls() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        int time = (int) (c.getTimeInMillis() / 1000);
        return  time;
    }

    public static int getDayAgoMislls(int dayAgo){  //N天前的 零点  时间戳
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        int time = getTodayEndMislls()-dayAgo*24*3600-hour*3600-min*60-sec ;
        return time ;
    }

    public static ArrayList<String> getSpinnerData() {
        ArrayList<String> list = new ArrayList<>();

        list.add("7天");
        list.add("30天");
        list.add("90天");
        list.add("180天");
        list.add("365天");
        list.add("全部");
        list.add("自定义");

        return list;
    }

    public static boolean hasSalesPermision() {
        User user = UserManager.getInstance().get();
        List<Integer> myResources = user.myResources;
        for (int res : myResources) {
            if (res == Resource.PERMISSION_DATA_SALE_PROCESS || res == Resource.PERMISSION_DATA_CAMPUS || res == Resource.PERMISSION_DATA_OTHER) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasTeachPermision() {
        User user = UserManager.getInstance().get();
        List<Integer> myResources = user.myResources;
        for (int res : myResources) {
            if (res == Resource.PERMISSION_DATA_TEACH_PROCESS || res == Resource.PERMISSION_DATA_TEACH_CAMPUS || res == Resource.PERMISSION_DATA_TEACH_OTHER) {
                return true;
            }
        }
        return false;
    }
}
