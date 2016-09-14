package com.ishow.ischool.business.salesprocess;

import android.support.v7.widget.RecyclerView;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.widget.custom.TableRowTextView;

import butterknife.BindView;

/**
 * Created by MrS on 2016/9/13.
 */
public class SaleStatementTableActivity extends BaseActivity4Crm {
    @BindView(R.id.sale_table_head)
    TableRowTextView saleTableHead;
    @BindView(R.id.sale_table_recyleview)
    RecyclerView saleTableRecyleview;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_salestatementtable, R.string.sale_process_statement, MODE_BACK);
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

}
