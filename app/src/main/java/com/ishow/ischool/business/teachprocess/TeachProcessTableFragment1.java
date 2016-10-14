package com.ishow.ischool.business.teachprocess;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.commonlib.core.BaseFragment4mvp;
import com.ishow.ischool.R;
import com.ishow.ischool.widget.custom.TableRowTextView;
import com.ishow.ischool.widget.custom.TableRowTextViewTeach;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MrS on 2016/10/10.
 */

public class TeachProcessTableFragment1 extends BaseFragment4mvp {


    @BindView(R.id.head_cell)
    TableRowTextView headCell;
//    @BindView(R.id.body_cell1)
//    LabelTextView bodyCell1;
    @BindView(R.id.body_cell2)
    TableRowTextViewTeach bodyCell2;
    @BindView(R.id.body_cell3)
    TableRowTextViewTeach bodyCell3;
    @BindView(R.id.body_cell4)
    TableRowTextViewTeach bodyCell4;
    @BindView(R.id.body_cell5)
    TableRowTextView bodyCell5;
    private ArrayList<String> table1_head;
    private ArrayList<String> table1_body;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_teach_process_table1, null);
        ButterKnife.bind(this, rootView);
        return rootView;
        /*table2_head = extras.getStringArrayList("table2_head");
        table2_body = (List<List<String>>) extras.getSerializable("table2_body");*/
    }

    @Override
    public void init() {
        Bundle extras = getActivity().getIntent().getExtras();
        table1_head = extras.getStringArrayList("table1_head");
        table1_body = extras.getStringArrayList("table1_body");

        setUpData();
    }

    private void setUpData() {
        List<String> strings_head = table1_head.subList(13, table1_head.size());
        List<String> strings_body = table1_body.subList(13, table1_body.size());
        headCell.setTxtList(strings_head);
       // bodyCell1.setText(table1_body.get(0));
        bodyCell2.updateList(table1_body.get(4),table1_body.get(1),table1_body.get(2),table1_body.get(3));
        bodyCell3.updateList(table1_body.get(8),table1_body.get(5),table1_body.get(6),table1_body.get(7));
        bodyCell4.updateList(table1_body.get(12),table1_body.get(9),table1_body.get(10),table1_body.get(11));
        bodyCell5.setTxtList(strings_body);

    }
}
