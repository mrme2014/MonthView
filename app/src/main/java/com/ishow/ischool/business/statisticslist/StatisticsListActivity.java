package com.ishow.ischool.business.statisticslist;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.commonlib.util.LogUtil;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Cons;
import com.ishow.ischool.application.CrmApplication;
import com.ishow.ischool.bean.student.Student;
import com.ishow.ischool.bean.student.StudentList;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.business.addstudent.AddStudentActivity;
import com.ishow.ischool.business.student.detail.StudentDetailActivity;
import com.ishow.ischool.common.api.MarketApi;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.widget.custom.StatisticsFilterFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wqf on 16/8/14.
 */
public class StatisticsListActivity extends BaseListActivity4Crm<StatisticsListPresenter, StatisticsListModel, Student> implements StatisticsListContract.View,
        StatisticsFilterFragment.FilterCallback {

    @BindView(R.id.fab)
    FloatingActionButton addFab;

    //  搜索
    private SearchView mSearchView;
    private String mSearchKey;
    private boolean mSearchMode = false;

    // 筛选
    private CrmApplication app;
    private HashMap<String, String> params;
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

        app = (CrmApplication)getApplication();
        params = app.getFilerParam();
        if (params.get("campus_id") == null || params.get("source") == null) {
            // 之前没有筛选过
            if (mUser.userInfo.campus_id == Campus.HEADQUARTERS) {
                params.put("campus_id", Campus.HEADQUARTERS + "");                  // 总部获取学院统计列表campus_id传1
            } else {
                params.put("campus_id", mUser.userInfo.campus_id + "");
            }

            curPositionId = mUser.positionInfo.id;
            if (curPositionId == Cons.Position.Chendujiangshi.ordinal()) {
                params.put("source", MarketApi.TYPESOURCE_READING + "");
            } else if (curPositionId == Cons.Position.Xiaoliaozhuanyuan.ordinal()) {
                params.put("source", MarketApi.TYPESOURCE_CHAT + "");
            } else {
                params.put("source", "-1");
            }
        }
        mFilterSourceName = params.get(new String("source_name"));
        mFilterCollegeName = params.get(new String("college_name"));
        mFilterReferrerName = params.get(new String("referrer_name"));

        final MenuItem searchItem = mToolbar.getMenu().findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setQueryHint("根据姓名或手机号搜索..");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSearchMode = true;
                LogUtil.d("SearchView newText = " + newText);
                mSearchKey = newText;
                if (TextUtils.isEmpty(mSearchKey)) {
                    params.remove("mobile_or_name");
                } else {
                    params.put("mobile_or_name", mSearchKey);
                }
                setRefreshing();
                return true;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSearchMode = false;
                mSearchKey = "";
                params.remove("mobile_or_name");
                // 此处拿到原始的数据，mCurrentPage置为2，即模拟刚刚刷新了一次
                return false;
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                if (dialog == null) {
                    dialog = StatisticsFilterFragment.newInstance(params, mFilterSourceName, mFilterCollegeName, mFilterReferrerName);
                    dialog.setOnFilterCallback(StatisticsListActivity.this);
                }
                dialog.show(getSupportFragmentManager(), "dialog");
                break;
        }
        return true;
    }

    /**
     * @param map       筛选参数
     * @param source_name       用于缓存筛选后，再次进入时填充内容（来源方式）
     * @param university_name   用于缓存筛选后，再次进入时填充内容（就读学校）
     * @param referrer_name     用于缓存筛选后，再次进入时填充内容 (推荐人)
     */
    @Override
    public void onFinishFilter(HashMap<String, String> map, String source_name, String university_name, String referrer_name) {
        dialog = null;
        HashMap<String,String> hashMap = app.getFilerParam();
        if (!hashMap.equals(map)) {
            mFilterSourceName = source_name;
            mFilterCollegeName = university_name;
            mFilterReferrerName = referrer_name;
            params.clear();
            params.putAll(map);

            // cache
            if (!TextUtils.isEmpty(mFilterSourceName)) {
                params.put("source_name", mFilterSourceName);
            }
            if (!TextUtils.isEmpty(mFilterCollegeName)) {
                params.put("college_name", mFilterCollegeName);
            }
            if (!TextUtils.isEmpty(mFilterReferrerName)) {
                params.put("referrer_name", mFilterReferrerName);
            }

            app.setFilerParam(params);
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
        mPresenter.getList4StudentStatistics(params, mCurrentPage++);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_statistics, parent, false);
        return new StatisticsListViewHolder(view);
    }

    class StatisticsListViewHolder extends BaseViewHolder {
        @BindView(R.id.avatar)
        ImageView avatar;
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
                name.setText(data.studentInfo.name);
                university.setText(data.studentInfo.college_name);
//                state.setText(UserUtil.getUserPayState(data.applyInfo.status));
                state.setText(data.studentInfo.pay_state_name);
                state.setBackgroundResource(R.drawable.bg_round_corner_gray);
//                if (data.applyInfo.status == )
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
                                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                                            Uri data = Uri.parse("tel:" + phoneNumber);
                                            callIntent.setData(data);
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
            JumpManager.jumpActivity(StatisticsListActivity.this, intent);
        }
    }

    @Override
    public void getListSuccess(StudentList studentList) {
        if (mSearchMode && mCurrentPage == 2) {
            mDataList.clear();
        }
        loadSuccess(studentList.lists);
    }


    @Override
    public void getListFail(String msg) {
        loadFailed();
    }

    @OnClick(R.id.fab)
    void add() {
        JumpManager.jumpActivity(this, AddStudentActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
