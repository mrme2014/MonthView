package com.ishow.ischool.business.campusperformance;

import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

import com.ishow.ischool.R;
import com.ishow.ischool.adpter.TableContentItemAdapter;
import com.ishow.ischool.adpter.TableLeftItemAdapter;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.widget.table.BodyHorizontalScrollView;
import com.ishow.ischool.widget.table.HeadHorizontalScrollView;

/**
 * Created by wqf on 2016/9/23.
 */
public class CampusPerformanceTableActivity extends BaseActivity4Crm {

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_campus_performance_table, R.string.campus_performance_target, MODE_BACK);
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {
        initView();
        initAdapter();
    }

    private HeadHorizontalScrollView sv_head; //不可滑动的顶部左侧的ScrollView
    private BodyHorizontalScrollView sv_content; //底部右侧的ScrollView
    private ListView lv_left;             //底部左侧的ListView
    private ListView lv_detail;        //底部右侧的ListView

    boolean isLeftListEnabled = false;
    boolean isRightListEnabled = false;

    private TableLeftItemAdapter tableLeftItemAdapter;
    private TableContentItemAdapter tableContentItemAdapter;


    private void initView() {
        sv_head = (HeadHorizontalScrollView)findViewById(R.id.sv_title);
        sv_content = (BodyHorizontalScrollView)findViewById(R.id.sv_detail);
        lv_left = (ListView) findViewById(R.id.lv_left);
        lv_detail = (ListView)findViewById(R.id.lv_detail);
        combination(lv_left, lv_detail, sv_head, sv_content);
    }

    private void initAdapter() {
        tableLeftItemAdapter = new TableLeftItemAdapter(this);
        tableContentItemAdapter = new TableContentItemAdapter(this);
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
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
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
