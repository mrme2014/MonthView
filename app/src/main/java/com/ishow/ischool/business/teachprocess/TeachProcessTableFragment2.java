package com.ishow.ischool.business.teachprocess;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.SaleSatementAdapter;
import com.ishow.ischool.adpter.SaleStatementTableAdapter;
import com.ishow.ischool.widget.custom.DispatchHScrollView;
import com.ishow.ischool.widget.custom.TableRowTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MrS on 2016/9/29.
 */

public class TeachProcessTableFragment2 extends Fragment {

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
    private View rootView;


    private ArrayList<List<String>> tableBodys;
    private ArrayList<String> tableHead;
    private SaleStatementTableAdapter adapter;

    private SaleSatementAdapter adapter3;
    private String table1Remoed;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_salestatement_fragment, container, false);
        ButterKnife.bind(this, rootView);

        setUpView();

        setUpData();

        return rootView;
    }

    private void setUpView() {
        saleTableRecyleview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        saleTableRecyleviewLeft.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

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
    }

    private void linkageRollRecycleView(RecyclerView activeRollRecyclview, RecyclerView passiveRollRecycleView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) activeRollRecyclview.getLayoutManager();
        LinearLayoutManager layoutManager1 = (LinearLayoutManager) passiveRollRecycleView.getLayoutManager();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        View view = layoutManager.findViewByPosition(firstVisibleItemPosition);
        layoutManager1.scrollToPositionWithOffset(firstVisibleItemPosition, view.getTop());
    }

    private void setUpData() {

        Bundle bundle = getActivity().getIntent().getExtras();
        tableBodys = (ArrayList<List<String>>) bundle.getSerializable("table2_body");
        tableHead = bundle.getStringArrayList("table2_head");
        setTable1Adapter();
    }

    private void setTable1Adapter() {

        if (adapter == null) {
            table1Remoed = tableHead.remove(0);

            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < tableBodys.size(); i++) {
                strings.add(tableBodys.get(i).remove(0));
            }

            adapter = new SaleStatementTableAdapter(getContext(), tableBodys);
            adapter3 = new SaleSatementAdapter(getContext(), strings);
        }
        saleTableHead.setTxtList(tableHead);
        headCell.setText(table1Remoed);
        saleTableRecyleviewLeft.setAdapter(adapter3);
        saleTableRecyleview.setAdapter(adapter);

    }

}
