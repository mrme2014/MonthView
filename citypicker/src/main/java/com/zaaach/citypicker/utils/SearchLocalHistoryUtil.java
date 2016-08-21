package com.zaaach.citypicker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqf on 16/8/21.
 */
public class SearchLocalHistoryUtil {
    private final static String SP_NAME = "search_history";
    private final static String HISTORY_SIZE_KEY = "city_size";
    private final static String HISTORY_KEY = "city_";

    public static List<String> getCities(Context ctx) {
        SharedPreferences preferences = ctx.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        List<String> list = new ArrayList<String>();
        int size = preferences.getInt(HISTORY_SIZE_KEY, 0);
        for (int i = 0; i < size; i++) {
            list.add(preferences.getString(HISTORY_KEY + i, null));
        }
        return list;
    }

    public static boolean saveCities(Context ctx, List<String> list) {
        SharedPreferences sp = ctx.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(HISTORY_SIZE_KEY, list.size());

        for (int i = 0; i < list.size(); i++) {
            edit.remove(HISTORY_KEY + i);
            edit.putString(HISTORY_KEY + i, list.get(i));
        }
        return edit.commit();
    }
}
