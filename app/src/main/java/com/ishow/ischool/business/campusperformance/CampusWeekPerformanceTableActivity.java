package com.ishow.ischool.business.campusperformance;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

import com.ishow.ischool.R;
import com.ishow.ischool.adpter.TableLeftItemAdapter;
import com.ishow.ischool.bean.campusperformance.MonthTableData;
import com.ishow.ischool.bean.campusperformance.WeekTableBodyItem;
import com.ishow.ischool.bean.campusperformance.WeekTableBodyRow;
import com.ishow.ischool.bean.campusperformance.WeekTableHead;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.widget.table.BodyHorizontalScrollView;
import com.ishow.ischool.widget.table.HeadHorizontalScrollView;
import com.ishow.ischool.widget.table.MyLinearLayout4ListView;
import com.ishow.ischool.widget.table.WeekPerformanceTableHeadAdapter;
import com.ishow.ischool.widget.table.WeekPerformanceTableRowAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqf on 2016/9/23.
 */
public class CampusWeekPerformanceTableActivity extends BaseActivity4Crm {
    private MonthTableData mDatas;
    private String monthStr;
    private int mMonthId = 1;
    private ArrayList<WeekTableHead> mWeek4MonthHeadData;
    private ArrayList<WeekTableBodyRow> mWeek4MonthData;

    @Override
    protected void initEnv() {
        Bundle bundle = getIntent().getExtras();
        mDatas = bundle.getParcelable("data");
        monthStr = bundle.getString("month");
        mMonthId = bundle.getInt("monthPosition");
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_campus_week_performance_table, getString(R.string.campus_performance_amount) + "(" + monthStr + ")");
    }

    @Override
    protected void setUpView() {
        mWeek4MonthHeadData = mDatas._week_title.get(mMonthId);
        mWeek4MonthData = (ArrayList<WeekTableBodyRow>) mDatas._week_data.get(mMonthId);
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
    private WeekPerformanceTableRowAdapter tableContentItemAdapter;
    private MyLinearLayout4ListView myLinearLayout4ListView;


    private void initView() {
        sv_head = (HeadHorizontalScrollView) findViewById(R.id.sv_title);
        sv_content = (BodyHorizontalScrollView) findViewById(R.id.sv_detail);
        lv_left = (ListView) findViewById(R.id.lv_left);
        lv_detail = (ListView) findViewById(R.id.lv_detail);
        myLinearLayout4ListView = (MyLinearLayout4ListView) findViewById(R.id.head_lv);
        combination(lv_left, lv_detail, sv_head, sv_content);
    }

    private void initAdapter() {
        ArrayList<String> campusDatas = new ArrayList<>();
        for (int i = 0; i < mWeek4MonthData.size(); i++) {
            ArrayList<WeekTableBodyItem> items = (ArrayList<WeekTableBodyItem>) mWeek4MonthData.get(i);
            ArrayList<String> item = (ArrayList<String>) items.get(0);
            campusDatas.add(item.get(0));
        }
        tableLeftItemAdapter = new TableLeftItemAdapter(this, campusDatas);
        lv_left.setAdapter(tableLeftItemAdapter);
        tableContentItemAdapter = new WeekPerformanceTableRowAdapter(this, mDatas._week_data.get(mMonthId));
        lv_detail.setAdapter(tableContentItemAdapter);

        List<WeekTableHead> titles = new ArrayList<>();
        titles.addAll(mWeek4MonthHeadData.subList(1, mWeek4MonthHeadData.size()));        // 去掉第一个"校区"(已固定存在于左上角)
        WeekPerformanceTableHeadAdapter adapter = new WeekPerformanceTableHeadAdapter(CampusWeekPerformanceTableActivity.this, titles);
        myLinearLayout4ListView.setAdapter(adapter);

        myLinearLayout4ListView.setOnItemClickListener(new MyLinearLayout4ListView.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, Object obj, int position) {
//                MonthTableHead info = (MonthTableHead) obj;
//                if (info != null) {
//                }
            }
        });
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
        // 禁止快速滑动,回弹效果
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
