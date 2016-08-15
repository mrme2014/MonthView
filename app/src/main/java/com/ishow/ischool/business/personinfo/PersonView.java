package com.ishow.ischool.business.personinfo;

import com.commonlib.core.BaseView;

/**
 * Created by MrS on 2016/8/15.
 */
public interface PersonView extends BaseView {
    void onNetSucess(int strResId);
    void onNetFailed(int strResId);
}
