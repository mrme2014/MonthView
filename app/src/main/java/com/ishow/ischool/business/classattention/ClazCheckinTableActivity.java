package com.ishow.ischool.business.classattention;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.util.LogUtil;
import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.SaleSatementAdapter;
import com.ishow.ischool.adpter.SaleStatementTableAdapter;
import com.ishow.ischool.bean.classattend.ClazCheckTable;
import com.ishow.ischool.bean.classattend.ClazTableRow;
import com.ishow.ischool.bean.classattend.ClazTableTotal;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.fragment.TimeSeletByUserDialog;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.DispatchHScrollView;
import com.ishow.ischool.widget.custom.TableRowTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;

/**
 * Created by MrS on 2016/10/21.
 */

public class ClazCheckinTableActivity extends BaseActivity4Crm<ClazPresenter, ClazModle> implements ClazView<ClazCheckTable>, View.OnClickListener {
    @BindView(R.id.head_cell)
    LabelTextView headCell;
    @BindView(R.id.sale_table_head)
    TableRowTextView saleTableHead;
    @BindView(R.id.sale_table_headScrollView)
    DispatchHScrollView saleTableHeadScrollView;
    @BindView(R.id.sale_table_recyleview_left)
    RecyclerView saleTableRecyleviewLeft;
    @BindView(R.id.sale_table_recyleview)
    RecyclerView saleTableRecyleview;
    @BindView(R.id.DispatchHScrollView_body)
    DispatchHScrollView dispatchHScrollView;
    @BindView(R.id.claz_table_tip)
    TextView clazTableTip;

    private SaleStatementTableAdapter adapter2;
    private SaleSatementAdapter adapter3;

    private int claz_id;
    private TimeSeletByUserDialog timeSeletByUser;
    private TreeMap map;
    private int end_time;
    private int begin_time;
    private ArrayList<String> left;
    private List<List<String>> listList;

    @Override
    protected void initEnv() {
        super.initEnv();
        Intent intent = getIntent();
        claz_id = intent.getIntExtra("claz_id", 1);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_checkintable, R.string.history_claz, R.menu.menu_table_claz_time, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        Menu menu = mToolbar.getMenu();
        MenuItem item = menu.getItem(0);
        LabelTextView actionView = (LabelTextView) MenuItemCompat.getActionView(item);
        actionView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.mipmap.icon_screen_down_white), null);
        actionView.setAboutMenuItem();
        actionView.setText(getString(R.string.history_claz_time));
        actionView.setUpMenu(true);
        actionView.setOnClickListener(this);
        saleTableRecyleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        saleTableRecyleviewLeft.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        saleTableRecyleviewLeft.setHasFixedSize(true);
        saleTableRecyleview.setHasFixedSize(true);

        saleTableHeadScrollView.setTableHead(dispatchHScrollView);
        dispatchHScrollView.setTableHead(saleTableHeadScrollView);
        //saleTableHeadScrollView2.setTableHead(dispatchHScrollView);

        saleTableRecyleviewLeft.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        saleTableRecyleview.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        saleTableRecyleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                linkageRollRecycleView(saleTableRecyleview, saleTableRecyleviewLeft);
            }
        });
        saleTableRecyleviewLeft.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                linkageRollRecycleView(saleTableRecyleviewLeft, saleTableRecyleview);
            }
        });

        headCell.setText("序号");
        saleTableHead.setTxtList(AppUtil.getClazCheckInTableHeadList());
    }

    private void linkageRollRecycleView(RecyclerView activeRollRecyclview, RecyclerView passiveRollRecycleView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) activeRollRecyclview.getLayoutManager();
        LinearLayoutManager layoutManager1 = (LinearLayoutManager) passiveRollRecycleView.getLayoutManager();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        View view = layoutManager.findViewByPosition(firstVisibleItemPosition);
        if (view != null)
            layoutManager1.scrollToPositionWithOffset(firstVisibleItemPosition, view.getTop());
    }

    @Override
    protected void setUpData() {

        if (map == null) map = new TreeMap();
        map.put("classes_id", claz_id);
        mPresenter.getCheckInList(map);

    }

    @Override
    public void getResutSucess(ClazCheckTable result) {
        List<ClazTableRow> lists = result.lists;
        if (lists == null || lists.size() == 0) {
            clazTableTip.setText(String.format(getString(R.string.claz_table_tip), "  0", "  0", "  0"));
            if (left != null) left.clear();
            if (listList != null) listList.clear();
            adapter2.notifyDataSetChanged();
            adapter3.notifyDataSetChanged();
            return;
        }
        ClazTableTotal total = result.total;
        clazTableTip.setText(String.format(getString(R.string.claz_table_tip), total.num, "  "+total.real_numbers+"  ",  "  "+total.numbers+"  "));
        if (listList == null) listList = new ArrayList<>();
        listList.clear();
        if (left == null) left = new ArrayList<>();
        left.clear();
        LogUtil.e(listList.size()+"-----"+lists.size());
        for (int i = 0; i < lists.size(); i++) {
            ClazTableRow clazTableRow = lists.get(i);
            List<String> row = new ArrayList<>();
            row.add(DateUtil.parseSecond2Str(Long.parseLong(clazTableRow.class_date + "")));
            row.add(clazTableRow.teacher);
            row.add(clazTableRow.lessoned_times);
            row.add(clazTableRow.real_numbers);
            row.add(clazTableRow.numbers);
            left.add((i + 1) + "");//左边 list序号列
            listList.add(row);
        }
        if (adapter2 == null) {
            adapter3 = new SaleSatementAdapter(this, left);
            adapter2 = new SaleStatementTableAdapter(this, listList);
            saleTableRecyleview.setAdapter(adapter2);
            saleTableRecyleviewLeft.setAdapter(adapter3);
        } else {
            adapter2.onRefresh(listList);
            adapter3.onRefresh(left);
        }
    }

    @Override
    public void CheckInSucess(String msg) {

    }

    @Override
    public void getResultEorre(String msg) {
        showToast(msg);
    }

    @Override
    public void onClick(View v) {
        timeSeletByUser = new TimeSeletByUserDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean("no_need_check", true);
        begin_time =0;
        end_time=0;
        //bundle.putInt("start_time", begin_time);
        // bundle.putInt("end_time", end_time);
        timeSeletByUser.setArguments(bundle);
        timeSeletByUser.setOnSelectResultCallback(new TimeSeletByUserDialog.OnSelectResultCallback() {
            @Override
            public void onResult(int start_time, int over_time) {
                begin_time = start_time - 12 * 3600;
                end_time = over_time-12*3600;
                map.clear();
                map.put("begin_time", begin_time);
                LogUtil.e(start_time+"onResult"+over_time);
                if (over_time != 0) map.put("end_time", end_time);
                setUpData();
            }

            @Override
            public void onEorr(String error) {
                showToast(error);
            }

            @Override
            public void onCacel() {

            }
        });
        if (!timeSeletByUser.isAdded()) timeSeletByUser.show(getSupportFragmentManager(), "dialog");
    }
}
