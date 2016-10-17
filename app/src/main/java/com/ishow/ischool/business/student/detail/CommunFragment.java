package com.ishow.ischool.business.student.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.market.Communication;
import com.ishow.ischool.bean.market.CommunicationList;
import com.ishow.ischool.bean.student.Student;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.common.rxbus.RxBus;
import com.ishow.ischool.event.CommunicationAddRefreshEvent;
import com.ishow.ischool.event.CommunicationEditRefreshEvent;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.CommunEditDialog;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import rx.functions.Action1;


public class CommunFragment extends BaseFragment4Crm<CommunPresenter, CommunModel> implements CommunContract.View {

    private int mCurrentPage = 1;
    private CommunListAdapter mAdapter;

    private OnFragmentInteractionListener mListener;
    private ArrayList<Communication> datas;
    private boolean needRefresh;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.empty_layout)
    LinearLayout emptyView;

    public CommunFragment() {
    }

    public static CommunFragment newInstance() {
        CommunFragment fragment = new CommunFragment();
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

        RxBus.getDefault().register(CommunicationAddRefreshEvent.class, new Action1<CommunicationAddRefreshEvent>() {
            @Override
            public void call(CommunicationAddRefreshEvent o) {
                needRefresh = true;
//                initData();
            }
        });

        RxBus.getDefault().register(CommunicationEditRefreshEvent.class, new Action1<CommunicationEditRefreshEvent>() {
            @Override
            public void call(CommunicationEditRefreshEvent o) {
                needRefresh = true;
//                initData();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needRefresh) {
            initData();
            needRefresh = false;
        }
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
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.getDefault().unregister(CommunicationAddRefreshEvent.class);
        RxBus.getDefault().unregister(CommunicationEditRefreshEvent.class);
    }

    public void initData() {
        if (getStudentInfo() != null) {
            HashMap<String, String> params = AppUtil.getParamsHashMap(Resource.COMMUNICATION_LIST);
            params.put("student_id", getStudentInfo().student_id + "");
            params.put("fields", "communicationInfo,studentInfo,userInfo,avatar");
            mPresenter.getCommunicationList(params, mCurrentPage);
        }
    }

    @Override
    public void listCommunicationSuccess(CommunicationList data) {

        datas = data.lists;
        if (datas != null && !datas.isEmpty()) {
            emptyView.setVisibility(View.GONE);
            mAdapter.refresh(data.lists);
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

    @Override
    public void onAddCommunicationSuccess() {
        refresh();
    }

    @Override
    public void onAddCommunicationFailed(String msg) {
        showToast(msg);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Bundle data);
    }

    private StudentInfo getStudentInfo() {
        return ((StudentDetailActivity) getActivity()).getStudentInfo();
    }

    private Student getStudent() {
        return ((StudentDetailActivity) getActivity()).getStudent();
    }

    public void refresh() {
        RxBus.getDefault().post(new CommunicationEditRefreshEvent());
        initData();
    }

    private void showCommunEditDialog() {
        final Communication communication = mAdapter.getItem(0);
        CommunEditDialog dialog = new CommunEditDialog();
        dialog.setOnClickListener(new CommunEditDialog.OnClickListener() {
            @Override
            public void onClick(String content, long date) {
                if (!TextUtils.isEmpty(content) && date != 0) {
                    HashMap<String, String> params = AppUtil.getParamsHashMap(Resource.COMMUNICATION_EDIT);
                    params.put("content", content);
                    params.put("callback_date", date + "");
                    params.put("student_id", communication.studentInfo.student_id + "");
                    params.put("status", communication.communicationInfo.status + "");
                    params.put("type", communication.communicationInfo.type + "");
                    params.put("result", communication.communicationInfo.result + "");
                    params.put("refuse", communication.communicationInfo.refuse + "");
                    params.put("belief", communication.communicationInfo.belief + "");
                    params.put("tuition_source", communication.communicationInfo.tuition_source + "");
                    params.put("communication_date", date + "");
                    params.put("campus_id", communication.communicationInfo.campus_id + "");
                    mPresenter.addCommunication(params);
                }

            }
        }).show(getChildFragmentManager(), "dialog");
    }

}
