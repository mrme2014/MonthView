package com.ishow.ischool.business.student.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.market.Communication;
import com.ishow.ischool.bean.market.CommunicationItem;
import com.ishow.ischool.bean.market.CommunicationList;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.util.AppUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;


public class CommunicationListFragment extends BaseFragment4Crm<CommunPresenter, CommunModel> implements CommunContract.View {

    private static final String P_STUDENT_ID = "student_id";


    private CommunListAdapter mAdapter;
    private int mStudentId;

    private OnFragmentInteractionListener mListener;
    private int mPage;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public CommunicationListFragment() {
    }

    public static CommunicationListFragment newInstance(int studentId) {
        CommunicationListFragment fragment = new CommunicationListFragment();
        Bundle args = new Bundle();
        args.putInt(P_STUDENT_ID, studentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStudentId = getArguments().getInt(P_STUDENT_ID);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_communication_list;
    }

    @Override
    public void init() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CommunListAdapter(getActivity(), null);
        recyclerView.setAdapter(mAdapter);

        HashMap<String, String> params = AppUtil.getParamsHashMap(7);
        params.put("student_id", mStudentId + "");
        mPresenter.getCommunicationList(params);
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

    @Override
    public void listCommunicationSuccess(CommunicationList data) {

        ArrayList<Communication> datas = data.lists;
        if (datas != null) {
            ArrayList<CommunicationItem> items = new ArrayList<>();
            Communication c = datas.remove(0);
            items.add(new CommunicationItem(CommunicationItem.TYPE_COMMUNICSTION_LATEST, c));
            items.add(new CommunicationItem(CommunicationItem.TYPE_COMMUNICSTION_ADD, null));
            for (Communication comm : datas) {
                items.add(new CommunicationItem(CommunicationItem.TYPE_COMMUNICSTION_CONTENT, comm));
            }
            mAdapter.refresh(items);
        }
    }

    @Override
    public void listCommunicationFailed(String msg) {
        showToast(msg);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Bundle data);
    }
}
