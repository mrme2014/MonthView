package com.ishow.ischool.business.campusperformance;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

import com.ishow.ischool.R;
import com.ishow.ischool.adpter.TableContentItemAdapter;
import com.ishow.ischool.adpter.TableLeftItemAdapter;
import com.ishow.ischool.bean.campusperformance.SignAmount;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.widget.table.BodyHorizontalScrollView;
import com.ishow.ischool.widget.table.HeadHorizontalScrollView;
import com.ishow.ischool.widget.table.MyLinearLayout4ListView;
import com.ishow.ischool.widget.table.AmountTableHeadAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqf on 2016/9/23.
 */
public class CampusAmountTableActivity extends BaseActivity4Crm {
    private ArrayList<String> campus;
    private ArrayList<SignAmount> datas;

    private HeadHorizontalScrollView sv_head; //不可滑动的顶部左侧的ScrollView
    private MyLinearLayout4ListView myLinearLayout4ListView;
    private BodyHorizontalScrollView sv_content; //底部右侧的ScrollView
    private ListView lv_left;             //底部左侧的ListView
    private ListView lv_detail;        //底部右侧的ListView

    boolean isLeftListEnabled = false;
    boolean isRightListEnabled = false;

    private TableLeftItemAdapter tableLeftItemAdapter;
    private TableContentItemAdapter tableContentItemAdapter;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_campus_amount_table, R.string.student_performance_table, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        Bundle bundle = getIntent().getExtras();
        campus = bundle.getStringArrayList("campus");
        datas = bundle.getParcelableArrayList("data");
    }

    @Override
    protected void setUpData() {
        initView();
        initAdapter();
    }


    private void initView() {
        myLinearLayout4ListView = (MyLinearLayout4ListView) findViewById(R.id.head_lv);
        sv_head = (HeadHorizontalScrollView) findViewById(R.id.sv_title);
        sv_content = (BodyHorizontalScrollView) findViewById(R.id.sv_detail);
        lv_left = (ListView) findViewById(R.id.lv_left);
        lv_detail = (ListView) findViewById(R.id.lv_detail);
        combination(lv_left, lv_detail, sv_head, sv_content);

        List<String> list = new ArrayList<>();
        list.add("总现场");
        list.add("总报名");
        list.add("总全款");
        list.add("总报名率");
        list.add("总全款率");
        list.add("总报名全款率");
        AmountTableHeadAdapter adapter = new AmountTableHeadAdapter(CampusAmountTableActivity.this, list);
        myLinearLayout4ListView.setAdapter(adapter);
    }

    private void initAdapter() {
        tableLeftItemAdapter = new TableLeftItemAdapter(this, campus);
        tableContentItemAdapter = new TableContentItemAdapter(this, datas);
        lv_left.setAdapter(tableLeftItemAdapter);
        lv_detail.setAdapter(tableContentItemAdapter);
    }

    private void combination(final ListView lvName, final ListView lvDetail, final HorizontalScrollView title, BodyHorizontalScrollView content) {
        /**
         * 左右滑动同步
         */
        content.setMyScrollChangeListener(new BodyHorizontalScrollView.LinkScrollChangeListener() {
            @Override
            public void onscroll(BodyHorizontalScrollView view, int x, int y, int oldx, int oldy) {
                title.scrollTo(x, y);
            }
        });

        /**
         * 上下滑动同步
         */
        // 禁止快速滑动
        lvName.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        lvDetail.setOverScrollMode(ListView.OVER_SCROLL_NEVER);

        //左侧ListView滚动时，控制右侧ListView滚动
        lvName.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //这两个enable标志位是为了避免死循环
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isRightListEnabled = false;
                    isLeftListEnabled = true;
                } else if (scrollState == SCROLL_STATE_IDLE) {
                    isRightListEnabled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View child = view.getChildAt(0);
                if (child != null && isLeftListEnabled) {
                    lvDetail.setSelectionFromTop(firstVisibleItem, child.getTop());
                }
            }
        });

        //右侧ListView滚动时，控制左侧ListView滚动
        lvDetail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isLeftListEnabled = false;
                    isRightListEnabled = true;
                } else if (scrollState == SCROLL_STATE_IDLE) {
                    isLeftListEnabled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                View c = view.getChildAt(0);
                if (c != null && isRightListEnabled) {
                    lvName.setSelectionFromTop(firstVisibleItem, c.getTop());
                }
            }
        });

    }
}
