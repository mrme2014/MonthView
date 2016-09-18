package com.ishow.ischool.business.salesprocess;

import android.content.res.Configuration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ishow.ischool.R;
import com.ishow.ischool.adpter.SaleStatementTableAdapter;
import com.ishow.ischool.bean.saleprocess.TableBody;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.widget.custom.TableRowTextView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by MrS on 2016/9/13.
 */
public class SaleStatementTableActivity extends BaseActivity4Crm {
    @BindView(R.id.sale_table_head)
    TableRowTextView saleTableHead;
    @BindView(R.id.sale_table_recyleview)
    RecyclerView saleTableRecyleview;

    public static final String TABLE_HEAD = "table_head";
    public static final String TABLE_BODY = "table_body";
    @BindView(R.id.DispatchHScrollView)
    com.ishow.ischool.widget.custom.DispatchHScrollView DispatchHScrollView;

    private ArrayList<TableBody> tableBodys;
    private ArrayList<String> tableHead;
    private SaleStatementTableAdapter adapter;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_salestatementtable, R.string.sale_process_statement, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        saleTableRecyleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        saleTableRecyleview.setHasFixedSize(true);

        DispatchHScrollView.setTableHead(saleTableHead);
    }

    @Override
    protected void setUpData() {
        tableBodys = getIntent().getExtras().getParcelableArrayList(TABLE_BODY);
        tableHead = getIntent().getExtras().getStringArrayList(TABLE_HEAD);

        saleTableHead.setTxtList(tableHead);
        adapter = new SaleStatementTableAdapter(this, tableBodys);

        saleTableRecyleview.setAdapter(adapter);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

}
