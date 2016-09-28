package com.ishow.ischool.business.salesprocess;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.adpter.SaleSatementAdapter;
import com.ishow.ischool.adpter.SaleStatementTableAdapter;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.widget.custom.TableRowTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MrS on 2016/9/13.
 */
public class SaleStatementTableActivity extends BaseActivity4Crm {
    @BindView(R.id.sale_table_head)
    TableRowTextView saleTableHead;
    @BindView(R.id.sale_table_recyleview)
    RecyclerView saleTableRecyleview;
    @BindView(R.id.DispatchHScrollView_body)
    com.ishow.ischool.widget.custom.DispatchHScrollView DispatchHScrollView;
    @BindView(R.id.head_cell)
    TextView headCell;

    @BindView(R.id.sale_table_headScrollView)
    com.ishow.ischool.widget.custom.DispatchHScrollView saleTableHeadScrollView;
    @BindView(R.id.sale_table_recyleview_left)
    RecyclerView saleTableRecyleviewLeft;
    @BindView(R.id.sale_table_head2)
    TableRowTextView saleTableHead2;
    @BindView(R.id.sale_table_headScrollView2)
    com.ishow.ischool.widget.custom.DispatchHScrollView saleTableHeadScrollView2;

    private ArrayList<List<String>> tableBodys;
    private ArrayList<String> tableHead;
    private SaleStatementTableAdapter adapter;

    private ArrayList<String> tableHead2;
    private ArrayList<List<String>> tableBodys2;
    private SaleStatementTableAdapter adapter2;

    private SaleSatementAdapter adapter3;
    private SaleSatementAdapter adapter4;

    private boolean show_table1;

    public static final String TABLE_HEAD = "table_head";
    public static final String TABLE_BODY = "table_body";
    public static final String TABLE_HEAD_TABLE2 = "table_head1";
    public static final String TABLE_BODY_BODY2 = "table_body2";
    public static final String SHOW_TABLE1 = "show_table1";

    private String table1Remoed;
    private String table2Remoed;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_salestatementtable, R.string.sale_process_statement, MODE_BACK);
    }

    @Override
    protected void setUpView() {

        saleTableRecyleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        saleTableRecyleviewLeft.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        saleTableRecyleviewLeft.setHasFixedSize(true);
        saleTableRecyleview.setHasFixedSize(true);

        saleTableHeadScrollView.setTableHead(DispatchHScrollView);
        saleTableHeadScrollView2.setTableHead(DispatchHScrollView);

        saleTableRecyleviewLeft.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        saleTableRecyleview.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        saleTableRecyleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) saleTableRecyleview.getLayoutManager();
                LinearLayoutManager layoutManager1 = (LinearLayoutManager) saleTableRecyleviewLeft.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                View view = layoutManager.findViewByPosition(firstVisibleItemPosition);
                layoutManager1.scrollToPositionWithOffset(firstVisibleItemPosition, view.getTop());
            }
        });
        saleTableRecyleviewLeft.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) saleTableRecyleviewLeft.getLayoutManager();
                LinearLayoutManager layoutManager1 = (LinearLayoutManager) saleTableRecyleview.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                View view = layoutManager.findViewByPosition(firstVisibleItemPosition);
                layoutManager1.scrollToPositionWithOffset(firstVisibleItemPosition, view.getTop());
            }
        });

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
        Menu menu = mToolbar.getMenu();
        if (menu!=null){
            MenuItem item = menu.getItem(0);
            item.setTitle(show_table1 ? getString(R.string.sale_process_zhuanjieshao) : getString(R.string.sale_process_statement));
        }
        mToolbarTitle.setText(show_table1 ? getString(R.string.sale_process_statement) : getString(R.string.sale_process_zhuanjieshao));
        if (show_table1) {
            setTable1Adapter();
        } else {
            setTable2Adapter();
        }


    }

    private void setTable1Adapter() {

        DispatchHScrollView.setTableHead(saleTableHeadScrollView);
        DispatchHScrollView.scrollTo(saleTableHeadScrollView.getScrollX(), 0);

        saleTableHeadScrollView.setVisibility(View.VISIBLE);
        saleTableHeadScrollView2.setVisibility(View.GONE);

        if (adapter == null) {
            table1Remoed = tableHead.remove(0);

            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < tableBodys.size(); i++) {
                strings.add(tableBodys.get(i).remove(0));
            }

            adapter = new SaleStatementTableAdapter(this, tableBodys);
            adapter3 = new SaleSatementAdapter(this, strings);
            saleTableHead.setTxtList(tableHead);
        }

        headCell.setText(table1Remoed);
        saleTableRecyleviewLeft.setAdapter(adapter3);
        saleTableRecyleview.setAdapter(adapter);

    }

    private void setTable2Adapter() {
        DispatchHScrollView.setTableHead(saleTableHeadScrollView2);
        DispatchHScrollView.scrollTo(saleTableHeadScrollView2.getScrollX(), 0);

        saleTableHeadScrollView2.setVisibility(View.VISIBLE);
        saleTableHeadScrollView.setVisibility(View.GONE);

        if (adapter2 == null) {
            table2Remoed = tableHead2.remove(0);

            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < tableBodys2.size(); i++) {
                strings.add(tableBodys2.get(i).remove(0));
            }
            adapter2 = new SaleStatementTableAdapter(this, tableBodys2);
            adapter4 = new SaleSatementAdapter(this, strings);
            saleTableHead2.setTxtList(tableHead2);
        }

        headCell.setText(table2Remoed);
        saleTableRecyleview.setAdapter(adapter2);
        saleTableRecyleviewLeft.setAdapter(adapter4);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        show_table1 = !show_table1;
        mToolbarTitle.setText(show_table1 ? getString(R.string.sale_process_statement) : getString(R.string.sale_process_zhuanjieshao));
        item.setTitle(show_table1 ? getString(R.string.sale_process_zhuanjieshao) : getString(R.string.sale_process_statement));

        if (show_table1) {
            setTable1Adapter();
        } else {
            setTable2Adapter();
        }

        return super.onMenuItemClick(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
