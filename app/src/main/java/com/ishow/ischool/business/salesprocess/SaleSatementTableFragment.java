package com.ishow.ischool.business.salesprocess;

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

public class SaleSatementTableFragment extends Fragment {

    @BindView(R.id.head_cell)
    LabelTextView headCell;
    @BindView(R.id.sale_table_head)
    TableRowTextView saleTableHead;
    @BindView(R.id.sale_table_headScrollView)
    DispatchHScrollView saleTableHeadScrollView;
   /* @BindView(R.id.sale_table_head2)
    TableRowTextView saleTableHead2;
    @BindView(R.id.sale_table_headScrollView2)
    DispatchHScrollView saleTableHeadScrollView2;*/
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

        Bundle arguments = getArguments();
        if (arguments != null) {
            show_table1 = arguments.getBoolean(SHOW_TABLE1);
        }
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle == null) return;
        if (show_table1) {
            tableBodys = (ArrayList<List<String>>) bundle.getSerializable(TABLE_BODY);
            tableHead = bundle.getStringArrayList(TABLE_HEAD);

        }

        if (!show_table1 && bundle.containsKey(TABLE_HEAD_TABLE2) && bundle.containsKey(TABLE_BODY_BODY2)) {
            tableHead2 = bundle.getStringArrayList(TABLE_HEAD_TABLE2);
            tableBodys2 = (ArrayList<List<String>>) bundle.getSerializable(TABLE_BODY_BODY2);

        }

        if (show_table1) {
            setTable1Adapter();
        } else {
            setTable2Adapter();
        }
    }

    private void setTable1Adapter() {

       /* dispatchHScrollView.setTableHead(saleTableHeadScrollView);
        dispatchHScrollView.scrollTo(saleTableHeadScrollView.getScrollX(), 0);

        saleTableHeadScrollView.setVisibility(View.VISIBLE);
        saleTableHeadScrollView2.setVisibility(View.GONE);*/

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

    private void setTable2Adapter() {
       /* dispatchHScrollView.setTableHead(saleTableHeadScrollView2);
        dispatchHScrollView.scrollTo(saleTableHeadScrollView2.getScrollX(), 0);

        saleTableHeadScrollView2.setVisibility(View.VISIBLE);
        saleTableHeadScrollView.setVisibility(View.GONE);*/

        if (adapter2 == null) {
            table2Remoed = tableHead2.remove(0);

            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < tableBodys2.size(); i++) {
                strings.add(tableBodys2.get(i).remove(0));
            }
            adapter2 = new SaleStatementTableAdapter(getContext(), tableBodys2);
            adapter4 = new SaleSatementAdapter(getContext(), strings);
           // saleTableHead2.setTxtList(tableHead2);
        }
        saleTableHead.setTxtList(tableHead2);
        headCell.setText(table2Remoed);
        saleTableRecyleview.setAdapter(adapter2);
        saleTableRecyleviewLeft.setAdapter(adapter4);

    }
}
