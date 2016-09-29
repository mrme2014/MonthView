package com.ishow.ischool.business.salesprocess;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.BaseActivity4Crm;

/**
 * Created by MrS on 2016/9/13.
 */
public class SaleStatementTableActivity extends BaseActivity4Crm {
    public final static String SHOW_MENU = "show_menu";
    private boolean show_table1;
    private SaleSatementTableFragment fragment1, fragment2;

    @Override
    protected void initEnv() {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
//                            // bar
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
//        }
        super.initEnv();
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_salestatementtable, R.string.sale_process_statement, MODE_BACK);

    }

    @Override
    protected void setUpView() {
        show_table1 = getIntent().getBooleanExtra("show_table1", false);
        boolean show_menu = getIntent().getBooleanExtra(SHOW_MENU, false);

        if (show_menu) {
            setUpToolbar(show_table1 ? R.string.sale_process_statement : R.string.sale_process_zhuanjieshao, R.menu.menu_sale_table, MODE_BACK);
            Menu menu = mToolbar.getMenu();
            if (menu != null && menu.size() > 0) {
                MenuItem item = menu.getItem(0);
                item.setTitle(show_table1 ? getString(R.string.sale_process_zhuanjieshao) : getString(R.string.sale_process_statement));
            }
        }
        showFragment();
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        mToolbarTitle.setText(show_table1 ? getString(R.string.sale_process_zhuanjieshao) : getString(R.string.sale_process_statement));
        item.setTitle(show_table1 ? getString(R.string.sale_process_statement) : getString(R.string.sale_process_zhuanjieshao));
        show_table1 =!show_table1;
        showFragment();
        return super.onMenuItemClick(item);
    }

    private void showFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment1!=null)
            ft.hide(fragment1);
        if (fragment2!=null)
            ft.hide(fragment2);

        if (show_table1){
            if (fragment1==null){
                Bundle bundle = new Bundle();
                bundle.putBoolean(SaleSatementTableFragment.SHOW_TABLE1,show_table1);
                fragment1 = new SaleSatementTableFragment();
                fragment1.setArguments(bundle);
                ft.add(R.id.sale_content,fragment1);
            }else ft.show(fragment1);
        }else{
            if (fragment2==null) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(SaleSatementTableFragment.SHOW_TABLE1,show_table1);
                fragment2 = new SaleSatementTableFragment();
                fragment2.setArguments(bundle);
                ft.add(R.id.sale_content,fragment2);
            } else ft.show(fragment2);
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
}
