package com.ishow.ischool.business.student.detail;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.FragmentAdapter;
import com.ishow.ischool.bean.student.Student;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.rxbus.RxBus;
import com.ishow.ischool.util.ColorUtil;
import com.ishow.ischool.widget.custom.AvatarImageView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

public class StudentDetailActivity extends BaseActivity4Crm<StudentDetailPresenter, StudentDetailModel> implements
        CommunicationListFragment.OnFragmentInteractionListener, StudentInfoFragment.OnFragmentInteractionListener,
        StudentDetailContract.View {

    public static final String P_STUDENT = "student";
    public static final String P_STUDENT_ID = "student_id";
    public static final String P_COMMUNICATION = "communication";
    @BindView(R.id.tabs)
    TabLayout mTabs;

    @BindView(R.id.student_viewpager)
    ViewPager mViewPaper;

    @BindView(R.id.student_avatar_iv)
    AvatarImageView avatarTv;

    @BindView(R.id.student_user_name)
    TextView usernameTv;

    @BindView(R.id.student_apply_state)
    LabelTextView applyStateLtv;

    @BindView(R.id.student_class_hour)
    LabelTextView classHourLtv;

    @BindView(R.id.student_tuition)
    LabelTextView tuitionLtv;

    @BindView(R.id.titlebar_title)
    TextView titlebarTitleTv;

    @BindView(R.id.appbar)
    AppBarLayout mAppBar;

    public Student student;
    public int studentId;

    private StudentInfoFragment studentInfoFragment;
    private CommunicationListFragment communicationListFragment;
    private FragmentAdapter mFragmentAdapter;
    private boolean isCommun;
    private boolean needRefresh;

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
        isCommun = getIntent().getBooleanExtra(P_COMMUNICATION, false);

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

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                handleAlphaOnTitle(percentage);
            }
        });
    }

    private void handleAlphaOnTitle(float percentage) {
        titlebarTitleTv.setAlpha(percentage);
        //mToolbar.getBackground().setAlpha((int) (255 * percentage));
    }

    private void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        studentInfoFragment = StudentInfoFragment.newInstance();
        communicationListFragment = CommunicationListFragment.newInstance();
        fragments.add(studentInfoFragment);
        fragments.add(communicationListFragment);

        ArrayList<String> titleList = new ArrayList<>();
        titleList.add(getString(R.string.student_detail));
        titleList.add(getString(R.string.commun_list));
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titleList);
        mViewPaper.setAdapter(mFragmentAdapter);
        mViewPaper.setCurrentItem(isCommun ? 1 : 0);
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

        needRefresh = data.getBoolean("refresh", false);
    }

    @Override
    public void onGetStudentSuccess(Student student) {
        this.student = student;
        updateView(student);
    }

    private void updateView(Student student) {
        avatarTv.setText(student.studentInfo.name);
        avatarTv.setBackgroundColor(ColorUtil.getColorById(student.studentInfo.id));

        usernameTv.setText(student.studentInfo.name);
        applyStateLtv.setText(student.studentInfo.pay_state_name);
        classHourLtv.setText(student.studentInfo.class_hour + "/" + student.studentInfo.class_hour_total);
        tuitionLtv.setText(student.studentInfo.payed + "");
        titlebarTitleTv.setText(student.studentInfo.name);

        studentInfoFragment.refresh();
        communicationListFragment.refresh();
    }

    @Override
    public void onGetStudentFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void onEditStudentSuccess(Object student) {

    }

    @Override
    public void onEditStudentFailed(String msg) {
        showToast(msg);
    }

    public StudentInfo getStudentInfo() {
        if (student != null) {
            return student.studentInfo;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (needRefresh) {
            RxBus.getDefault().post(new StudentInfo());
        }
    }

    @Override
    protected void onNavigationBtnClicked() {
        super.onNavigationBtnClicked();
        if (needRefresh) {
            RxBus.getDefault().post(new StudentInfo());
        }
    }
}
