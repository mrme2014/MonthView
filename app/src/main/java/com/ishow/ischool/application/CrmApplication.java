package com.ishow.ischool.application;

import com.commonlib.application.BaseApplication;
import com.ishow.ischool.common.manager.UserManager;

/**
 * Created by MrS on 2016/7/1.
 */

public class CrmApplication extends BaseApplication {


    @Override
    public void onCreate() {
        super.onCreate();

        UserManager.getInstance().init(this);
    }
}
