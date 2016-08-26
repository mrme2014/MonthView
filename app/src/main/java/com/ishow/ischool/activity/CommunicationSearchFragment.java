package com.ishow.ischool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonlib.Conf;
import com.commonlib.core.BaseListFragment;
import com.commonlib.http.ApiFactory;
import com.commonlib.util.DateUtil;
import com.commonlib.widget.LabelTextView;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resourse;
import com.ishow.ischool.bean.market.Communication;
import com.ishow.ischool.bean.market.CommunicationList;
import com.ishow.ischool.business.student.detail.StudentDetailActivity;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.CommunicationApi;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.util.ColorUtil;
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
public class CommunicationSearchFragment extends BaseListFragment<Communication> {

    private HashMap<String, String> searchParams;
    private String mResourceId;

    public static CommunicationSearchFragment newInstance(String resources_id) {
        CommunicationSearchFragment fragment = new CommunicationSearchFragment();
        Bundle args = new Bundle();
        args.putString("resources_id", resources_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mResourceId = bundle.getString("resources_id", "");
        }
    }

    @Override
    public void init() {
        searchParams = new HashMap<String, String>();
        searchParams.put("resources_id", mResourceId);
    }

    public void startSearch(String searchKey) {
        searchParams.put("keyword", searchKey);
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

        ApiFactory.getInstance().getApi(CommunicationApi.class)
                .listCommnunication(searchParams, Conf.DEFAULT_PAGESIZE_LISTVIEW, mCurrentPage++)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<CommunicationList>() {
                    @Override
                    public void onSuccess(CommunicationList communicationList) {
                        loadSuccess(communicationList.lists);
                    }

                    @Override
                    public void onError(String msg) {
                        loadFailed();
                    }
                });
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_communication_list, parent, false);
        return new CommnunicationHolder(view);
    }

    class CommnunicationHolder extends BaseViewHolder {
        @BindView(R.id.user_photo_iv)
        AvatarImageView userPhotoIv;
        @BindView(R.id.user_name)
        TextView usernameTv;
        @BindView(R.id.communication_date)
        TextView dateTv;
        @BindView(R.id.communication_content)
        TextView contentTv;
        @BindView(R.id.user_state)
        LabelTextView stateTv;
        @BindView(R.id.user_oppose_point)
        LabelTextView opposePointTv;
        @BindView(R.id.user_faith)
        LabelTextView faithTv;

        public CommnunicationHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            Communication communication = mDataList.get(position);
            userPhotoIv.setText(communication.studentInfo.name);
            userPhotoIv.setBackgroundColor(ColorUtil.getColorById(communication.studentInfo.id));
//            GradientDrawable myGrad = (GradientDrawable) userPhotoIv.getBackground();
//            myGrad.setColor(ColorUtil.getColorById(communication.studentInfo.id));

            usernameTv.setText(communication.studentInfo.name);
            dateTv.setText(DateUtil.parseDate2Str(communication.communicationInfo.update_time, "yyyy-MM-dd"));
            contentTv.setText(communication.communicationInfo.content);
            stateTv.setText(AppUtil.getStateById(communication.communicationInfo.status));
            opposePointTv.setText(AppUtil.getRefuseById(communication.communicationInfo.refuse));
            faithTv.setText(AppUtil.getBeliefById(communication.communicationInfo.belief));
        }

        @Override
        public void onItemClick(View view, int position) {
            Communication communication = mDataList.get(position);
            Intent intent = new Intent(getActivity(), StudentDetailActivity.class);
            intent.putExtra(StudentDetailActivity.P_COMMUNICATION, true);
            intent.putExtra(StudentDetailActivity.P_STUDENT_ID, communication.studentInfo.student_id);
            JumpManager.jumpActivity(getActivity(), intent, Resourse.PERMISSION_STU_DETAIL);
        }
    }
}
