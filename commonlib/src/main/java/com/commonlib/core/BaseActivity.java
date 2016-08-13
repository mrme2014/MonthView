package com.commonlib.core;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.commonlib.R;
import com.commonlib.application.ActivityStackManager;
import com.commonlib.core.util.GenericUtil;


/**
 * Created by wqf on 16/4/28.
 * 宗旨：纯粹界面操作交互，不需要MP参与的行为，尽量V自己做，保证MVP职责清晰，P只有干净简洁的协助VM的业务逻辑操作，M只处理数据操作。
 */
public abstract class BaseActivity<P extends BasePresenter, M extends BaseModel> extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    protected Toolbar toolbar;
    protected TextView toolbar_title;
    public static final int MODE_BACK = 0;      // 左侧返回键
    public static final int MODE_DRAWER = 1;
    public static final int MODE_NONE = 2;      // 空

    public P mPresenter;
    public M mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initEnv();
        setUpContentView();
        mPresenter = GenericUtil.getType(this, 0);
        mModel = GenericUtil.getType(this, 1);
        if (this instanceof BaseView) {
            mPresenter.setVM(this, mModel);
        }
        setUpView();
        setUpData();

        ActivityStackManager.getInstance().pushActivity(this);
    }

    /**
     * 初始化 环境，intent数据
     */
    protected void initEnv() {}

    protected abstract void setUpContentView();

    protected abstract void setUpView();

    protected abstract void setUpData();


    /**
     * 默认带有返回图标
     * @param layoutResID
     */
    @Override
    public void setContentView(int layoutResID) {
        setContentView(layoutResID, -1, -1, MODE_BACK);
    }

    /**
     * @param layoutResID
     * @param titleResId    标题（ResourceId）
     */
    public void setContentView(int layoutResID, int titleResId) {
        setContentView(layoutResID, titleResId, -1, MODE_BACK);
    }

    /**
     * @param layoutResID
     * @param titleStr      标题（字符串）
     */
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
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("");
            toolbar_title = (TextView) findViewById(R.id.toolbar_title);

            if (mode == MODE_BACK) {
//                toolbar.setNavigationIcon(R.drawable.bg_nav_back);
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("");
            toolbar_title = (TextView) findViewById(R.id.toolbar_title);

            if (mode == MODE_BACK) {
//                toolbar.setNavigationIcon(R.drawable.ic_back);
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigationBtnClicked();
                }
            });

            setUpTitle(titleStr);
            setUpMenu(menuId);
        }
    }

    /**
     * @param menuId
     */
    protected void setUpMenu(int menuId) {
        if (toolbar != null) {
            toolbar.getMenu().clear();
            if (menuId > 0) {
                toolbar.inflateMenu(menuId);
                toolbar.setOnMenuItemClickListener(this);
            }
        }
    }

    protected void setUpTitle(int titleResId) {
        if (titleResId > 0 && toolbar_title != null) {
            toolbar_title.setText(titleResId);
        }
    }

    protected void setUpTitle(String titleStr) {
        if (!titleStr.equals("") && toolbar_title != null) {
            toolbar_title.setText(titleStr);
        }
    }

    protected void onNavigationBtnClicked() {
        finish();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    public boolean isActivityFinished() {
        if (Build.VERSION.SDK_INT >= 17) {
            return this == null || isFinishing() || isDestroyed();
        } else {
            return this == null || isFinishing();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }

        ActivityStackManager.getInstance().popActivity(this);
    }
}
