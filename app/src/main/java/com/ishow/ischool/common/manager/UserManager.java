package com.ishow.ischool.common.manager;

import android.content.Context;

import com.commonlib.util.LogUtil;
import com.commonlib.util.SpUtil;
import com.google.gson.Gson;
import com.ishow.ischool.bean.user.User;


/**
 * Created by kevin on 15/8/7.
 */
public class UserManager {

    private static final String USER_KEY = "user_key";

    private static UserManager userManager;

    private Context context;

    private Class<User> clazz;

    private Gson gson;

    private User user;

    private Object lock = new Object();

    private UserManager() {
        gson = new Gson();
    }

    public static synchronized UserManager getInstance() {
        if (userManager == null) {
            userManager = new UserManager();
        }
        return userManager;
    }

    public void init(Context context) {
        this.context = context;
    }

    /**
     * 保存User到本地，必须保证user 并且 userInfo 不是null
     *
     * @param user
     */
    public void save(User user) {
        if (context == null) {
            throw new RuntimeException();
        }
        if (user == null) {
            LogUtil.d(this, "save user or userinfo is null");
            return;
        }
        this.user = user;
        synchronized (lock) {
            String data = gson.toJson(user);
            persistDate(data);
        }
    }

    public User get() {
        if (context == null) {
            throw new RuntimeException();
        }
        synchronized (lock) {
            if (user == null) {
                String data = readData();
                user = gson.fromJson(data, clazz);
            }
            if (user == null) {
                LogUtil.d(this, "get user is null");
            }
            return user;
        }
    }

    public void clear() {
        synchronized (lock) {
            user = null;
            persistDate("");
        }
    }

    private void persistDate(String data) {
        SpUtil.getInstance(context).setValue(USER_KEY, data);
        user = null;
    }

    private String readData() {
        return SpUtil.getInstance(context).getStringValue(USER_KEY);
    }

}
