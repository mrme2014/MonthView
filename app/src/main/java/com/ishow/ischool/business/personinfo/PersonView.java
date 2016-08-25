package com.ishow.ischool.business.personinfo;

import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.user.Avatar;

/**
 * Created by MrS on 2016/8/15.
 */
public interface PersonView extends BaseView {
    void onNetFailed(String str);
    void updateInfo(int strResId,Avatar avatar,String qq,int birthday);
    boolean isAlive();
}
