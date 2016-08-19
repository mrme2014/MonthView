package com.ishow.ischool.business.student.detail;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.ishow.ischool.R;
import com.ishow.ischool.adpter.FragmentAdapter;
import com.ishow.ischool.common.base.BaseActivity4Crm;

import java.util.ArrayList;

import butterknife.BindView;

public class StudentDetailActivity extends BaseActivity4Crm<StudentDetailPresenter, StudentDetailModel> implements CommunicationListFragment.OnFragmentInteractionListener, StudentInfoFragment.OnFragmentInteractionListener {

    public static final String P_STUDENT = "student";
    @BindView(R.id.tabs)
    TabLayout mTabs;

    @BindView(R.id.student_viewpager)
    ViewPager mViewPaper;

    private CommunicationListFragment studentInfoFragment;
    private CommunicationListFragment communicationListFragment;
    private FragmentAdapter mFragmentAdapter;

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
        studentInfoFragment = StudentInfoFragment.newInstance(null);
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

    }

    @Override
    public void onFragmentInteraction(Bundle data) {

    }
}
