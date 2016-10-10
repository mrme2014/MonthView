package com.ishow.ischool.business.teachprocess;

import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.BaseActivity4Crm;

/**
 * Created by MrS on 2016/10/10.
 */

public class TeachProcessTableActivity extends BaseActivity4Crm {
    public final static String SHOW_MENU = "show_menu";
    public final static String SHOW_TABLE1 = "show_table1";
    private boolean show_table1;

    private TeachProcessTableFragment1 fragment1;
    private TeachProcessTableFragment2 fragment2;

    @Override
    protected void setUpContentView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_salestatementtable, R.string.teach_process_table1, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        show_table1 = getIntent().getBooleanExtra(SHOW_TABLE1, false);
        boolean show_menu = getIntent().getBooleanExtra(SHOW_MENU, false);

        if (show_menu) {
            setUpToolbar(show_table1 ? R.string.teach_process_table1 : R.string.teach_process_table2, R.menu.menu_sale_table, MODE_BACK);
            Menu menu = mToolbar.getMenu();
            if (menu != null && menu.size() > 0) {
                MenuItem item = menu.getItem(0);
                item.setTitle(show_table1 ? getString(R.string.teach_process_table2) : getString(R.string.teach_process_table1));
            }
        }
        showFragment();
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        mToolbarTitle.setText(show_table1 ? getString(R.string.teach_process_table2) : getString(R.string.teach_process_table1));
        item.setTitle(show_table1 ? getString(R.string.teach_process_table1) : getString(R.string.teach_process_table2));
        show_table1 = !show_table1;
        showFragment();
        return super.onMenuItemClick(item);
    }

    private void showFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment1 != null)
            ft.hide(fragment1);
        if (fragment2 != null)
            ft.hide(fragment2);

        if (show_table1) {
            if (fragment1 == null) {
                fragment1 = new TeachProcessTableFragment1();
                ft.add(R.id.sale_content, fragment1);
            } else ft.show(fragment1);
        } else {
            if (fragment2 == null) {
                fragment2 = new TeachProcessTableFragment2();
                ft.add(R.id.sale_content, fragment2);
            } else ft.show(fragment2);
        }
        ft.commitAllowingStateLoss();
    }
}
