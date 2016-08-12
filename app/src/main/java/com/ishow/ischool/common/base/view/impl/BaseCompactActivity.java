package com.ishow.ischool.common.base.view.impl;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.commonlib.application.ActivityStackManager;
import com.ishow.ischool.R;
import com.ishow.ischool.common.base.presenter.impl.BasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by MrS on 2016/7/20.
 */
public abstract class BaseCompactActivity<P extends BasePresenter> extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    public P mPresenter;

    private static final int PERMISSION_CODE = 100;//权限申请默认的request_code
    private String permission;//当前正在申请的那一条权限

    protected Toolbar mToolbar;
    protected TextView mToolbarTitle;
    public static final int MODE_NONE = -1;      // 空
    public static final int MODE_BACK = 0;      // 左侧返回键
    public static final int MODE_DRAWER = 1;
    public static final int MODE_HOME = 2;      //

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initEnv();
        setUpContentView();

        unbinder = ButterKnife.bind(this);
        mPresenter = bindPresenter();

        setUpView();
        setUpData();

        ActivityStackManager.getInstance().pushActivity(this);
    }

    /**
     * 初始化 环境，intent数据
     */
    protected void initEnv() {

    }

    protected abstract void setUpContentView();

    protected abstract void setUpView();

    protected abstract void setUpData();


    @Override
    public void setContentView(int layoutResID) {
        setContentView(layoutResID, -1, -1, MODE_NONE);
    }

    public void setContentView(int layoutResID, int titleResId) {
        setContentView(layoutResID, titleResId, -1, MODE_BACK);
    }

    public void setContentView(int layoutResID, String titleStr) {
        setContentView(layoutResID, titleStr, -1, MODE_BACK);
    }

    public void setContentView(int layoutResID, int titleResId, int mode) {
        setContentView(layoutResID, titleResId, -1, mode);
    }

    public void setContentView(int layoutResID, int titleResId, int menuId, int mode) {
        super.setContentView(layoutResID);
        setUpToolbar(titleResId, menuId, mode);
    }

    public void setContentView(int layoutResID, String titleStr, int menuId, int mode) {
        super.setContentView(layoutResID);
        setUpToolbar(titleStr, menuId, mode);
    }

    protected void setUpToolbar(int titleResId, int menuId, int mode) {
        if (mode != MODE_NONE) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            mToolbar.setTitle("");
            mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);

            if (mode == MODE_BACK) {
                mToolbar.setNavigationIcon(R.drawable.bg_nav_back);
            }
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigationBtnClicked();
                }
            });

            setUpTitle(titleResId);
            setUpMenu(menuId);
        }
    }

    protected void setUpToolbar(String titleStr, int menuId, int mode) {
        if (mode != MODE_NONE) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            mToolbar.setTitle("");
            mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);

            if (mode == MODE_BACK) {
                mToolbar.setNavigationIcon(R.mipmap.nav_back_normal);
            }
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigationBtnClicked();
                }
            });

            setUpTitle(titleStr);
            setUpMenu(menuId);
        }
    }

    protected void setUpMenu(int menuId) {
        if (mToolbar != null) {
            mToolbar.getMenu().clear();
            if (menuId > 0) {
                mToolbar.inflateMenu(menuId);
                mToolbar.setOnMenuItemClickListener(this);
            }
        }
    }

    protected void setUpTitle(int titleResId) {
        if (titleResId > 0 && mToolbarTitle != null) {
            mToolbarTitle.setText(titleResId);
        }
    }

    protected void setUpTitle(String titleStr) {
        if (!titleStr.equals("") && mToolbarTitle != null) {
            mToolbarTitle.setText(titleStr);
        }
    }

    protected void onNavigationBtnClicked() {
        finish();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    public abstract P bindPresenter();

    public P getPresenter() {
        return mPresenter;
    }

    Snackbar snackbar = null;

    public void showToast(String s) {
        if (mToolbar != null) {
            snackbar = Snackbar.make(mToolbar, s, Snackbar.LENGTH_LONG);
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

    public void startActivity(Class claz) {
        Intent intent = new Intent(this, claz);
        startActivity(intent);
        this.finish();
    }

    public void startActivityOnly(Class claz) {
        Intent intent = new Intent(this, claz);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getPresenter() != null) {
            getPresenter().destroy();
        }
        unbinder.unbind();
        ActivityStackManager.getInstance().popActivity(this);
    }

    public void checkPermission(String permission, checkPermissionListner l) {
        this.permission = permission;
        this.listner = l;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_CODE);
        }
    }


    public interface checkPermissionListner {
        void PERMISSION_GRANTED();

        void PERMISSION_DENIED();
    }

    checkPermissionListner listner;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && permissions[0].equals(permission) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (listner != null)
                listner.PERMISSION_GRANTED();
        } else {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                String tips = null;
                if (TextUtils.equals(permission, Manifest.permission_group.PHONE))
                    tips = getString(R.string.permisson_quanxian_call);
                else if (TextUtils.equals(permission, Manifest.permission_group.STORAGE))
                    tips = getString(R.string.permisson_quanxian_call);
                else if (TextUtils.equals(permission, Manifest.permission_group.CONTACTS))
                    tips = getString(R.string.permisson_quanxian_connact);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage(tips)
                        .setPositiveButton(getString(R.string.permisson_quanxian_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (listner != null)
                                    listner.PERMISSION_DENIED();
                            }
                        }).create();
                dialog.show();
            }
        }
    }

    public void showSoftWindow(View view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

    public void hideSoftWindow(View view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public boolean isActivityFinished() {
        if (Build.VERSION.SDK_INT >= 17) {
            return this == null || isFinishing() || isDestroyed();
        } else {
            return this == null || isFinishing();
        }
    }

}
