package com.ishow.ischool.common.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.commonlib.core.BaseListActivity;
import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;

import butterknife.ButterKnife;

/**
 * Created by wqf on 16/8/13.
 * 此处T类型是列表item的基础数据类型
 */
public abstract class BaseListActivity4Crm<P extends BasePresenter, M extends BaseModel, T> extends BaseListActivity<P, M, T> {

    protected Snackbar snackbar = null;
    protected ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    public void showToast(String s) {
        if (toolbar != null) {
            snackbar = Snackbar.make(toolbar, s, Snackbar.LENGTH_LONG);
            snackbar.setAction("朕知道了", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        } else Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int stringId) {
        showToast(getString(stringId));
    }

    public void handProgressbar(boolean show) {
        if (show) {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setMessage("request server...");
            }
            dialog.show();
        } else if (!show && dialog != null) dialog.dismiss();
    }
}
