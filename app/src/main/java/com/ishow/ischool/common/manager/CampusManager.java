package com.ishow.ischool.common.manager;

import android.content.Context;

import com.commonlib.util.LogUtil;
import com.commonlib.util.SpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ishow.ischool.bean.system.CampusInfo;

import java.util.ArrayList;

/**
 * Created by wqf on 16/8/22.
 */
public class CampusManager {

    private static final String CAMPUS_KEY = "campus_key";
    private static CampusManager campusManager;

    private Context context;

    private Gson gson;
    private ArrayList<CampusInfo> campusInfos;

    public static synchronized CampusManager getInstance() {
        if (campusManager == null) {
            campusManager = new CampusManager();
        }
        return campusManager;
    }

    public void init(Context context) {
        this.context = context;
    }

    /**
     * 保存所有校区信息到本地
     */
    public void save(ArrayList<CampusInfo> campusInfos) {
        if (context == null) {
            throw new RuntimeException();
        }
        this.campusInfos = campusInfos;
        String data = gson.toJson(campusInfos);
        persistDate(data);
    }

    public ArrayList<CampusInfo> get() {
        if (context == null) {
            throw new RuntimeException();
        }
        if (campusInfos == null) {
            String data = readData();
            campusInfos = gson.fromJson(data, new TypeToken<ArrayList<CampusInfo>>(){}.getType());
        }
        if (campusInfos == null) {
            LogUtil.d(this, "get campus is null");
        }
        return campusInfos;
    }

    public ArrayList<String> getCampusNames() {
        if (context == null) {
            throw new RuntimeException();
        }
        ArrayList<String> campusNames = new ArrayList<>();
        for (CampusInfo campusInfo : get()) {
            if (!campusInfo.name.equals("总部") && !campusInfo.name.equals("第三校区")) {
                campusNames.add(campusInfo.name);
            }
        }
        return campusNames;
    }

    public String getCampusNameById(int campusId) {
        for (CampusInfo campusInfo : get()) {
            if (campusInfo.id == campusId) {
                return campusInfo.name;
            }
        }
        return "";
    }

    private void persistDate(String data) {
        LogUtil.d("campusInfos" + data);
        SpUtil.getInstance(context).setValue(CAMPUS_KEY, data);
//        campusInfos = null;
    }

    private String readData() {
        return SpUtil.getInstance(context).getStringValue(CAMPUS_KEY);
    }
}
