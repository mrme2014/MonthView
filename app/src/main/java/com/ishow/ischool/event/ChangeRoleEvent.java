package com.ishow.ischool.event;

import com.ishow.ischool.bean.user.User;

/**
 * Created by abel on 16/10/13.
 */

public class ChangeRoleEvent {
    public User user;

    public ChangeRoleEvent(User user) {
        this.user = user;
    }
}
