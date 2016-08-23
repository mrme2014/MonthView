package com.ishow.ischool.business.student.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.market.Communication;
import com.ishow.ischool.bean.market.CommunicationItem;
import com.ishow.ischool.bean.market.CommunicationList;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.business.communicationadd.CommunicationAddActivity;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.util.AppUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;


public class CommunicationListFragment extends BaseFragment4Crm<CommunPresenter, CommunModel> implements CommunContract.View {

    private static final String P_STUDENT = "student";


    private CommunListAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.empty_layout)
    LinearLayout emptyView;

    @BindView(R.id.communication_add)
    TextView communAddTv;

    public CommunicationListFragment() {
    }

    public static CommunicationListFragment newInstance() {
        CommunicationListFragment fragment = new CommunicationListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_communication_list;
    }

    @Override
    public void init() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CommunListAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        initData();
    }


    public void onButtonPressed(Bundle data) {
        if (mListener != null) {
            mListener.onFragmentInteraction(data);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initData() {
        if (getStudentInfo() != null) {
            HashMap<String, String> params = AppUtil.getParamsHashMap(7);
            params.put("student_id", getStudentInfo().student_id + "");
            mPresenter.getCommunicationList(params);
        }
    }

    @Override
    public void listCommunicationSuccess(CommunicationList data) {

        ArrayList<Communication> datas = data.lists;
        if (datas != null && !datas.isEmpty()) {
            emptyView.setVisibility(View.GONE);
            ArrayList<CommunicationItem> items = new ArrayList<>();
            Communication c = datas.get(0);
            items.add(new CommunicationItem(CommunicationItem.TYPE_COMMUNICSTION_LATEST, c));
            items.add(new CommunicationItem(CommunicationItem.TYPE_COMMUNICSTION_ADD, null));
            for (Communication comm : datas) {
                items.add(new CommunicationItem(CommunicationItem.TYPE_COMMUNICSTION_CONTENT, comm));
            }
            mAdapter.refresh(items);
        } else {
            showEmptyView();
        }
    }

    private void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void listCommunicationFailed(String msg) {
        showToast(msg);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Bundle data);
    }

    @OnClick(R.id.communication_add)
    void onClickCommuncationAdd(View view) {
        Intent intent = new Intent(getActivity(), CommunicationAddActivity.class);
        intent.putExtra(CommunicationAddActivity.P_STUDENT, getStudentInfo());
        JumpManager.jumpActivity(getActivity(), intent);
    }

    private StudentInfo getStudentInfo() {
        return ((StudentDetailActivity) getActivity()).getStudentInfo();
    }

    public void refresh() {
        initData();
    }


}
