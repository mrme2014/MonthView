package com.ishow.ischool.business.student.detail;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.FragmentAdapter;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.util.AppUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

public class StudentDetailActivity extends BaseActivity4Crm<StudentDetailPresenter, StudentDetailModel> implements
        CommunicationListFragment.OnFragmentInteractionListener, StudentInfoFragment.OnFragmentInteractionListener,
        StudentDetailContract.View {

    public static final String P_STUDENT = "student";
    public static final String P_STUDENT_ID = "student_id";
    @BindView(R.id.tabs)
    TabLayout mTabs;

    @BindView(R.id.student_viewpager)
    ViewPager mViewPaper;

    @BindView(R.id.student_avatar_iv)
    TextView avatarTv;

    @BindView(R.id.student_user_name)
    TextView usernameTv;

    @BindView(R.id.student_apply_state)
    LabelTextView applyStateLtv;

    @BindView(R.id.student_class_hour)
    LabelTextView classHourLtv;

    @BindView(R.id.student_tuition)
    LabelTextView tuitionLtv;

    public StudentInfo student;
    public int studentId;

    private StudentInfoFragment studentInfoFragment;
    private CommunicationListFragment communicationListFragment;
    private FragmentAdapter mFragmentAdapter;

    @Override
    protected void initEnv() {
        super.initEnv();
        studentId = getIntent().getIntExtra(P_STUDENT_ID, 0);
        if (studentId == 0) {
            StudentInfo student = getIntent().getParcelableExtra(P_STUDENT);
            if (student != null) {
                studentId = student.student_id;
            }
        }

    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_student_detail, -1, -1, MODE_BACK);
    }

    @Override
    protected void setUpView() {

        initViewPager();

        mTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabs.setupWithViewPager(mViewPaper);
    }

    private void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        studentInfoFragment = StudentInfoFragment.newInstance(student);
        communicationListFragment = CommunicationListFragment.newInstance("");
        fragments.add(studentInfoFragment);
        fragments.add(communicationListFragment);

        ArrayList<String> titleList = new ArrayList<>();
        titleList.add(getString(R.string.student_detail));
        titleList.add(getString(R.string.commun_list));
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titleList);
        mViewPaper.setAdapter(mFragmentAdapter);
        mViewPaper.setCurrentItem(0);
    }

    @Override
    protected void setUpData() {
        if (studentId != 0) {
            HashMap<String, String> params = new HashMap<>();
            params.put("id", studentId + "");
            params.put("resources_id", "7");
            mPresenter.getStudent(params);
        }
    }

    @Override
    public void onFragmentInteraction(Bundle data) {

    }

    @Override
    public void onGetStudentSuccess(StudentInfo student) {
        this.student = student;
        updateView(student);
    }

    private void updateView(StudentInfo student) {
        avatarTv.setText(AppUtil.getLast2Text(student.name));
        usernameTv.setText(student.name);
        applyStateLtv.setText(student.pay_state_name);
        classHourLtv.setText(student.class_hour + "/" + student.class_hour_total);
        tuitionLtv.setText(student.payed + "");
        studentInfoFragment.refresh(student);
    }

    @Override
    public void onGetStudentFailed(String msg) {

    }
}
