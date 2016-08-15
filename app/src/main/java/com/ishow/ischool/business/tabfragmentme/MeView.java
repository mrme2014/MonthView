package com.ishow.ischool.business.tabfragmentme;

import com.ishow.ischool.common.base.view.IView;

/**
 * Created by MrS on 2016/8/12.
 */
public interface MeView extends IView{
    public void logoutSucess();

    public void logutFailed(String msg);
}
