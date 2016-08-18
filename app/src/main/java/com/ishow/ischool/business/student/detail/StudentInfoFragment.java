package com.ishow.ischool.business.student.detail;

import android.content.Context;
import android.os.Bundle;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.student.StudentStatistics;
import com.ishow.ischool.common.base.BaseFragment4Crm;

/**
 * Created by abel on 16/8/18.
 */

public class StudentInfoFragment extends BaseFragment4Crm<StudentDetailPresenter, StudentDetailModel> {

    private static final String ARG_PARAM = "param";

    private StudentStatistics mParam1;

    private OnFragmentInteractionListener mListener;

    public StudentInfoFragment() {
    }

    public static CommunicationListFragment newInstance(StudentStatistics student) {
        CommunicationListFragment fragment = new CommunicationListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, student);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getParcelable(ARG_PARAM);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_student_info;
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
