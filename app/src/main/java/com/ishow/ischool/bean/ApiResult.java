package com.ishow.ischool.bean;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by MrS on 2016/7/18.
 */
public class ApiResult<T> {

    public static final int ERROR_TOKEN1 = 2;
    public static final int ERROR_TOKEN2 = 3;

    public int error_no;

    public String error_msg;

    public JsonElement result;

    public JsonElement getResult() {
        return result;
    }

    public void setResult(JsonElement result) {
        this.result = result;
    }

    public T getResultBean(Class<T> clazz) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(result, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public T getResultBean(Type type) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(result, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public T getResultList() {
        Type type1 = new TypeToken<List<T>>() {
        }.getType();
        Gson gson = new Gson();
        try {
            return gson.fromJson(result, type1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
