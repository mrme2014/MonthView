package com.ishow.ischool.presenter;

import com.ishow.ischool.common.api.Api;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.base.presenter.impl.BasePresenter;
import com.ishow.ischool.view.MeView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MrS on 2016/8/12.
 */
public class MeFragmentPresenter extends BasePresenter<MeView> {
    public MeFragmentPresenter(MeView view) {
        super(view);
    }
    public void logout(){
        Api.getUserApi().logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new ApiObserver<String>() {
                    @Override
                    public void onSuccess(String result) {
                        view.logoutSucess();
                    }

                    @Override
                    public void onError(String msg) {
                        view.logutFailed(msg);
                    }
                });
    }
}
