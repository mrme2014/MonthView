package com.ishow.ischool.business.salesprocess;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.ishow.ischool.R;
import com.ishow.ischool.adpter.SaleStatementTableAdapter;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.widget.custom.TableRowTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by MrS on 2016/9/13.
 */
public class SaleStatementTableActivity extends BaseActivity4Crm {
    @BindView(R.id.sale_table_head)
    TableRowTextView saleTableHead;
    @BindView(R.id.sale_table_recyleview)
    RecyclerView saleTableRecyleview;
    @BindView(R.id.DispatchHScrollView)
    com.ishow.ischool.widget.custom.DispatchHScrollView DispatchHScrollView;

    private ArrayList<List<String>> tableBodys;
    private ArrayList<String> tableHead;
    private SaleStatementTableAdapter adapter;

    private ArrayList<String> tableHead2;
    private ArrayList<List<String>> tableBodys2;
    private SaleStatementTableAdapter adapter2;
    private boolean show_table1;

    public static final String TABLE_HEAD = "table_head";
    public static final String TABLE_BODY = "table_body";
    public static final String TABLE_HEAD_TABLE2 = "table_head1";
    public static final String TABLE_BODY_BODY2 = "table_body2";
    public static final String SHOW_TABLE1 = "show_table1";


    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_salestatementtable, R.string.sale_process_statement, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        saleTableRecyleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        saleTableRecyleview.setHasFixedSize(true);
        DispatchHScrollView.setTableHead(saleTableHead);

        mToolbarTitle.setText(show_table1 ? getString(R.string.sale_process_statement) : getString(R.string.sale_process_zhuanjieshao));
    }

    @Override
    protected void setUpData() {

        show_table1 = getIntent().getBooleanExtra(SHOW_TABLE1, true);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        tableBodys = (ArrayList<List<String>>) bundle.getSerializable(TABLE_BODY);
        tableHead = bundle.getStringArrayList(TABLE_HEAD);

        if (bundle.containsKey(TABLE_HEAD_TABLE2) && bundle.containsKey(TABLE_BODY_BODY2)) {
            tableHead2 = bundle.getStringArrayList(TABLE_HEAD_TABLE2);
            tableBodys2 = (ArrayList<List<String>>) bundle.getSerializable(TABLE_BODY_BODY2);
            setUpToolbar(R.string.sale_process_statement, R.menu.menu_sale_table, MODE_BACK);

        }

        if (show_table1) {
            setTable1Adapter();
        } else {
            setTable2Adapter();
        }
    }

    private void setTable1Adapter() {
        saleTableHead.setTxtList(tableHead);
        if (adapter == null) {
            adapter = new SaleStatementTableAdapter(this, tableBodys);
        }
        saleTableRecyleview.setAdapter(adapter);
    }

    private void setTable2Adapter() {
        saleTableHead.setTxtList(tableHead2);
        if (adapter2 == null) {
            adapter2 = new SaleStatementTableAdapter(this, tableBodys2);
        }
        saleTableRecyleview.setAdapter(adapter2);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        mToolbarTitle.setText(show_table1 ? getString(R.string.sale_process_zhuanjieshao) : getString(R.string.sale_process_statement));
        item.setTitle(show_table1 ? getString(R.string.sale_process_statement) : getString(R.string.sale_process_zhuanjieshao));
        if (show_table1) {
            setTable2Adapter();
        } else {
            setTable1Adapter();
        }
        show_table1 = !show_table1;
        return super.onMenuItemClick(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
}
