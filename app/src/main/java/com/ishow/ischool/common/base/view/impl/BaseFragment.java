package com.ishow.ischool.common.base.view.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ishow.ischool.application.CrmApplication;
import com.ishow.ischool.common.base.presenter.impl.BasePresenter;
import com.ishow.ischool.common.base.view.IView;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;

/**
 * Created by MrS on 2016/7/11.
 */
public abstract class BaseFragment<V extends IView, P extends BasePresenter> extends Fragment implements IView {
    public View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getResId(), null);
        ButterKnife.bind(this, rootView);

        presenter = bindPresenter();
        if (presenter != null) {
            view = bindView();
            presenter.attachView(view);
        }

        onCreateView();

        return rootView;
    }

    public abstract int getResId();

    public abstract void onCreateView();

    private P presenter;
    private V view;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public abstract P bindPresenter();

    public abstract V bindView();

    public P getPresenter() {
        return presenter;
    }

    Snackbar snackbar = null;

    public void showToast(String s) {
        if (rootView != null) {
            snackbar = Snackbar.make(rootView, s, Snackbar.LENGTH_LONG);
            snackbar.setAction("朕知道了", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        } else Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int stringId) {
        showToast(getString(stringId));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.dettachView();
            presenter = null;
        }
        ButterKnife.unbind(this);

        RefWatcher refWatcher = CrmApplication.getRefWatcher();
        refWatcher.watch(this);
    }


}
