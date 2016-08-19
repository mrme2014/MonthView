package com.ishow.ischool.business.student.detail;

import android.content.Context;
import android.os.Bundle;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.common.base.BaseFragment4Crm;


public class CommunicationListFragment extends BaseFragment4Crm<StudentDetailPresenter, StudentDetailModel> {

    private static final String ARG_PARAM = "param";

    private String mParam1;

    private OnFragmentInteractionListener mListener;

    public CommunicationListFragment() {
    }

    public static CommunicationListFragment newInstance(String param1) {
        CommunicationListFragment fragment = new CommunicationListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_communication_list;
    }

    @Override
    public void init() {

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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Bundle data);
    }
}
