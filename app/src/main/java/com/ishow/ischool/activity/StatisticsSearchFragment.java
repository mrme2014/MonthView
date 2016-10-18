package com.ishow.ischool.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.commonlib.Conf;
import com.commonlib.core.BaseListFragment;
import com.commonlib.http.ApiFactory;
import com.commonlib.widget.pull.BaseItemDecor;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.student.Student;
import com.ishow.ischool.bean.student.StudentList;
import com.ishow.ischool.business.student.detail.StudentDetailActivity;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.MarketApi;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.widget.custom.AvatarImageView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/8/25.
 */
public class StatisticsSearchFragment extends BaseListFragment<Student> {

    private HashMap<String, String> searchParams;
    private String mCampusId;
    private String mSource;
    private String mSearchKey = "";

    public static StatisticsSearchFragment newInstance(String campus_id, String source) {
        StatisticsSearchFragment fragment = new StatisticsSearchFragment();
        Bundle args = new Bundle();
        args.putString("campus_id", campus_id);
        args.putString("source", source);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCampusId = bundle.getString("campus_id", "");
            mSource = bundle.getString("source", "");
        }
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new BaseItemDecor(getActivity(), 67);
    }

    @Override
    public void init() {
        searchParams = new HashMap<String, String>();
        searchParams.put("campus_id", mCampusId);
        searchParams.put("source", mSource);
    }

    public void startSearch(String searchKey) {
        mSearchKey = searchKey;
        searchParams.put("mobile_or_name", mSearchKey);
        setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mCurrentPage = 1;
        }

        if (TextUtils.isEmpty(mSearchKey)) {
            loadFailed();
        } else {
            ApiFactory.getInstance().getApi(MarketApi.class)
                    .listStudentStatistics(Resource.MARKET_STUDENT_STATISTICS, searchParams, Conf.DEFAULT_PAGESIZE_LISTVIEW, mCurrentPage++)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiObserver<StudentList>() {
                        @Override
                        public void onSuccess(StudentList studentList) {
                            loadSuccess(studentList.lists);
                        }

                        @Override
                        public void onError(String msg) {
                            loadFailed();
                        }
                    });
        }
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
                    ActionSheet.createBuilder(getActivity(), getActivity().getSupportFragmentManager())
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
            Intent intent = new Intent(getActivity(), StudentDetailActivity.class);
            intent.putExtra(StudentDetailActivity.P_STUDENT, data.studentInfo);
            JumpManager.jumpActivity(getActivity(), intent, Resource.MARKET_STUDENT_STUDENTINFO);
        }
    }
}
