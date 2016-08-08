package com.ishow.ischool.common.base.presenter;


import com.ishow.ischool.common.base.view.IView;

/**
 * Created by MrS on 2016/7/12.
 */
public interface IPresenter<V extends IView> {
    //绑定视图层
    public void attachView(V view);

    //取消绑定
    public void dettachView();
}
