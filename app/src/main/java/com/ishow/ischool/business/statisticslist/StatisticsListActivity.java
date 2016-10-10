package com.ishow.ischool.business.statisticslist;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.commonlib.util.LogUtil;
import com.commonlib.widget.fabbehavior.HidingScrollListener;
import com.commonlib.widget.pull.BaseItemDecor;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.activity.StatisticsSearchFragment;
import com.ishow.ischool.application.Cons;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.student.Student;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.bean.student.StudentList;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.business.addstudent.AddStudentActivity;
import com.ishow.ischool.business.student.detail.StudentDetailActivity;
import com.ishow.ischool.common.api.MarketApi;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.common.rxbus.RxBus;
import com.ishow.ischool.fragment.StatisticsFilterFragment;
import com.ishow.ischool.widget.custom.AvatarImageView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;


/**
 * Created by wqf on 16/8/14.
 */
public class StatisticsListActivity extends BaseListActivity4Crm<StatisticsListPresenter, StatisticsListModel, Student> implements StatisticsListContract.View,
        StatisticsFilterFragment.FilterCallback {

    @BindView(R.id.fab)
    FloatingActionButton addFab;
    @BindView(R.id.search_content)
    FrameLayout frameLayout;

    private String mCampusId, mSource;

    //  搜索
    private SearchView mSearchView;
    private String mSearchKey;
    StatisticsSearchFragment searchFragment;

    // 筛选
    private HashMap<String, String> filterParams;
    private String mFilterSourceName;
    private String mFilterCollegeName;
    private String mFilterReferrerName;
    StatisticsFilterFragment dialog = null;
    private int curPositionId;      // 用户当前职位id

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_statistics_list, R.string.student_statistics, R.menu.menu_statisticslist, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        super.setUpView();

        doSubscribe();
        initFilter();
        final MenuItem searchItem = mToolbar.getMenu().findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setQueryHint(getString(R.string.hint_search_phone_username));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LogUtil.d("SearchView newText = " + newText);
                mSearchKey = newText;
                if (searchFragment == null) {
                    searchFragment = StatisticsSearchFragment.newInstance(mCampusId, mSource);
                }
                searchFragment.startSearch(mSearchKey);
                return true;
            }
        });
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchFragment();
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                hideSearchFragment();
                return false;
            }
        });


        // recycleview上滑隐藏fab,下滑显示
        recycler.getRecyclerView().addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                Resources resources = StatisticsListActivity.this.getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                float density = dm.density;
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                addFab.animate()
                        .translationY(height - addFab.getHeight())
                        .setInterpolator(new AccelerateInterpolator(2))
                        .start();
            }

            @Override
            public void onShow() {
                addFab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }
        });

        searchFragment = StatisticsSearchFragment.newInstance(mCampusId, mSource);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new BaseItemDecor(this, 67);
    }

    void initFilter() {
        filterParams = new HashMap<String, String>();
        if (mUser.userInfo.campus_id == Campus.HEADQUARTERS) {
            mCampusId = Campus.HEADQUARTERS + "";
            filterParams.put("campus_id", Campus.HEADQUARTERS + "");                  // 总部获取学院统计列表campus_id传1
        } else {
            mCampusId = mUser.userInfo.campus_id + "";
            filterParams.put("campus_id", mUser.userInfo.campus_id + "");
        }

        curPositionId = mUser.positionInfo.id;
        if (curPositionId == Cons.Position.Chendujiangshi.ordinal()) {
            mSource = MarketApi.TYPESOURCE_READING + "";
            filterParams.put("source", MarketApi.TYPESOURCE_READING + "");
        } else if (curPositionId == Cons.Position.Xiaoliaozhuanyuan.ordinal()) {
            mSource = MarketApi.TYPESOURCE_CHAT + "";
            filterParams.put("source", MarketApi.TYPESOURCE_CHAT + "");
        } else {
            mSource = "-1";
            filterParams.put("source", "-1");
        }
    }

    void showSearchFragment() {
        if (searchFragment == null) {
            searchFragment = StatisticsSearchFragment.newInstance(mCampusId, mSource);
        }
        frameLayout.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.search_content, searchFragment);
        ft.commit();
    }

    void hideSearchFragment() {
        frameLayout.setVisibility(View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(searchFragment);
        ft.commit();
        searchFragment = null;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                if (dialog == null) {
                    dialog = StatisticsFilterFragment.newInstance(filterParams, mFilterSourceName, mFilterCollegeName, mFilterReferrerName);
                    dialog.setOnFilterCallback(StatisticsListActivity.this);
                }
                dialog.show(getSupportFragmentManager(), "dialog");
                break;
        }
        return true;
    }

    private void doSubscribe() {
//        Subscription subscription4AddSuccess = RxBus.getInstance()
//                .doSubscribe(StudentInfo.class, new Action1<StudentInfo>() {
//                    @Override
//                    public void call(StudentInfo studentInfo) {
//                        initFilter();
//                        setRefreshing();
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//
//                    }
//                });
//        RxBus.getInstance().addSubscription(this, subscription4AddSuccess);
        RxBus.getDefault().register(StudentInfo.class, new Action1() {
            @Override
            public void call(Object o) {
                initFilter();
                setRefreshing();
            }
        });
    }

    /**
     * @param map             筛选参数
     * @param source_name     用于缓存筛选后，再次进入时填充内容（来源方式）
     * @param university_name 用于缓存筛选后，再次进入时填充内容（就读学校）
     * @param referrer_name   用于缓存筛选后，再次进入时填充内容 (推荐人)
     */
    @Override
    public void onFinishFilter(HashMap<String, String> map, String source_name, String university_name, String referrer_name) {
        dialog = null;
        if (!filterParams.equals(map)) {
            mFilterSourceName = source_name;
            mFilterCollegeName = university_name;
            mFilterReferrerName = referrer_name;
            filterParams.clear();
            filterParams.putAll(map);
            setRefreshing();
        }

    }

    @Override
    public void onCancelDilaog() {
        getSupportFragmentManager().beginTransaction().remove(dialog).commit();
        dialog.dismiss();
        dialog = null;
    }

    @Override
    public void onRefresh(int action) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mCurrentPage = 1;
        }

        mPresenter.getList4StudentStatistics(filterParams, mCurrentPage++);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_statistics, parent, false);
        return new StatisticsListViewHolder(view);
    }

    class StatisticsListViewHolder extends BaseViewHolder {
        @BindView(R.id.avatar)
        AvatarImageView avatar;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.state)
        TextView state;
        @BindView(R.id.university)
        TextView university;
        @BindView(R.id.phone)
        ImageView phone;

        public StatisticsListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            Student data = mDataList.get(position);
            final String nameStr = data.studentInfo.name;
            final String phoneNumber = data.studentInfo.mobile;
            if (data != null) {
//                PicUtils.loadUserHeader(StatisticsListActivity.this, data.StudentInfo., avatar);
                avatar.setText(data.studentInfo.name, data.studentInfo.id, "");
                name.setText(data.studentInfo.name);
                university.setText(data.studentInfo.college_name);
                state.setText(data.studentInfo.pay_state_name);
                state.setBackgroundResource(R.drawable.bg_round_corner_gray);
            }
            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActionSheet.createBuilder(StatisticsListActivity.this, StatisticsListActivity.this.getSupportFragmentManager())
                            .setCancelButtonTitle(R.string.str_cancel)
                            .setOtherButtonTitles(nameStr + "\n" + phoneNumber, getString(R.string.call), getString(R.string.phone_contacts))
                            .setCancelableOnTouchOutside(true)
                            .setListener(new ActionSheet.ActionSheetListener() {
                                @Override
                                public void onDismiss(ActionSheet actionSheet, boolean b) {

                                }

                                @Override
                                public void onOtherButtonClick(ActionSheet actionSheet, int i) {
                                    switch (i) {
                                        case 1:
                                            // TODO: 16/8/17  打电话
                                            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(callIntent);
                                            break;
                                        case 2:
                                            // TODO: 16/8/17  保存至通讯录
                                            Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                                            intent.setType("vnd.android.cursor.item/person");
                                            intent.setType("vnd.android.cursor.item/contact");
                                            intent.setType("vnd.android.cursor.item/raw_contact");
                                            //    intent.putExtra(android.provider.ContactsContract.Intents.Insert.NAME, name);
                                            intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE, phoneNumber);
                                            intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE_TYPE, 3);
                                            startActivity(intent);
                                            break;
                                    }
                                }
                            }).show();
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            Student data = mDataList.get(position);
            Intent intent = new Intent(StatisticsListActivity.this, StudentDetailActivity.class);
            intent.putExtra(StudentDetailActivity.P_STUDENT, data.studentInfo);
            JumpManager.jumpActivity(StatisticsListActivity.this, intent, Resource.PERMISSION_STU_DETAIL);
        }
    }

    @Override
    public void getListSuccess(StudentList studentList) {
        loadSuccess(studentList.lists);
        setUpTitle(getString(R.string.student_statistics) + getString(R.string.student_statistics_total, studentList.total));
    }


    @Override
    public void getListFail(String msg) {
        loadFailed();
        showToast(msg);
    }

    @OnClick(R.id.fab)
    void add() {
        JumpManager.jumpActivity(this, AddStudentActivity.class, Resource.PERMISSION_ADD_NEW_STU);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //RxBus.getInstance().unSubscribe(this);
        RxBus.getDefault().unregister(StudentInfo.class);
    }
}
