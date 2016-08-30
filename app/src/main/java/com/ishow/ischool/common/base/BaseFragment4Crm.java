package com.ishow.ischool.common.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.commonlib.core.BaseFragment4mvp;
import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.ishow.ischool.application.CrmApplication;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mini on 16/8/14.
 */
public abstract class BaseFragment4Crm<P extends BasePresenter, M extends BaseModel> extends BaseFragment4mvp<P, M> {

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), null);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    public abstract int getLayoutId();

    @Override
    public void onDestroyView() {
        RefWatcher refWatcher = CrmApplication.getRefWatcher();
        refWatcher.watch(this);
        unbinder.unbind();
        super.onDestroyView();
    }

    Snackbar snackbar = null;

    public void showToast(String s) {
        if (getView() != null) {
            snackbar = Snackbar.make(getView(), s, Snackbar.LENGTH_LONG);
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

    private ProgressDialog dialog;

    public void handProgressbar(boolean show) {
        if (show) {
            if (dialog == null) {
                dialog = new ProgressDialog(getContext());
                dialog.setMessage("Loading...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
            }
            dialog.show();
        } else if (!show && dialog != null) dialog.dismiss();
    }
}
