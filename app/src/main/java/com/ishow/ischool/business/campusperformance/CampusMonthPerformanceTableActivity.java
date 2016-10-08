package com.ishow.ischool.business.campusperformance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

import com.commonlib.http.ApiFactory;
import com.commonlib.util.LogUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.TableLeftItemAdapter;
import com.ishow.ischool.bean.campusperformance.MonthTableData;
import com.ishow.ischool.bean.campusperformance.MonthTableHead;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.DataApi;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.widget.table.BodyHorizontalScrollView;
import com.ishow.ischool.widget.table.HeadHorizontalScrollView;
import com.ishow.ischool.widget.table.MonthPerformanceTableHeadAdapter;
import com.ishow.ischool.widget.table.MonthPerformanceTableRowAdapter;
import com.ishow.ischool.widget.table.MyLinearLayout4ListView;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 2016/9/23.
 */
public class CampusMonthPerformanceTableActivity extends BaseActivity4Crm {
    private MonthTableData mDatas;
    private String mCampusIds;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_campus_month_performance_table, R.string.student_performance_detail_table, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        mCampusIds = getIntent().getStringExtra("campusParam");
    }

    @Override
    protected void setUpData() {
        initView();
        ApiFactory.getInstance().getApi(DataApi.class).getCampusMonth(1, mCampusIds, 201607, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<MonthTableData>() {
                    @Override
                    public void onSuccess(MonthTableData result) {
                        LogUtil.d("onSuccess", result.toString());
                        mDatas = result;

                        initAdapter();
                    }

                    @Override
                    public void onError(String msg) {
                        LogUtil.d("onError", msg);
                    }
                });
    }

    private HeadHorizontalScrollView sv_head; //不可滑动的顶部左侧的ScrollView
    private BodyHorizontalScrollView sv_content; //底部右侧的ScrollView
    private ListView lv_left;             //底部左侧的ListView
    private ListView lv_detail;        //底部右侧的ListView

    boolean isLeftListEnabled = false;
    boolean isRightListEnabled = false;

    private TableLeftItemAdapter tableLeftItemAdapter;
    private MonthPerformanceTableRowAdapter tableContentItemAdapter;
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
        // 校区名字是 _data每个元素的第一个元素
        ArrayList<String> campusDatas = new ArrayList<>();
        for (int i = 0; i < mDatas._data.size(); i++) {
            campusDatas.add(mDatas._data.get(i).get(0).get(0));
        }
        tableLeftItemAdapter = new TableLeftItemAdapter(this, campusDatas);
        lv_left.setAdapter(tableLeftItemAdapter);
        tableContentItemAdapter = new MonthPerformanceTableRowAdapter(this, mDatas._data);
        lv_detail.setAdapter(tableContentItemAdapter);

        List<MonthTableHead> titles = new ArrayList<>();
        titles.addAll(mDatas._title.subList(1, mDatas._title.size()));        // 去掉第一个"校区"(已固定存在于左上角)
        MonthPerformanceTableHeadAdapter adapter = new MonthPerformanceTableHeadAdapter(CampusMonthPerformanceTableActivity.this, titles);
        myLinearLayout4ListView.setAdapter(adapter);

        int flag = -1, count = 1;
        for (int i = 0; i < mDatas._title.size(); i++) {
            if (mDatas._title.get(i).subtitle != null && mDatas._title.get(i).subtitle.size() == 3) {
                flag = i;
                break;
            }
        }
        for (int j = flag; j < mDatas._title.size(); j++) {
            if (count >= 7) {           // 只取连续有subtitle的6个对象
                break;
            }
            mDatas._title.get(j).monthPosition = count;
            count++;
        }

        myLinearLayout4ListView.setOnItemClickListener(new MyLinearLayout4ListView.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, Object obj, int position) {
                MonthTableHead info = (MonthTableHead) obj;
                if (info != null) {
                    if (info.monthPosition > 0) {
                        Intent intent = new Intent(CampusMonthPerformanceTableActivity.this, CampusWeekPerformanceTableActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("data", mDatas);
                        bundle.putString("month", info.title);
                        bundle.putInt("monthPosition", info.monthPosition);
//                        bundle.putStringArrayList("bodydata", mDatas._week_data.get(0));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
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
