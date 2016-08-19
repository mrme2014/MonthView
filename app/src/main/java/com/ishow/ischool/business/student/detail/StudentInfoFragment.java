package com.ishow.ischool.business.student.detail;

import android.content.Context;
import android.os.Bundle;

import com.commonlib.util.DateUtil;
import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.common.base.BaseFragment4Crm;

import butterknife.BindView;

/**
 * Created by abel on 16/8/18.
 */

public class StudentInfoFragment extends BaseFragment4Crm<StudentDetailPresenter, StudentDetailModel> {

    private static final String ARG_PARAM = "param";

    private StudentInfo mParam1;

    private OnFragmentInteractionListener mListener;
    private StudentInfo mStudent;

    @BindView(R.id.student_english_name)
    LabelTextView englishNameTv;
    @BindView(R.id.student_phone)
    LabelTextView phoneTv;
    @BindView(R.id.student_qq)
    LabelTextView qqTv;
    @BindView(R.id.student_birthday)
    LabelTextView birthdayTv;
    @BindView(R.id.student_school)
    LabelTextView schoolTv;
    @BindView(R.id.student_specialty)
    LabelTextView specialtyTv;
    @BindView(R.id.student_class)
    LabelTextView classTv;
    @BindView(R.id.student_idcard)
    LabelTextView idcardTv;

    public StudentInfoFragment() {
    }

    public static StudentInfoFragment newInstance(StudentInfo student) {
        StudentInfoFragment fragment = new StudentInfoFragment();
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

    public void refresh(StudentInfo student) {
        if (student == null) {
            return;
        }
        this.mStudent = student;
        updateView();
    }

    private void updateView() {
        englishNameTv.setText(mStudent.english_name);
        phoneTv.setText(mStudent.mobile + "");
        qqTv.setText(mStudent.qq + "");
        birthdayTv.setText(DateUtil.parseDate2Str((long) mStudent.birthday * 1000));
        schoolTv.setText(mStudent.college_name);
        specialtyTv.setText(mStudent.major);
        classTv.setText(mStudent.grade);
        idcardTv.setText(mStudent.idcard);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Bundle data);
    }
}
