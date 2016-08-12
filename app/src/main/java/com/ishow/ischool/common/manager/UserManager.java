package com.ishow.ischool.common.manager;

import com.ishow.ischool.bean.user.User;

/**
 * Created by abel on 16/8/11.
 */
public class UserManager {

    public static UserManager instance;
    private User user;

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;

    }
}
