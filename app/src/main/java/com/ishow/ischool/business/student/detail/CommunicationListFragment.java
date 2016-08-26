package com.ishow.ischool.business.student.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.application.Cons;
import com.ishow.ischool.application.Resourse;
import com.ishow.ischool.bean.market.Communication;
import com.ishow.ischool.bean.market.CommunicationItem;
import com.ishow.ischool.bean.market.CommunicationList;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.business.communication.add.CommunicationAddActivity;
import com.ishow.ischool.business.communication.edit.CommunicationEditActivity;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.common.rxbus.RxBus;
import com.ishow.ischool.event.CommunicationRefreshEvent;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.CommunEditDialog;
import com.ishow.ischool.widget.custom.SelectDialogFragment;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;


public class CommunicationListFragment extends BaseFragment4Crm<CommunPresenter, CommunModel> implements CommunContract.View {

    private static final String P_STUDENT = "student";
    private static final int REQUEST_SOURCE = 200;


    private CommunListAdapter mAdapter;

    private OnFragmentInteractionListener mListener;
    private ArrayList<Communication> datas;

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

        setListener();
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
            HashMap<String, String> params = AppUtil.getParamsHashMap(Resourse.COMMUNICATION_LIST);
            params.put("student_id", getStudentInfo().student_id + "");
            params.put("fields", "communicationInfo,studentInfo,userInfo,avatar");
            mPresenter.getCommunicationList(params);
        }
    }

    @Override
    public void listCommunicationSuccess(CommunicationList data) {

        datas = data.lists;
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

    @Override
    public void onEditCommunicationFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void onEditCommunicationSuccedd(HashMap<String, String> params) {
        refresh();
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

    public void refresh() {
        RxBus.getDefault().post(new CommunicationRefreshEvent());
        initData();
    }


    public void setListener() {
        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Communication communication = mAdapter.getItem(0).communication;
                final int communId = communication.communicationInfo.id;

                switch (view.getId()) {

                    case R.id.commun_state:
                        final ArrayList<String> datas = AppUtil.getStateList();
                        AppUtil.showItemDialog(getChildFragmentManager(), datas, new SelectDialogFragment.OnItemSelectedListner() {
                            @Override
                            public void onItemSelected(int position, String txt) {
                                HashMap<String, String> params = AppUtil.getParamsHashMap(Resourse.COMMUNICATION_EDIT);
                                params.put("status", (position + 1) + "");
                                params.put("status_str", datas.get(position));
                                params.put("id", communId + "");
                                mPresenter.editCommunication(params);
                            }
                        });
                        break;
                    case R.id.commun_faith:
                        final ArrayList<String> faiths = AppUtil.getBeliefList();
                        AppUtil.showItemDialog(getChildFragmentManager(), faiths, new SelectDialogFragment.OnItemSelectedListner() {

                            @Override
                            public void onItemSelected(int position, String txt) {
                                HashMap<String, String> params = AppUtil.getParamsHashMap(Resourse.COMMUNICATION_EDIT);
                                params.put("belief", (position + 1) + "");
                                params.put("belief_str", faiths.get(position));
                                params.put("id", communId + "");
                                mPresenter.editCommunication(params);
                            }
                        });
                        break;
                    case R.id.commun_oppose:
                        final ArrayList<String> opposes = AppUtil.getRefuseList();
                        AppUtil.showItemDialog(getChildFragmentManager(), opposes, new SelectDialogFragment.OnItemSelectedListner() {

                            @Override
                            public void onItemSelected(int position, String txt) {
                                HashMap<String, String> params = AppUtil.getParamsHashMap(Resourse.COMMUNICATION_EDIT);
                                params.put("refuse", (position + 1) + "");
                                params.put("refuse_str", opposes.get(position));
                                params.put("id", communId + "");
                                mPresenter.editCommunication(params);
                            }
                        });
                        break;
                    case R.id.commun_source:
                        Intent intent = new Intent(getActivity(), CommunicationEditActivity.class);
                        intent.putExtra(CommunicationEditActivity.P_ID, communId);
                        intent.putExtra(CommunicationEditActivity.P_TITLE, getString(R.string.commun_label_source));
                        intent.putExtra(CommunicationEditActivity.P_TYPE, Cons.Communication.source);
                        intent.putExtra(CommunicationEditActivity.P_TEXT, communication.communicationInfo.tuition_source);
                        startActivityForResult(intent, REQUEST_SOURCE);
                        break;

                    case R.id.commun_back_date:
                        AppUtil.showTimePickerDialog(getChildFragmentManager(), new PickerDialogFragment.Callback<Integer>() {
                            @Override
                            public void onPickResult(Integer object, String... result) {
                                HashMap<String, String> params = AppUtil.getParamsHashMap(Resourse.COMMUNICATION_EDIT);
                                params.put("callback_date", String.valueOf(object));
                                params.put("callback_date_str", result[0]);
                                params.put("id", communId + "");
                                mPresenter.editCommunication(params);
                            }
                        });
                        break;
                    case R.id.item_commun_add_btn:
                        ShowCommunEditDialog();
                        break;
                }
            }
        });
    }

    private void ShowCommunEditDialog() {
        final Communication communication = mAdapter.getItem(0).communication;
        CommunEditDialog dialog = new CommunEditDialog();
        dialog.setOnClickListener(new CommunEditDialog.OnClickListener() {
            @Override
            public void onClick(String content, long date) {
                if (!TextUtils.isEmpty(content) && date != 0) {
                    HashMap<String, String> params = AppUtil.getParamsHashMap(Resourse.COMMUNICATION_EDIT);
                    params.put("content", content);
                    params.put("callback_date", date + "");
                    params.put("student_id", communication.studentInfo.student_id + "");
                    params.put("status", communication.communicationInfo.status + "");
                    params.put("type", communication.communicationInfo.type + "");
                    params.put("result", communication.communicationInfo.result + "");
                    params.put("refuse", communication.communicationInfo.refuse + "");
                    params.put("belief", communication.communicationInfo.belief + "");
                    params.put("tuition_source", communication.communicationInfo.tuition_source + "");
                    params.put("communication_date", communication.communicationInfo.communication_date + "");
                    params.put("campus_id", communication.communicationInfo.campus_id + "");
                    mPresenter.addCommunication(params);
                }

            }
        }).show(getChildFragmentManager(), "dialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_SOURCE) {
                refresh();
            }
        }
    }

    @OnClick(R.id.communication_add)
    void onClickCommunAdd(View view) {
        Intent intent = new Intent(getActivity(), CommunicationAddActivity.class);
        intent.putExtra(CommunicationAddActivity.P_STUDENT_INFO, getStudentInfo());
        JumpManager.jumpActivity(getActivity(), intent);
    }
}
